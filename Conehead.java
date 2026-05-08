import greenfoot.*;

public class Conehead extends Zombie {
    public GreenfootImage[] wNormal, wD1, wD2, wBare, wArmless;
    public GreenfootImage[] eNormal, eD1, eD2, eBare, eArmless;
    private boolean cone = true;
    private boolean isArmless = false;

    public Conehead() {
        super(ZombieConfig.CONE);
        
        wNormal  = importSprites(ZombieAssets.CONE_WALK.path,           7);
        wD1      = importSprites(ZombieAssets.CONE_WALK_D1.path,        7);
        wD2      = importSprites(ZombieAssets.CONE_WALK_D2.path,        7);
        wBare    = importSprites(ZombieAssets.SHARED_WALK_BARE.path,     7);
        wArmless = importSprites(ZombieAssets.SHARED_WALK_ARMLESS.path,  7);
        
        eNormal  = importSprites(ZombieAssets.CONE_EAT.path,            7);
        eD1      = importSprites(ZombieAssets.CONE_EAT_D1.path,         7);
        eD2      = importSprites(ZombieAssets.CONE_EAT_D2.path,         7);
        eBare    = importSprites(ZombieAssets.SHARED_EAT_BARE.path,      7);
        eArmless = importSprites(ZombieAssets.SHARED_EAT_ARMLESS.path,   7);
        
        setState(new WalkingState(this));
    }

    @Override
    protected void handleThresholds() {
        int currentHp = getHp();
        
        // Ngưỡng rơi nón
        if (currentHp <= ZombieRegistry.CONE_BARE && cone) {
            cone = false;
            AudioManager.getInstance().playSound(80, false, "limbs_pop.mp3");
            if (getWorld() != null) getWorld().addObject(new Cone(), getX(), getY() - 25);
        }
        
        // Ngưỡng rơi tay
        if (currentHp <= ZombieRegistry.CONE_ARMLESS && !isArmless) {
            isArmless = true;
            AudioManager.getInstance().playSound(80, false, "limbs_pop.mp3");
            if (getWorld() != null) getWorld().addObject(new Arm(), getX() + 8, getY() + 20);
        }
    }

    @Override
    public void hit(int dmg) {
        if (!isLiving()) return;

        // Âm thanh nhựa khi còn nón, âm thanh thịt khi mất nón
        AudioManager.getInstance().playSound(80, false, cone ? "plastichit.mp3" : "splat.mp3");

        String currentPath;
        int currentHp = getHp();
        boolean isEating = checkEating();

        // Logic xác định path để hitFlash (giống BasicZombie nhưng nhiều tầng hơn)
        if (currentHp > ZombieRegistry.CONE_D1) {
            currentPath = isEating ? ZombieAssets.CONE_EAT.path : ZombieAssets.CONE_WALK.path;
        } else if (currentHp > ZombieRegistry.CONE_D2) {
            currentPath = isEating ? ZombieAssets.CONE_EAT_D1.path : ZombieAssets.CONE_WALK_D1.path;
        } else if (currentHp > ZombieRegistry.CONE_BARE) {
            currentPath = isEating ? ZombieAssets.CONE_EAT_D2.path : ZombieAssets.CONE_WALK_D2.path;
        } else if (!isArmless) {
            currentPath = isEating ? ZombieAssets.SHARED_EAT_BARE.path : ZombieAssets.SHARED_WALK_BARE.path;
        } else {
            currentPath = isEating ? ZombieAssets.SHARED_EAT_ARMLESS.path : ZombieAssets.SHARED_WALK_ARMLESS.path;
        }

        hitFlash(currentPath);
        super.hit(dmg);
    }

    public boolean isArmless() {
        return this.isArmless;
    }

    public GreenfootImage[] getCurrentAnimation(boolean isEating) {
        int currentHp = getHp();
        if (currentHp > ZombieRegistry.CONE_D1) return isEating ? eNormal : wNormal;
        if (currentHp > ZombieRegistry.CONE_D2) return isEating ? eD1 : wD1;
        if (currentHp > ZombieRegistry.CONE_BARE) return isEating ? eD2 : wD2;
        if (!isArmless) return isEating ? eBare : wBare;
        return isEating ? eArmless : wArmless;
    }
    
    private void updateAnimationReference() {
    }
}