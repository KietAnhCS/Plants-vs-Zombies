import greenfoot.*;

public class Brickhead extends Zombie {
    public GreenfootImage[] wNormal, wD1, wD2, wBare, wArmless;
    public GreenfootImage[] eNormal, eD1, eD2, eBare, eArmless;
    private boolean brick = true;
    private boolean fallen = false;

    public Brickhead() {
        super(ZombieConfig.BRICK);
        
        // Load Animations
        wNormal  = importSprites(ZombieAssets.BRICK_WALK.path,           7);
        wD1      = importSprites(ZombieAssets.BRICK_WALK_D1.path,        7);
        wD2      = importSprites(ZombieAssets.BRICK_WALK_D2.path,        7);
        wBare    = importSprites(ZombieAssets.SHARED_WALK_BARE.path,     7);
        wArmless = importSprites(ZombieAssets.SHARED_WALK_ARMLESS.path,  7);
        
        eNormal  = importSprites(ZombieAssets.BRICK_EAT.path,            7);
        eD1      = importSprites(ZombieAssets.BRICK_EAT_D1.path,         7);
        eD2      = importSprites(ZombieAssets.BRICK_EAT_D2.path,         7);
        eBare    = importSprites(ZombieAssets.SHARED_EAT_BARE.path,      7);
        eArmless = importSprites(ZombieAssets.SHARED_EAT_ARMLESS.path,   7);
        
        // Khởi tạo trạng thái ban đầu
        setState(new WalkingState(this));
    }

    @Override
    protected void handleThresholds() {
        int currentHp = getHp();
        
        // Rơi nón gạch
        if (currentHp <= ZombieRegistry.BRICK_BARE && brick) {
            brick = false;
            AudioManager.getInstance().playSound(80, false, "limbs_pop.mp3");
            if (getWorld() != null) getWorld().addObject(new Brick(), getX(), getY() - 25);
        }
        
        // Rơi tay
        if (currentHp <= ZombieRegistry.BRICK_ARMLESS && !fallen) {
            fallen = true;
            AudioManager.getInstance().playSound(80, false, "limbs_pop.mp3");
            if (getWorld() != null) getWorld().addObject(new Arm(), getX() + 8, getY() + 20);
        }
    }

    @Override
    public void hit(int dmg) {
        if (!isLiving()) return;

        // Âm thanh tương ứng (nếu còn gạch thì kêu tiếng kim loại/vật cứng)
        AudioManager.getInstance().playSound(80, false, brick ? "shieldhit.mp3" : "splat.mp3");

        String path;
        int currentHp = getHp();
        boolean isEating = checkEating();

        // Xác định đường dẫn Flash dựa trên các ngưỡng máu
        if (currentHp > ZombieRegistry.BRICK_D1) {
            path = isEating ? ZombieAssets.BRICK_EAT.path : ZombieAssets.BRICK_WALK.path;
        } else if (currentHp > ZombieRegistry.BRICK_D2) {
            path = isEating ? ZombieAssets.BRICK_EAT_D1.path : ZombieAssets.BRICK_WALK_D1.path;
        } else if (currentHp > ZombieRegistry.BRICK_BARE) {
            path = isEating ? ZombieAssets.BRICK_EAT_D2.path : ZombieAssets.BRICK_WALK_D2.path;
        } else if (!fallen) {
            path = isEating ? ZombieAssets.SHARED_EAT_BARE.path : ZombieAssets.SHARED_WALK_BARE.path;
        } else {
            path = isEating ? ZombieAssets.SHARED_EAT_ARMLESS.path : ZombieAssets.SHARED_WALK_ARMLESS.path;
        }

        hitFlash(path);
        super.hit(dmg);
    }

    public GreenfootImage[] getCurrentAnimation(boolean isEating) {
        int currentHp = getHp();
        if (currentHp > ZombieRegistry.BRICK_D1) return isEating ? eNormal : wNormal;
        if (currentHp > ZombieRegistry.BRICK_D2) return isEating ? eD1 : wD1;
        if (currentHp > ZombieRegistry.BRICK_BARE) return isEating ? eD2 : wD2;
        if (!fallen) return isEating ? eBare : wBare;
        return isEating ? eArmless : wArmless;
    }
}