import greenfoot.*;

public class BasicZombie extends Zombie {
    public GreenfootImage[] wNormal, wArmless;
    public GreenfootImage[] eNormal, eArmless;

    public BasicZombie() {
        super(ZombieConfig.BASIC);
        this.walkSpeed = (Greenfoot.getRandomNumber(6) + 22) / 100.0;
        wNormal  = importSprites(SpriteKey.BASIC_WALK.path,         7);
        wArmless = importSprites(SpriteKey.BASIC_WALK_ARMLESS.path, 7);
        eNormal  = importSprites(SpriteKey.BASIC_EAT.path,          7);
        eArmless = importSprites(SpriteKey.BASIC_EAT_ARMLESS.path,  7);
        this.currentState = new BasicZombieState(this);
    }

    @Override
    protected void handleThresholds() {
        if (hp <= ZombieRegistry.BASIC_ARMLESS && !fallen) {
            fallen = true;
            AudioManager.playSound(80, false, "limbs_pop.mp3");
            if (getWorld() != null) getWorld().addObject(new Arm(), getX() + 8, getY() + 20);
        }
    }

    @Override
    public void hit(int dmg) {
        if (!isAlive) return;
        AudioManager.playSound(80, false, "splat.mp3", "splat2.mp3");
        if (isLiving()) {
            if (!fallen) hitFlash(eating ? eNormal  : wNormal,
                                  eating ? SpriteKey.BASIC_EAT.path        : SpriteKey.BASIC_WALK.path);
            else         hitFlash(eating ? eArmless : wArmless,
                                  eating ? SpriteKey.BASIC_EAT_ARMLESS.path : SpriteKey.BASIC_WALK_ARMLESS.path);
        } else if (!finalDeath) {
            hitFlash(eating ? headlesseating : headless,
                     eating ? SpriteKey.SHARED_HEADLESS_EAT.path : SpriteKey.SHARED_HEADLESS.path);
        }
        super.hit(dmg);
    }
}