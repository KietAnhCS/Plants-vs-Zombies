import greenfoot.*;
import java.util.List;

public class RaZombie extends Zombie {
    private static final int WIDTH  = 120;
    private static final int HEIGHT = 120;
    private static final int STEAL_COOLDOWN  = 1000;
    private static final int STEAL_DURATION  = 400;
    private static final int SUN_MOVE_SPEED  = 3;
    private static final int ANIM_DELAY      = 60;

    private final GreenfootImage[] wNormal, eNormal, wDeath;
    private final GreenfootImage[] sStart, sLoop, sEnd;

    private enum StealPhase { NONE, START, LOOP, END }
    private StealPhase phase = StealPhase.NONE;

    private FallingSun targetSun   = null;
    private int sunActionTimer     = 0;
    private int stealDuration      = 0;
    private int sunStolenCount     = 0;
    private boolean isStealing     = false;
    private boolean resetAnim      = false;

    public RaZombie() {
        super(ZombieConfig.RA);
        wNormal = scale(importSprites(ZombieAssets.RA_WALK.path,        39));
        eNormal = scale(importSprites(ZombieAssets.RA_EAT.path,         50));
        wDeath  = scale(importSprites(ZombieAssets.RA_DEATH.path,       30));
        sStart  = scale(importSprites(ZombieAssets.RA_POWER_START.path, 20));
        sLoop   = scale(importSprites(ZombieAssets.RA_POWER_LOOP.path,  30));
        sEnd    = scale(importSprites(ZombieAssets.RA_POWER_END.path,   38));
        setState(new WalkingState(this));
    }

    private GreenfootImage[] scale(GreenfootImage[] imgs) {
        for (GreenfootImage img : imgs) img.scale(WIDTH, HEIGHT);
        return imgs;
    }

    @Override
    public void act() {
        if (getWorld() == null || !getWorld().getObjects(Overlay.class).isEmpty()) return;
        if (!isLiving()) { deathAnim(); return; }
        handleStealing();
        if (!isStealing) { updateLogic(); }
    }

    @Override
    protected void handleThresholds() {}

    @Override
    public void hit(int dmg) {
        if (!isLiving()) return;
        AudioManager.getInstance().playSound(80, false, "splat.mp3");
        String path = isStealing ? stealPath() : (checkEating() ? ZombieAssets.RA_EAT.path : ZombieAssets.RA_WALK.path);
        hitFlash(path);
        super.hit(dmg);
    }

    private String stealPath() {
        switch (phase) {
            case START: return ZombieAssets.RA_POWER_START.path;
            case LOOP:  return ZombieAssets.RA_POWER_LOOP.path;
            default:    return ZombieAssets.RA_POWER_END.path;
        }
    }

    private void handleStealing() {
        if (isStealing && (targetSun == null || targetSun.getWorld() == null)) {
            resetStealing();
            return;
        }
        switch (phase) {
            case NONE:
                if (++sunActionTimer >= STEAL_COOLDOWN) {
                    FallingSun best = getBestSun();
                    if (best != null) {
                        targetSun = best;
                        isStealing = true;
                        phase = StealPhase.START;
                        setFrame(1);
                    }
                }
                break;
            case START:
                animate(sStart, ANIM_DELAY, false);
                if (getCurrentFrame() >= sStart.length) { phase = StealPhase.LOOP; setFrame(1); }
                break;
            case LOOP:
                animate(sLoop, ANIM_DELAY, true);
                pullSunToward();
                if (++stealDuration >= STEAL_DURATION) { phase = StealPhase.END; stealDuration = 0; setFrame(1); }
                break;
            case END:
                animate(sEnd, ANIM_DELAY, false);
                if (getCurrentFrame() >= sEnd.length) resetStealing();
                break;
        }
    }

    private void pullSunToward() {
        if (targetSun == null || targetSun.getWorld() == null) return;
        targetSun.setBeingStolen(true);
        double dx = getX() - targetSun.getX();
        double dy = getY() - targetSun.getY();
        double dist = Math.hypot(dx, dy);
        if (dist > 10) {
            targetSun.setLocation(
                targetSun.getX() + (int)(dx / dist * SUN_MOVE_SPEED),
                targetSun.getY() + (int)(dy / dist * SUN_MOVE_SPEED)
            );
        }
        if (intersects(targetSun)) {
            sunStolenCount++;
            getWorld().removeObject(targetSun);
            targetSun = null;
            AudioManager.getInstance().playSound(80, false, "sun_collect.mp3");
            phase = StealPhase.END;
            setFrame(1);
        }
    }

    private void resetStealing() {
        phase = StealPhase.NONE;
        isStealing = false;
        sunActionTimer = 0;
        stealDuration = 0;
        targetSun = null;
    }

    private FallingSun getBestSun() {
        FallingSun best = null;
        double minDist = Double.MAX_VALUE;
        for (FallingSun sun : getWorld().getObjects(FallingSun.class)) {
            double d = Math.hypot(getX() - sun.getX(), getY() - sun.getY());
            if (d < minDist && isClosest(sun)) { minDist = d; best = sun; }
        }
        return best;
    }

    private boolean isClosest(FallingSun sun) {
        double myDist = Math.hypot(getX() - sun.getX(), getY() - sun.getY());
        for (RaZombie other : getWorld().getObjects(RaZombie.class)) {
            if (other == this || !other.isLiving()) continue;
            if (Math.hypot(other.getX() - sun.getX(), other.getY() - sun.getY()) < myDist) return false;
        }
        return true;
    }

    @Override
    protected void deathAnim() {
        if (!resetAnim) {
            resetAnim = true;
            setFrame(1);
            if (targetSun != null) targetSun.setBeingStolen(false);
            for (int i = 0; i < sunStolenCount; i++) {
                getWorld().addObject(new FallingSun(),
                    getX() + Greenfoot.getRandomNumber(60) - 30,
                    getY() + Greenfoot.getRandomNumber(60) - 30);
            }
            sunStolenCount = 0;
        }
        animate(wDeath, 75, false);
        if (getCurrentFrame() >= wDeath.length) { removeFromRow(); getWorld().removeObject(this); }
    }

    public boolean isArmless() { return false; }

    @Override
    public GreenfootImage[] getCurrentAnimation(boolean isEating) { return isEating ? eNormal : wNormal; }
}