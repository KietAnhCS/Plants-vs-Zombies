import greenfoot.*;

public class Buckethead extends Zombie {
    public GreenfootImage[] wNormal, wD1, wD2, wBare, wArmless;
    public GreenfootImage[] eNormal, eD1, eD2, eBare, eArmless;
    private boolean bucket = true;
    private boolean fallen = false;

    public Buckethead() {
        super(ZombieConfig.BUCKET);
        
        // Load Animations
        wNormal  = importSprites(ZombieAssets.BUCKET_WALK.path,           7);
        wD1      = importSprites(ZombieAssets.BUCKET_WALK_D1.path,        7);
        wD2      = importSprites(ZombieAssets.BUCKET_WALK_D2.path,        7);
        wBare    = importSprites(ZombieAssets.SHARED_WALK_BARE.path,     7);
        wArmless = importSprites(ZombieAssets.SHARED_WALK_ARMLESS.path,  7);
        
        eNormal  = importSprites(ZombieAssets.BUCKET_EAT.path,            7);
        eD1      = importSprites(ZombieAssets.BUCKET_EAT_D1.path,         7);
        eD2      = importSprites(ZombieAssets.BUCKET_EAT_D2.path,         7);
        eBare    = importSprites(ZombieAssets.SHARED_EAT_BARE.path,      7);
        eArmless = importSprites(ZombieAssets.SHARED_EAT_ARMLESS.path,   7);
        
        setState(new WalkingState(this));
    }

    @Override
    protected void handleThresholds() {
        int currentHp = getHp();
        
        // Rơi xô
        if (currentHp <= ZombieRegistry.BUCKET_BARE && bucket) {
            bucket = false;
            AudioManager.getInstance().playSound(80, false, "limbs_pop.mp3");
            if (getWorld() != null) getWorld().addObject(new Bucket(), getX(), getY() - 25);
        }
        
        // Rơi tay
        if (currentHp <= ZombieRegistry.BUCKET_ARMLESS && !fallen) {
            fallen = true;
            AudioManager.getInstance().playSound(80, false, "limbs_pop.mp3");
            if (getWorld() != null) getWorld().addObject(new Arm(), getX() + 8, getY() + 20);
        }
    }

    @Override
    public void hit(int dmg) {
        if (!isLiving()) return;

        // Âm thanh kim loại nếu còn xô, âm thanh thịt nếu mất xô
        AudioManager.getInstance().playSound(80, false, bucket ? "shieldhit.mp3" : "splat.mp3");

        String path;
        int currentHp = getHp();
        boolean isEating = checkEating();

        // Xác định đường dẫn Flash (chỉ truyền String cho SpriteAnimator)
        if (currentHp > ZombieRegistry.BUCKET_D1) {
            path = isEating ? ZombieAssets.BUCKET_EAT.path : ZombieAssets.BUCKET_WALK.path;
        } else if (currentHp > ZombieRegistry.BUCKET_D2) {
            path = isEating ? ZombieAssets.BUCKET_EAT_D1.path : ZombieAssets.BUCKET_WALK_D1.path;
        } else if (currentHp > ZombieRegistry.BUCKET_BARE) {
            path = isEating ? ZombieAssets.BUCKET_EAT_D2.path : ZombieAssets.BUCKET_WALK_D2.path;
        } else if (!fallen) {
            path = isEating ? ZombieAssets.SHARED_EAT_BARE.path : ZombieAssets.SHARED_WALK_BARE.path;
        } else {
            path = isEating ? ZombieAssets.SHARED_EAT_ARMLESS.path : ZombieAssets.SHARED_WALK_ARMLESS.path;
        }

        hitFlash(path);
        super.hit(dmg);
    }

    public boolean isArmless() {
        return this.fallen;
    }

    public GreenfootImage[] getCurrentAnimation(boolean isEating) {
        int currentHp = getHp();
        if (currentHp > ZombieRegistry.BUCKET_D1) return isEating ? eNormal : wNormal;
        if (currentHp > ZombieRegistry.BUCKET_D2) return isEating ? eD1 : wD1;
        if (currentHp > ZombieRegistry.BUCKET_BARE) return isEating ? eD2 : wD2;
        if (!fallen) return isEating ? eBare : wBare;
        return isEating ? eArmless : wArmless;
    }
}