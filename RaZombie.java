import greenfoot.*;
import java.util.List;

public class RaZombie extends Zombie {
    private static final int STEAL_COOLDOWN = 100;
    private static final int ANIM_STEAL_DELAY = 60;

    private GreenfootImage[] wNormal, eNormal, wDeath;
    private GreenfootImage[] sStart, sLoop, sEnd;

    private enum StealPhase { NONE, START, LOOP, END }
    private StealPhase phase = StealPhase.NONE;

    private FallingSun targetSun = null;
    private int sunActionTimer = 0;
    private int sunStolenCount = 0;
    private boolean isStealing = false;

    public RaZombie() {
        super(ZombieConfig.RA);
        this.walkSpeed = (Greenfoot.getRandomNumber(6) + 22) / 100.0;

        wNormal = importSprites(ZombieAssets.RA_WALK.path,        ZombieAssets.RA_WALK.count,        0.45);
        eNormal = importSprites(ZombieAssets.RA_EAT.path,         ZombieAssets.RA_EAT.count,         0.45);
        wDeath  = importSprites(ZombieAssets.RA_DEATH.path,       ZombieAssets.RA_DEATH.count,       0.45);
        sStart  = importSprites(ZombieAssets.RA_POWER_START.path, ZombieAssets.RA_POWER_START.count, 0.45);
        sLoop   = importSprites(ZombieAssets.RA_POWER_LOOP.path,  ZombieAssets.RA_POWER_LOOP.count,  0.45);
        sEnd    = importSprites(ZombieAssets.RA_POWER_END.path,   ZombieAssets.RA_POWER_END.count,   0.45);

        setState(new WalkingState(this));
    }

    @Override
    public void update() {
        if (getWorld() == null || !getWorld().getObjects(Overlay.class).isEmpty()) return;

        if (!isLiving()) {
            deathAnim();
            return;
        }

        handleSliding();
        handleThresholds();
        handleStealing();

        if (!isStealing) {
            boolean isEating = checkEating();
            if (currentState != null) currentState.update();
            if (isEating) {
                playEating();
                animate(eNormal, 200, true);
            } else {
                walk();
                animate(wNormal, 200, true);
            }
        }
    }

    private void handleStealing() {
        if (isStealing && (targetSun == null || targetSun.getWorld() == null)) {
            resetStealing();
            return;
        }

        switch (phase) {
            case NONE:
                if (eating) { sunActionTimer = 0; break; }
                if (++sunActionTimer >= STEAL_COOLDOWN) {
                    FallingSun best = getBestSun();
                    if (best != null) {
                        targetSun = best;
                        isStealing = true;
                        phase = StealPhase.START;
                        frame = 0;
                        targetSun.setBeingStolen(true);
                    } else {
                        sunActionTimer = 0;
                    }
                }
                break;

            case START:
                if (animate(sStart, ANIM_STEAL_DELAY, false)) {
                    phase = StealPhase.LOOP;
                    frame = 0;
                }
                break;

            case LOOP:
                animate(sLoop, ANIM_STEAL_DELAY, true);
                pullSunToward();
                break;

            case END:
                if (animate(sEnd, ANIM_STEAL_DELAY, false)) {
                    resetStealing();
                }
                break;
        }
    }

    private void pullSunToward() {
        if (targetSun == null) return;

        double dx = getX() - targetSun.getX();
        double dy = getY() - targetSun.getY();
        double dist = Math.hypot(dx, dy);

        if (dist > 10) {
            targetSun.setLocation(
                targetSun.getX() + (int)(dx / dist * 4),
                targetSun.getY() + (int)(dy / dist * 4)
            );
        }

        if (intersects(targetSun)) {
            sunStolenCount++;
            getWorld().removeObject(targetSun);
            targetSun = null;
            phase = StealPhase.END;
            frame = 0;
        }
    }

    private void resetStealing() {
        if (targetSun != null) targetSun.setBeingStolen(false);
        phase = StealPhase.NONE;
        isStealing = false;
        sunActionTimer = 0;
        targetSun = null;
        setState(new WalkingState(this));
    }

    private FallingSun getBestSun() {
        List<FallingSun> suns = (List<FallingSun>) getWorld().getObjects(FallingSun.class);
        FallingSun best = null;
        double minDist = Double.MAX_VALUE;

        for (FallingSun sun : suns) {
            if (sun.isBeingStolen()) continue;
            double d = Math.hypot(getX() - sun.getX(), getY() - sun.getY());
            if (d < minDist && isClosestTo(sun)) {
                minDist = d;
                best = sun;
            }
        }
        return best;
    }

    private boolean isClosestTo(FallingSun sun) {
        for (Object obj : getWorld().getObjects(RaZombie.class)) {
            RaZombie ra = (RaZombie) obj;
            if (ra == this || !ra.isLiving()) continue;
            if (Math.hypot(ra.getX() - sun.getX(), ra.getY() - sun.getY())<
                Math.hypot(getX() - sun.getX(), getY() - sun.getY())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void deathAnim() {
        if (!resetAnim && sunStolenCount > 0) {
            for (int i = 0; i < sunStolenCount; i++) {
                if (getWorld() != null) {
                    int rx = Greenfoot.getRandomNumber(40) - 20;
                    int ry = Greenfoot.getRandomNumber(40) - 20;
                    getWorld().addObject(new FallingSun(), getX() + rx, getY() + ry);
                }
            }
            sunStolenCount = 0;
            resetAnim = true;
        }
        if (animate(wDeath, 300, false)) {
            getWorld().removeObject(this);
        }
    }

    @Override
    public GreenfootImage[] getCurrentAnimation(boolean isEating) {
        if (isStealing) {
            if (phase == StealPhase.START) return sStart;
            if (phase == StealPhase.LOOP)  return sLoop;
            return sEnd;
        }
        return isEating ? eNormal : wNormal;
    }

    @Override
    protected void handleThresholds() {}
}