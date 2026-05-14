import greenfoot.*;

public class Brickhead extends Zombie {
    public GreenfootImage[] wNormal, wD1, wD2, wBare, wArmless;
    public GreenfootImage[] eNormal, eD1, eD2, eBare, eArmless;
    private boolean brick = true;
    private boolean fallen = false;

    public Brickhead() {
        super(ZombieConfig.BRICK);
        this.walkSpeed = (Greenfoot.getRandomNumber(6) + 20) / 100.0;

        wNormal  = importSprites(ZombieAssets.BRICK_WALK.path,          ZombieAssets.BRICK_WALK.count);
        wD1      = importSprites(ZombieAssets.BRICK_WALK_D1.path,       ZombieAssets.BRICK_WALK_D1.count);
        wD2      = importSprites(ZombieAssets.BRICK_WALK_D2.path,       ZombieAssets.BRICK_WALK_D2.count);
        wBare    = importSprites(ZombieAssets.SHARED_WALK_BARE.path,    ZombieAssets.SHARED_WALK_BARE.count);
        wArmless = importSprites(ZombieAssets.SHARED_WALK_ARMLESS.path, ZombieAssets.SHARED_WALK_ARMLESS.count);

        eNormal  = importSprites(ZombieAssets.BRICK_EAT.path,           ZombieAssets.BRICK_EAT.count);
        eD1      = importSprites(ZombieAssets.BRICK_EAT_D1.path,        ZombieAssets.BRICK_EAT_D1.count);
        eD2      = importSprites(ZombieAssets.BRICK_EAT_D2.path,        ZombieAssets.BRICK_EAT_D2.count);
        eBare    = importSprites(ZombieAssets.SHARED_EAT_BARE.path,     ZombieAssets.SHARED_EAT_BARE.count);
        eArmless = importSprites(ZombieAssets.SHARED_EAT_ARMLESS.path,  ZombieAssets.SHARED_EAT_ARMLESS.count);

        setState(new WalkingState(this));
    }

    @Override
    protected void handleThresholds() {
        int currentHp = getHp();
        if (currentHp <= ZombieRegistry.BRICK_BARE && brick) {
            brick = false;
            AudioManager.getInstance().playSound(80, false, "limbs_pop.mp3");
            if (getWorld() != null) getWorld().addObject(new Brick(), getX(), getY() - 25);
        }
        if (currentHp <= ZombieRegistry.BRICK_ARMLESS && !fallen) {
            fallen = true;
            AudioManager.getInstance().playSound(80, false, "limbs_pop.mp3");
            if (getWorld() != null) getWorld().addObject(new Arm(), getX() + 8, getY() + 20);
        }
    }

    @Override
    public void hit(int dmg) {
        if (getHp() <= 0 && !isLiving() && finalDeath) return;
        if (isLiving()) {
            AudioManager.getInstance().playSound(80, false, brick ? "shieldhit.mp3" : "splat.mp3");
            int currentHp = getHp();
            ZombieAssets asset;
            if (currentHp > ZombieRegistry.BRICK_D1) {
                asset = eating ? ZombieAssets.BRICK_EAT : ZombieAssets.BRICK_WALK;
            } else if (currentHp > ZombieRegistry.BRICK_D2) {
                asset = eating ? ZombieAssets.BRICK_EAT_D1 : ZombieAssets.BRICK_WALK_D1;
            } else if (currentHp > ZombieRegistry.BRICK_BARE) {
                asset = eating ? ZombieAssets.BRICK_EAT_D2 : ZombieAssets.BRICK_WALK_D2;
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

    @Override
    public GreenfootImage[] getCurrentAnimation(boolean isEating) {
        int currentHp = getHp();
        if (currentHp > ZombieRegistry.BRICK_D1) return isEating ? eNormal : wNormal;
        if (currentHp > ZombieRegistry.BRICK_D2) return isEating ? eD1 : wD1;
        if (currentHp > ZombieRegistry.BRICK_BARE) return isEating ? eD2 : wD2;
        if (!fallen) return isEating ? eBare : wBare;
        return isEating ? eArmless : wArmless;
    }

    @Override
    protected void deathAnim() {
        if (getWorld() == null) return;

        if (!resetAnim) {
            frame = 0;
            resetAnim = true;
            removeFromRow();
            eventBus.publishDeath(this);
            target = null;
            eating = false;
        }

        if (finalDeath) {
            if (!fixAnim) {
                fixAnim = true;
                AudioManager.playSound(80, false, "zombie_falling_1.mp3", "zombie_falling_2.mp3");
                getWorld().addObject(new FallingZombie(fall), getX(), getY());
                getWorld().removeObject(this);
            }
        } else {
            if (!spawnHead) {
                spawnHead = true;
                AudioManager.getInstance().playSound(80, false, "limbs_pop.mp3");
                getWorld().addObject(new Head(), getX() + 10, getY() - 20);
            }

            boolean isAnimFinished;
            if (checkEating()) {
                isAnimFinished = animate(headlesseating, 350, false);
                playEating();
            } else {
                isAnimFinished = animate(headless, 350, false);
                walk();
            }

            if (isAnimFinished || frame >= headless.length - 1) {
                finalDeath = true;
                frame = 0; 
            }
        }
    }
}