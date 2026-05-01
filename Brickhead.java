import greenfoot.*;

public class Brickhead extends Zombie {
    public GreenfootImage[] wNormal, wD1, wD2, wBare, wArmless;
    public GreenfootImage[] eNormal, eD1, eD2, eBare, eArmless;
    private boolean brick = true;

    public Brickhead() {
        super(ZombieConfig.BRICK);
        this.walkSpeed = (Greenfoot.getRandomNumber(6) + 22) / 100.0;
        wNormal  = importSprites(SpriteKey.BRICK_WALK.path,           7);
        wD1      = importSprites(SpriteKey.BRICK_WALK_D1.path,        7);
        wD2      = importSprites(SpriteKey.BRICK_WALK_D2.path,        7);
        wBare    = importSprites(SpriteKey.SHARED_WALK_BARE.path,     7);
        wArmless = importSprites(SpriteKey.SHARED_WALK_ARMLESS.path,  7);
        eNormal  = importSprites(SpriteKey.BRICK_EAT.path,            7);
        eD1      = importSprites(SpriteKey.BRICK_EAT_D1.path,         7);
        eD2      = importSprites(SpriteKey.BRICK_EAT_D2.path,         7);
        eBare    = importSprites(SpriteKey.SHARED_EAT_BARE.path,      7);
        eArmless = importSprites(SpriteKey.SHARED_EAT_ARMLESS.path,   7);
        this.currentState = new ArmoredZombieState(
            this,
            config.thresholds,
            new GreenfootImage[][] { wNormal, wD1, wD2, wBare, wArmless },
            new GreenfootImage[][] { eNormal, eD1, eD2, eBare, eArmless }
        );
    }

    @Override
    protected void handleThresholds() {
        if (hp <= ZombieRegistry.BRICK_BARE && brick) {
            brick = false;
            AudioManager.playSound(80, false, "limbs_pop.mp3");
            if (getWorld() != null) getWorld().addObject(new Brick(), getX(), getY() - 25);
        }
        if (hp <= ZombieRegistry.BRICK_ARMLESS && !fallen) {
            fallen = true;
            AudioManager.playSound(80, false, "limbs_pop.mp3");
            if (getWorld() != null) getWorld().addObject(new Arm(), getX() + 8, getY() + 20);
        }
    }

    @Override
    public void hit(int dmg) {
        if (!isAlive) return;
        AudioManager.playSound(80, false,
            brick ? "shieldhit.mp3"  : "splat.mp3",
            brick ? "shieldhit2.mp3" : "splat2.mp3");
        if (isLiving()) {
            if      (hp > ZombieRegistry.BRICK_D1)    hitFlash(eating ? eNormal  : wNormal,
                                                               eating ? SpriteKey.BRICK_EAT.path         : SpriteKey.BRICK_WALK.path);
            else if (hp > ZombieRegistry.BRICK_D2)    hitFlash(eating ? eD1      : wD1,
                                                               eating ? SpriteKey.BRICK_EAT_D1.path      : SpriteKey.BRICK_WALK_D1.path);
            else if (hp > ZombieRegistry.BRICK_BARE)  hitFlash(eating ? eD2      : wD2,
                                                               eating ? SpriteKey.BRICK_EAT_D2.path      : SpriteKey.BRICK_WALK_D2.path);
            else if (!fallen)                         hitFlash(eating ? eBare    : wBare,
                                                               eating ? SpriteKey.SHARED_EAT_BARE.path   : SpriteKey.SHARED_WALK_BARE.path);
            else                                      hitFlash(eating ? eArmless : wArmless,
                                                               eating ? SpriteKey.SHARED_EAT_ARMLESS.path : SpriteKey.SHARED_WALK_ARMLESS.path);
        } else if (!finalDeath) {
            hitFlash(eating ? headlesseating : headless,
                     eating ? SpriteKey.SHARED_HEADLESS_EAT.path : SpriteKey.SHARED_HEADLESS.path);
        }
        super.hit(dmg);
    }
}