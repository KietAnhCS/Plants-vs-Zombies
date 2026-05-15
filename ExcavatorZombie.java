import greenfoot.*;

public class ExcavatorZombie extends Zombie {
    private long shovelEndTime = 0;
    private boolean isWaitingAfterShovel = false;
    private long lastShovelTime = 0;
    private GreenfootImage[] wNormal, shovel, chop, death;

    public ExcavatorZombie() {
        super(new ZombieConfig(
            ZombieRegistry.EXCAVATOR_HP, ZombieRegistry.EXCAVATOR_DAMAGE,
            ZombieRegistry.EXCAVATOR_SPEED, "Excavator",
            new int[]{}, null, null, null, 0
        ));
        wNormal = importSprites(ZombieAssets.EXCAVATOR_WALK.path,   20, 0.45);
        shovel  = importSprites(ZombieAssets.EXCAVATOR_SHOVEL.path, 25);
        chop    = importSprites(ZombieAssets.EXCAVATOR_CHOP.path,   20, 0.45);
        death   = importSprites(ZombieAssets.EXCAVATOR_DEATH.path,  20, 0.45);
        setState(new ExcavatorWalkingState(this));
    }

    public boolean isWaiting() {
        return isWaitingAfterShovel;
    }

    public boolean canShovel() {
        return System.currentTimeMillis() - lastShovelTime > ZombieRegistry.EXCAVATOR_COOLDOWN;
    }

    public void startShovel() {
        setState(new ExcavatorShovelState(this));
        frame = 0;
    }

    public void onShovelFinished() {
        shovelEndTime = System.currentTimeMillis();
        lastShovelTime = shovelEndTime;
        isWaitingAfterShovel = true;
        frame = 0;
        setState(new ExcavatorWalkingState(this));
    }

    @Override
    public void update() {
        if (getWorld() == null) return;
        if (!getWorld().getObjects(Overlay.class).isEmpty()) return;
        if (isLiving()) {
            handleSliding();
            if (isWaitingAfterShovel && System.currentTimeMillis() - shovelEndTime >= 3000) {
                isWaitingAfterShovel = false;
            }
            updateLogic();
            handleThresholds();
        } else {
            deathAnim();
        }
    }

    public void walk() {
        if (!isWaitingAfterShovel && !(currentState instanceof ExcavatorShovelState)
                && getWorld() != null && !movingY) {
            move(-walkSpeed);
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
        if (!fixAnim) {
            fixAnim = true;
            AudioManager.playSound(80, false, "zombie_falling_1.mp3", "zombie_falling_2.mp3");
            if (getWorld() != null) {
                getWorld().addObject(new Head(), getX(), getY());
                getWorld().addObject(new FallingZombie(fall), getX(), getY());
                getWorld().removeObject(this);
            }
        }
    }
}