import greenfoot.*;

public class Conehead extends Zombie {
    public GreenfootImage[] wNormal, wD1, wD2, wBare, wArmless;
    public GreenfootImage[] eNormal, eD1, eD2, eBare, eArmless;
    private boolean cone = true;
    private boolean fallen = false;

    public Conehead() {
        super(ZombieConfig.CONE);
        this.walkSpeed = (Greenfoot.getRandomNumber(6) + 25) / 100.0;

        wNormal  = importSprites(ZombieAssets.CONE_WALK.path,         ZombieAssets.CONE_WALK.count);
        wD1      = importSprites(ZombieAssets.CONE_WALK_D1.path,      ZombieAssets.CONE_WALK_D1.count);
        wD2      = importSprites(ZombieAssets.CONE_WALK_D2.path,      ZombieAssets.CONE_WALK_D2.count);
        wBare    = importSprites(ZombieAssets.SHARED_WALK_BARE.path,    ZombieAssets.SHARED_WALK_BARE.count);
        wArmless = importSprites(ZombieAssets.SHARED_WALK_ARMLESS.path, ZombieAssets.SHARED_WALK_ARMLESS.count);

        eNormal  = importSprites(ZombieAssets.CONE_EAT.path,          ZombieAssets.CONE_EAT.count);
        eD1      = importSprites(ZombieAssets.CONE_EAT_D1.path,       ZombieAssets.CONE_EAT_D1.count);
        eD2      = importSprites(ZombieAssets.CONE_EAT_D2.path,       ZombieAssets.CONE_EAT_D2.count);
        eBare    = importSprites(ZombieAssets.SHARED_EAT_BARE.path,    ZombieAssets.SHARED_EAT_BARE.count);
        eArmless = importSprites(ZombieAssets.SHARED_EAT_ARMLESS.path, ZombieAssets.SHARED_EAT_ARMLESS.count);

        setState(new WalkingState(this));
    }

    @Override
    protected void handleThresholds() {
        int currentHp = getHp();
        if (currentHp <= ZombieRegistry.CONE_BARE && cone) {
            cone = false;
            AudioManager.getInstance().playSound(80, false, "limbs_pop.mp3");
            if (getWorld() != null) getWorld().addObject(new Cone(), getX(), getY() - 25);
        }
        if (currentHp <= ZombieRegistry.CONE_ARMLESS && !fallen) {
            fallen = true;
            AudioManager.getInstance().playSound(80, false, "limbs_pop.mp3");
            if (getWorld() != null) getWorld().addObject(new Arm(), getX() + 8, getY() + 20);
        }
    }

    @Override
    public void hit(int dmg) {
        if (getHp() <= 0 && !isLiving() && finalDeath) return;

        if (isLiving()) {
            AudioManager.getInstance().playSound(80, false, cone ? "plastichit.mp3" : "splat.mp3");
            
            ZombieAssets asset;
            int currentHp = getHp();

            if (currentHp > ZombieRegistry.CONE_D1) {
                asset = eating ? ZombieAssets.CONE_EAT : ZombieAssets.CONE_WALK;
            } else if (currentHp > ZombieRegistry.CONE_D2) {
                asset = eating ? ZombieAssets.CONE_EAT_D1 : ZombieAssets.CONE_WALK_D1;
            } else if (currentHp > ZombieRegistry.CONE_BARE) {
                asset = eating ? ZombieAssets.CONE_EAT_D2 : ZombieAssets.CONE_WALK_D2;
            } else if (!fallen) {
                asset = eating ? ZombieAssets.SHARED_EAT_BARE : ZombieAssets.SHARED_WALK_BARE;
            } else {
                asset = eating ? ZombieAssets.SHARED_EAT_ARMLESS : ZombieAssets.SHARED_WALK_ARMLESS;
            }

            hitFlash(asset.path);
        } else if (!finalDeath) {
            AudioManager.getInstance().playSound(80, false, "splat.mp3");
            hitFlash((eating ? ZombieAssets.SHARED_HEADLESS_EAT : ZombieAssets.SHARED_HEADLESS).path);
        }
        super.hit(dmg);
    }

    public GreenfootImage[] getCurrentAnimation(boolean isEating) {
        int currentHp = getHp();
        if (currentHp > ZombieRegistry.CONE_D1) return isEating ? eNormal : wNormal;
        if (currentHp > ZombieRegistry.CONE_D2) return isEating ? eD1 : wD1;
        if (currentHp > ZombieRegistry.CONE_BARE) return isEating ? eD2 : wD2;
        if (!fallen) return isEating ? eBare : wBare;
        return isEating ? eArmless : wArmless;
    }
}