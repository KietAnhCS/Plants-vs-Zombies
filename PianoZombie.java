import greenfoot.*;

public class PianoZombie extends Zombie {
    public GreenfootImage[] wNormal, wArmless;
    public GreenfootImage[] eNormal, eArmless;
    private boolean isArmless = false;

    public PianoZombie() {
        super(ZombieConfig.PIANO);

        // Load Animations
        wNormal  = importSprites(ZombieAssets.PIANO_WALK.path,            30);
        wArmless = importSprites(ZombieAssets.SHARED_WALK_ARMLESS.path,   7);
        eNormal  = importSprites(ZombieAssets.SHARED_EAT_BARE.path,       7);
        eArmless = importSprites(ZombieAssets.SHARED_EAT_ARMLESS.path,    7);

        // Khởi tạo trạng thái đi bộ
        setState(new WalkingState(this));
    }

    @Override
    protected void handleThresholds() {
        int currentHp = getHp();
        
        // Ngưỡng rơi tay/hỏng piano
        if (currentHp <= ZombieRegistry.PIANO_ARMLESS && !isArmless) {
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
        boolean isEating = checkEating();

        // Logic xác định path để hitFlash (khớp với SpriteAnimator yêu cầu String)
        if (!isArmless) {
            currentPath = isEating ? ZombieAssets.SHARED_EAT_BARE.path : ZombieAssets.PIANO_WALK.path;
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
        if (!isArmless) {
            return isEating ? eNormal : wNormal;
        }
        return isEating ? eArmless : wArmless;
    }

    private void updateAnimationReference() {
    }
}