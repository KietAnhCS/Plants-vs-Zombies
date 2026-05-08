import greenfoot.*;

public class BasicZombie extends Zombie {
    public GreenfootImage[] wNormal, wArmless;
    public GreenfootImage[] eNormal, eArmless;
    private boolean isArmless = false;

    public BasicZombie() {
        super(ZombieConfig.BASIC);
        wNormal  = importSprites(ZombieAssets.BASIC_WALK.path, 7);
        wArmless = importSprites(ZombieAssets.BASIC_WALK_ARMLESS.path, 7);
        eNormal  = importSprites(ZombieAssets.BASIC_EAT.path, 7);
        eArmless = importSprites(ZombieAssets.BASIC_EAT_ARMLESS.path, 7);
        
        setState(new WalkingState(this)); 
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
        if (!isLiving()) return;

        AudioManager.getInstance().playSound(80, false, "splat.mp3");

        String currentPath;
        if (!isArmless) {
            currentPath = checkEating() ? ZombieAssets.BASIC_EAT.path : ZombieAssets.BASIC_WALK.path;
        } else {
            currentPath = checkEating() ? ZombieAssets.BASIC_EAT_ARMLESS.path : ZombieAssets.BASIC_WALK_ARMLESS.path;
        }
        
        hitFlash(currentPath);
        super.hit(dmg);
    }

    public boolean isArmless() {
        return this.isArmless;
    }

    private void updateAnimationReference() {
    }
}