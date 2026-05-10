import greenfoot.*;
public class Buckethead extends Zombie {
    public GreenfootImage[] wNormal, wD1, wD2, wBare, wArmless;
    public GreenfootImage[] eNormal, eD1, eD2, eBare, eArmless;
    private boolean bucket = true;
    private boolean fallen = false;

    public Buckethead() {
        super(ZombieConfig.BUCKET);

        wNormal  = importSprites(ZombieAssets.BUCKET_WALK.path,          ZombieAssets.BUCKET_WALK.count);
        wD1      = importSprites(ZombieAssets.BUCKET_WALK_D1.path,       ZombieAssets.BUCKET_WALK_D1.count);
        wD2      = importSprites(ZombieAssets.BUCKET_WALK_D2.path,       ZombieAssets.BUCKET_WALK_D2.count);
        wBare    = importSprites(ZombieAssets.SHARED_WALK_BARE.path,     ZombieAssets.SHARED_WALK_BARE.count);
        wArmless = importSprites(ZombieAssets.SHARED_WALK_ARMLESS.path,  ZombieAssets.SHARED_WALK_ARMLESS.count);

        eNormal  = importSprites(ZombieAssets.BUCKET_EAT.path,           ZombieAssets.BUCKET_EAT.count);
        eD1      = importSprites(ZombieAssets.BUCKET_EAT_D1.path,        ZombieAssets.BUCKET_EAT_D1.count);
        eD2      = importSprites(ZombieAssets.BUCKET_EAT_D2.path,        ZombieAssets.BUCKET_EAT_D2.count);
        eBare    = importSprites(ZombieAssets.SHARED_EAT_BARE.path,      ZombieAssets.SHARED_EAT_BARE.count);
        eArmless = importSprites(ZombieAssets.SHARED_EAT_ARMLESS.path,   ZombieAssets.SHARED_EAT_ARMLESS.count);

        setState(new WalkingState(this));
    }

    @Override
    protected void handleThresholds() {
        int currentHp = getHp();
        if (currentHp <= ZombieRegistry.BUCKET_BARE && bucket) {
            bucket = false;
            AudioManager.getInstance().playSound(80, false, "limbs_pop.mp3");
            if (getWorld() != null) getWorld().addObject(new Bucket(), getX(), getY() - 25);
        }
        if (currentHp <= ZombieRegistry.BUCKET_ARMLESS && !fallen) {
            fallen = true;
            AudioManager.getInstance().playSound(80, false, "limbs_pop.mp3");
            if (getWorld() != null) getWorld().addObject(new Arm(), getX() + 8, getY() + 20);
        }
    }

    @Override
    public void hit(int dmg) {
        if (!isLiving()) return;
        AudioManager.getInstance().playSound(80, false, bucket ? "shieldhit.mp3" : "splat.mp3");
        int currentHp = getHp();
        String path;
        if (currentHp > ZombieRegistry.BUCKET_D1) {
            path = (eating ? ZombieAssets.BUCKET_EAT : ZombieAssets.BUCKET_WALK).path;
        } else if (currentHp > ZombieRegistry.BUCKET_D2) {
            path = (eating ? ZombieAssets.BUCKET_EAT_D1 : ZombieAssets.BUCKET_WALK_D1).path;
        } else if (currentHp > ZombieRegistry.BUCKET_BARE) {
            path = (eating ? ZombieAssets.BUCKET_EAT_D2 : ZombieAssets.BUCKET_WALK_D2).path;
        } else if (!fallen) {
            path = (eating ? ZombieAssets.SHARED_EAT_BARE : ZombieAssets.SHARED_WALK_BARE).path;
        } else {
            path = (eating ? ZombieAssets.SHARED_EAT_ARMLESS : ZombieAssets.SHARED_WALK_ARMLESS).path;
        }
        hitFlash(path);
        super.hit(dmg);
    }

    @Override
    public GreenfootImage[] getCurrentAnimation(boolean isEating) {
        int currentHp = getHp();
        if (currentHp > ZombieRegistry.BUCKET_D1) return isEating ? eNormal : wNormal;
        if (currentHp > ZombieRegistry.BUCKET_D2) return isEating ? eD1 : wD1;
        if (currentHp > ZombieRegistry.BUCKET_BARE) return isEating ? eD2 : wD2;
        if (!fallen) return isEating ? eBare : wBare;
        return isEating ? eArmless : wArmless;
    }

    public boolean isArmless() {
        return this.fallen;
    }
}