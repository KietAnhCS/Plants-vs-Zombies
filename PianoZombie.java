import greenfoot.*;
public class PianoZombie extends Zombie {
    public GreenfootImage[] wNormal, wArmless;
    public GreenfootImage[] eNormal, eArmless;
    private boolean piano = true;

    public PianoZombie() {
        super(ZombieConfig.PIANO);
        this.walkSpeed = (Greenfoot.getRandomNumber(6) + 22) / 100.0;

        wNormal  = importSprites(SpriteKey.PIANO_WALK.path,           30);
        wArmless = importSprites(SpriteKey.SHARED_WALK_ARMLESS.path,   7);
        eNormal  = importSprites(SpriteKey.SHARED_EAT_BARE.path,       7);
        eArmless = importSprites(SpriteKey.SHARED_EAT_ARMLESS.path,    7);

        this.currentState = new ArmoredZombieState(
            this,
            config.thresholds,
            new GreenfootImage[][] { wNormal, wArmless },
            new GreenfootImage[][] { eNormal, eArmless }
        );
    }

    @Override
    protected void handleThresholds() {
        if (hp <= ZombieRegistry.PIANO_ARMLESS && !fallen) {
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
                                  eating ? SpriteKey.SHARED_EAT_BARE.path    : SpriteKey.PIANO_WALK.path);
            else         hitFlash(eating ? eArmless : wArmless,
                                  eating ? SpriteKey.SHARED_EAT_ARMLESS.path : SpriteKey.SHARED_WALK_ARMLESS.path);
        } else if (!finalDeath) {
            hitFlash(eating ? headlesseating : headless,
                     eating ? SpriteKey.SHARED_HEADLESS_EAT.path : SpriteKey.SHARED_HEADLESS.path);
        }
        super.hit(dmg);
    }
}