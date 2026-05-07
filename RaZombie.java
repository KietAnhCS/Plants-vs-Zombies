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
    
    private FallingSun targetSun = null;

    private enum StealPhase { NONE, START, LOOP, END }
    private StealPhase currentPhase = StealPhase.NONE;

    public RaZombie() {
        super(ZombieConfig.RA); 
        this.walkSpeed = (Greenfoot.getRandomNumber(6) + 22) / 100.0;

        wNormal = importAndScale(SpriteKey.RA_WALK.path, 30);
        eNormal = importAndScale(SpriteKey.RA_EAT.path, 50); 
        wDeath  = importAndScale(SpriteKey.RA_DEATH.path, 30);

        sStart = importAndScale(SpriteKey.RA_POWER_START.path, 30);
        sLoop  = importAndScale(SpriteKey.RA_POWER_LOOP.path, 30);
        sEnd   = importAndScale(SpriteKey.RA_POWER_END.path, 38);

        this.currentState = new ArmoredZombieState(
            this,
            config.thresholds,
            new GreenfootImage[][] { wNormal },
            new GreenfootImage[][] { eNormal }
        );
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

        if (isLiving()) {
            handleSunStealingLogic();
            
            if (!isStealing) {
                if (currentState != null) {
                    currentState.update();
                    walk();
                    animate(currentState.getAnimation(), 80, true);
                }
            }
        } else {
            deathAnim();
        }
    }

    @Override
    protected void handleThresholds() { } 

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
                    frame = 0;
                }
            }
            return;
        }

        switch (currentPhase) {
            case START:
                animate(sStart, 60, false);
                if (frame >= sStart.length - 1) {
                    currentPhase = StealPhase.LOOP;
                    frame = 0;
                }
                break;
            case LOOP:
                animate(sLoop, 60, true);
                processStealing();
                stealDuration++;
                
                if (stealDuration >= 400) { 
                    currentPhase = StealPhase.END;
                    stealDuration = 0;
                    frame = 0;
                }
                break;
            case END:
                animate(sEnd, 60, false);
                if (frame >= sEnd.length - 1) {
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
            AudioManager.playSound(80, false, "sun_collect.mp3");
            
            currentPhase = StealPhase.END;
            stealDuration = 0;
            frame = 0;
        }
    }

    private void cancelStealing() {
        targetSun = null;
        isStealing = false;
        currentPhase = StealPhase.NONE;
        sunActionTimer = 0;
        stealDuration = 0;
    }

    private FallingSun getBestSunToSteal() {
        List<FallingSun> suns = getWorld().getObjects(FallingSun.class);
        FallingSun bestChoice = null;
        double minDistanceToMe = Double.MAX_VALUE;

        for (FallingSun sun : suns) {
            double distToMe = Math.hypot(getX() - sun.getX(), getY() - sun.getY());
            
            if (isClosestZombieTo(sun)) {
                if (distToMe < minDistanceToMe) {
                    minDistanceToMe = distToMe;
                    bestChoice = sun;
                }
            }
        }
        return bestChoice;
    }

    private boolean isClosestZombieTo(FallingSun sun) {
        List<RaZombie> allRaZombies = getWorld().getObjects(RaZombie.class);
        double myDist = Math.hypot(getX() - sun.getX(), getY() - sun.getY());
        
        for (RaZombie other : allRaZombies) {
            if (other == this || !other.isLiving()) continue;
            
            double otherDist = Math.hypot(other.getX() - sun.getX(), other.getY() - sun.getY());
            if (otherDist < myDist) {
                return false; 
            }
        }
        return true; 
    }

    private void resetStealing() {
        currentPhase = StealPhase.NONE;
        isStealing = false;
        sunActionTimer = 0;
        targetSun = null;
    }

    @Override
    public void deathAnim() {
        if (!resetAnim) { 
            frame = 0; 
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
        if (frame >= wDeath.length - 1) {
            finalDeath = true;
            removeFromRow();
            getWorld().removeObject(this);
        }
    }
}