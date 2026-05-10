import greenfoot.*;
import java.util.List;

public class RaZombie extends Zombie {
    public GreenfootImage[] wNormal, eNormal, wDeath;
    public GreenfootImage[] sStart, sLoop, sEnd;
    
    private final int WIDTH = 120;
    private final int HEIGHT = 120; 
    
    private int sunActionTimer = 0;
    private int stealDuration = 0;
    private int sunStolenCount = 0;
    private boolean isStealing = false;
    private boolean resetAnim = false;
    
    private FallingSun targetSun = null;
    private enum StealPhase { NONE, START, LOOP, END }
    private StealPhase currentPhase = StealPhase.NONE;

    public RaZombie() {
        super(ZombieConfig.RA);

        wNormal = importAndScale(ZombieAssets.RA_WALK.path, 39);
        eNormal = importAndScale(ZombieAssets.RA_EAT.path, 50); 
        wDeath  = importAndScale(ZombieAssets.RA_DEATH.path, 30);

        sStart = importAndScale(ZombieAssets.RA_POWER_START.path, 20);
        sLoop  = importAndScale(ZombieAssets.RA_POWER_LOOP.path, 30);
        sEnd   = importAndScale(ZombieAssets.RA_POWER_END.path, 38);

        setState(new WalkingState(this));
    }
    
    private GreenfootImage[] importAndScale(String path, int count) {
        GreenfootImage[] images = importSprites(path, count);
        for (GreenfootImage img : images) {
            img.scale(WIDTH, HEIGHT);
        }
        return images;
    }

    @Override
    public void act() {
        if (getWorld() == null) return;
        if (!getWorld().getObjects(Overlay.class).isEmpty()) return;
    
        if (!isLiving()) {
            deathAnim();
            return;
        }
    
        handleSunStealingLogic();
    
        if (!isStealing) {
            updateLogic();
            handleThresholds();
        }
    }

    @Override
    protected void handleThresholds() {
    } 

    @Override
    public void hit(int dmg) {
        if (!isLiving()) return;

        AudioManager.getInstance().playSound(80, false, "splat.mp3");

        String currentPath;
        if (isStealing) {
            if (currentPhase == StealPhase.START) currentPath = ZombieAssets.RA_POWER_START.path;
            else if (currentPhase == StealPhase.LOOP) currentPath = ZombieAssets.RA_POWER_LOOP.path;
            else currentPath = ZombieAssets.RA_POWER_END.path;
        } else {
            currentPath = checkEating() ? ZombieAssets.RA_EAT.path : ZombieAssets.RA_WALK.path;
        }
        
        hitFlash(currentPath);
        super.hit(dmg);
    }

    private void handleSunStealingLogic() {
        if (isStealing && (targetSun == null || targetSun.getWorld() == null)) {
            cancelStealing();
            return;
        }

        if (currentPhase == StealPhase.NONE) {
            sunActionTimer++;
            if (sunActionTimer >= 1000) {
                FallingSun bestSun = getBestSunToSteal();
                if (bestSun != null) {
                    targetSun = bestSun;
                    isStealing = true;
                    currentPhase = StealPhase.START;
                    setFrame(1);
                }
            }
            return;
        }

        switch (currentPhase) {
            case START:
                animate(sStart, 60, false);
                if (getCurrentFrame() >= sStart.length) {
                    currentPhase = StealPhase.LOOP;
                    setFrame(1);
                }
                break;
            case LOOP:
                animate(sLoop, 60, true);
                processStealing();
                stealDuration++;
                if (stealDuration >= 400) { 
                    currentPhase = StealPhase.END;
                    stealDuration = 0;
                    setFrame(1);
                }
                break;
            case END:
                animate(sEnd, 60, false);
                if (getCurrentFrame() >= sEnd.length) {
                    resetStealing();
                }
                break;
        }
    }

    private void processStealing() {
        if (targetSun == null || targetSun.getWorld() == null) return;
        targetSun.setBeingStolen(true); 
        int dx = getX() - targetSun.getX();
        int dy = getY() - targetSun.getY();
        double dist = Math.sqrt(dx*dx + dy*dy);
        if (dist > 10) {
            int speed = 3; 
            targetSun.setLocation(targetSun.getX() + (int)(dx/dist * speed), targetSun.getY() + (int)(dy/dist * speed));
        }
        if (this.intersects(targetSun)) {
            sunStolenCount++;
            getWorld().removeObject(targetSun);
            targetSun = null;
            AudioManager.getInstance().playSound(80, false, "sun_collect.mp3");
            currentPhase = StealPhase.END;
            setFrame(1);
        }
    }

    private void cancelStealing() {
        targetSun = null;
        isStealing = false;
        currentPhase = StealPhase.NONE;
        sunActionTimer = 0;
    }

    private FallingSun getBestSunToSteal() {
        List<FallingSun> suns = getWorld().getObjects(FallingSun.class);
        FallingSun bestChoice = null;
        double minDistance = Double.MAX_VALUE;
        for (FallingSun sun : suns) {
            double dist = Math.hypot(getX() - sun.getX(), getY() - sun.getY());
            if (isClosestRaZombie(sun) && dist < minDistance) {
                minDistance = dist;
                bestChoice = sun;
            }
        }
        return bestChoice;
    }

    private boolean isClosestRaZombie(FallingSun sun) {
        List<RaZombie> raZombies = getWorld().getObjects(RaZombie.class);
        double myDist = Math.hypot(getX() - sun.getX(), getY() - sun.getY());
        for (RaZombie other : raZombies) {
            if (other == this || !other.isLiving()) continue;
            if (Math.hypot(other.getX() - sun.getX(), other.getY() - sun.getY()) < myDist) return false;
        }
        return true; 
    }

    private void resetStealing() {
        currentPhase = StealPhase.NONE;
        isStealing = false;
        sunActionTimer = 0;
        targetSun = null;
    }

    protected void deathAnim() {
        if (!resetAnim) { 
            setFrame(1); 
            resetAnim = true;
            if (targetSun != null) targetSun.setBeingStolen(false);
            for (int i = 0; i < sunStolenCount; i++) {
                int rx = getX() + Greenfoot.getRandomNumber(60) - 30;
                int ry = getY() + Greenfoot.getRandomNumber(60) - 30;
                getWorld().addObject(new FallingSun(), rx, ry);
            }
            sunStolenCount = 0;
        }
        animate(wDeath, 75, false);
        if (getCurrentFrame() >= wDeath.length) {
            removeFromRow();
            getWorld().removeObject(this);
        }
    }

    public boolean isArmless() {
        return false;
    }

    public GreenfootImage[] getCurrentAnimation(boolean isEating) {
        return isEating ? eNormal : wNormal;
    }

    private void updateAnimationReference() {
    }
}