import greenfoot.*;

public class BasicZombie extends Zombie {
    public GreenfootImage[] wNormal, wArmless;
    public GreenfootImage[] eNormal, eArmless;
    private boolean isArmless = false;

    public BasicZombie() {
        super(ZombieConfig.BASIC);
        this.walkSpeed = (Greenfoot.getRandomNumber(6) + 25) / 100.0;

        wNormal  = importSprites(ZombieAssets.BASIC_WALK.path,         ZombieAssets.BASIC_WALK.count);
        wArmless = importSprites(ZombieAssets.BASIC_WALK_ARMLESS.path, ZombieAssets.BASIC_WALK_ARMLESS.count);
        eNormal  = importSprites(ZombieAssets.BASIC_EAT.path,          ZombieAssets.BASIC_EAT.count);
        eArmless = importSprites(ZombieAssets.BASIC_EAT_ARMLESS.path,  ZombieAssets.BASIC_EAT_ARMLESS.count);

        setState(new WalkingState(this));
    }

    @Override
    public void update() {
        if (getWorld() == null) return;
        if (!getWorld().getObjects(Overlay.class).isEmpty()) return;
        if (isLiving()) {
            updateLogic();
            handleThresholds();
        } else {
            deathAnim();
        }
    }

    @Override
    protected void handleThresholds() {
        if (getHp() <= ZombieRegistry.BASIC_ARMLESS && !isArmless) {
            isArmless = true;
            AudioManager.getInstance().playSound(80, false, "limbs_pop.mp3");
            if (getWorld() != null) {
                getWorld().addObject(new Arm(), getX() + 8, getY() + 20);
            }
        }
    }

    @Override
    public void hit(int dmg) {
        if (getHp() <= 0 && !isLiving() && finalDeath) return;
        if (isLiving()) {
            AudioManager.getInstance().playSound(80, false, "splat.mp3");
            ZombieAssets asset = !isArmless
                ? (eating ? ZombieAssets.BASIC_EAT : ZombieAssets.BASIC_WALK)
                : (eating ? ZombieAssets.BASIC_EAT_ARMLESS : ZombieAssets.BASIC_WALK_ARMLESS);
            hitFlash(asset.path);
        } else if (!finalDeath) {
            AudioManager.getInstance().playSound(80, false, "splat.mp3");
        }
        super.hit(dmg);
    }

    @Override
    public GreenfootImage[] getCurrentAnimation(boolean isEating) {
        return isEating
            ? (isArmless ? eArmless : eNormal)
            : (isArmless ? wArmless : wNormal);
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

    public boolean isArmless() {
        return this.isArmless;
    }
}