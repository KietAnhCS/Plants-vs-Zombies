import greenfoot.*;

public class ExcavatorZombie extends Zombie {
    private GreenfootImage[] wNormal, shovel, chop, death;
    private long lastShovelTime = 0;

    public ExcavatorZombie() {
        super(new ZombieConfig(
            ZombieRegistry.EXCAVATOR_HP, ZombieRegistry.EXCAVATOR_DAMAGE, 
            ZombieRegistry.EXCAVATOR_SPEED, "Excavator",
            new int[]{}, null, null, null, 0
        ));
        
        wNormal = importSprites(ZombieAssets.EXCAVATOR_WALK.path, 20, 0.45);
        shovel  = importSprites(ZombieAssets.EXCAVATOR_SHOVEL.path, 25);
        chop    = importSprites(ZombieAssets.EXCAVATOR_CHOP.path, 20, 0.45);
        death   = importSprites(ZombieAssets.EXCAVATOR_DEATH.path, 20, 0.45);

        setState(new WalkingState(this));
    }

    @Override
    public void act() {
        if (getWorld() == null) return;
        if (!getWorld().getObjects(Overlay.class).isEmpty()) return;

        if (isLiving()) {
            if (currentState instanceof WalkingState) {
                if (checkEating()) {
                    long now = System.currentTimeMillis();
                    if (now - lastShovelTime > ZombieRegistry.EXCAVATOR_COOLDOWN) {
                        setState(new ExcavatorShovelState(this));
                        lastShovelTime = now;
                        return;
                    } else {
                        setState(new EatingState(this));
                    }
                }
            }
            updateLogic();
            handleThresholds();
        } else {
            deathAnim();
        }
    }

    @Override
    public GreenfootImage[] getCurrentAnimation(boolean isEating) {
        if (currentState instanceof ExcavatorShovelState) return shovel;
        return isEating ? chop : wNormal;
    }
    
    public GreenfootImage[] getShovelSprites() { 
        return shovel; 
    }
    
    @Override
    protected void handleThresholds() {}

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