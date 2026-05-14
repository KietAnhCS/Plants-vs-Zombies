import greenfoot.*;

public class SunZombie extends Zombie {
    public GreenfootImage[] wNormal, eNormal;
    private boolean sunSpawned = false;

    public SunZombie() {
        super(ZombieConfig.SUN);
        this.walkSpeed = (Greenfoot.getRandomNumber(6) + 25) / 100.0;
        
        wNormal = importSprites(ZombieAssets.SUN_WALK.path, ZombieAssets.SUN_WALK.count, 0.45);
        eNormal = importSprites(ZombieAssets.SUN_WALK.path, ZombieAssets.SUN_WALK.count, 0.45);
        
        setState(new WalkingState(this));
    }

    private void spawnSuns() {
        if (sunSpawned || getWorld() == null) return;
        sunSpawned = true;
    
        SecretSun s = new SecretSun(); 
        getWorld().addObject(s, getX(), getY());
        
        AudioManager.getInstance().playSound(80, false, "achievement.mp3");
    }

    @Override
    public void hit(int dmg) {
        if (getHp() <= 0 && !isLiving() && finalDeath) return;
        
        if (isLiving() || !finalDeath) {
            AudioManager.getInstance().playSound(80, false, "splat.mp3");
        }
        
        super.hit(dmg);
        
        if (getHp() <= 0 && !sunSpawned) {
            spawnSuns();
        }
    }

    @Override
    protected void handleThresholds() {}

    @Override
    public GreenfootImage[] getCurrentAnimation(boolean isEating) {
        return isEating ? eNormal : wNormal;
    }

    @Override
    protected void deathAnim() {
        if (getWorld() == null) return;

        if (!resetAnim) {
            frame = 0;
            resetAnim = true;
            removeFromRow();
            eventBus.publishDeath(this);
            target = null;
            eating = false;
        }

        if (finalDeath) {
            if (!fixAnim) {
                fixAnim = true;
                AudioManager.playSound(80, false, "zombie_falling_1.mp3", "zombie_falling_2.mp3");
                getWorld().addObject(new FallingZombie(fall), getX(), getY());
                getWorld().removeObject(this);
            }
        } else {
            if (!spawnHead) {
                spawnHead = true;
                AudioManager.getInstance().playSound(80, false, "limbs_pop.mp3");
                getWorld().addObject(new Head(), getX() + 10, getY() - 20);
            }

            boolean isAnimFinished;
            if (checkEating()) {
                isAnimFinished = animate(headlesseating, 350, false);
                playEating();
            } else {
                isAnimFinished = animate(headless, 350, false);
                walk();
            }

            if (isAnimFinished || frame >= headless.length - 1) {
                finalDeath = true;
                frame = 0; 
            }
        }
    }
}