import greenfoot.*;

public class Conehead extends Zombie {

    public GreenfootImage[] wNormal, wD1, wD2, wBare, wArmless;
    public GreenfootImage[] eNormal, eD1, eD2, eBare, eArmless;
    private boolean cone = true;

    public Conehead() {
        super();
        this.maxHp = ZombieRegistry.CONE_HP;
        this.hp = maxHp;
        this.damage = ZombieRegistry.CONE_DAMAGE;
        this.walkSpeed = (Greenfoot.getRandomNumber(6) + 22) / 100.0;
        loadSprites();
        this.currentState = new ConeheadState(this);
    }

    private void loadSprites() {
        wNormal  = importSprites(ZombieAssets.CONE_WALK, 7);
        wD1      = importSprites(ZombieAssets.CONE_WALK_D1, 7);
        wD2      = importSprites(ZombieAssets.CONE_WALK_D2, 7);
        wBare    = importSprites(ZombieAssets.SHARED_WALK_BARE, 7);
        wArmless = importSprites(ZombieAssets.SHARED_WALK_ARMLESS, 7);

        eNormal  = importSprites(ZombieAssets.CONE_EAT, 7);
        eD1      = importSprites(ZombieAssets.CONE_EAT_D1, 7);
        eD2      = importSprites(ZombieAssets.CONE_EAT_D2, 7);
        eBare    = importSprites(ZombieAssets.SHARED_EAT_BARE, 7);
        eArmless = importSprites(ZombieAssets.SHARED_EAT_ARMLESS, 7);
    }

    @Override
    protected void handleThresholds() {
        if (hp <= ZombieRegistry.CONE_BARE && cone) {
            cone = false;
            AudioManager.playSound(80, false, "limbs_pop.mp3");
            if (getWorld() != null) getWorld().addObject(new Cone(), getX(), getY() - 25);
        }
        if (hp <= ZombieRegistry.CONE_ARMLESS && !fallen) {
            fallen = true;
            AudioManager.playSound(80, false, "limbs_pop.mp3");
            if (getWorld() != null) getWorld().addObject(new Arm(), getX() + 8, getY() + 20);
        }
    }

    @Override
    public void hit(int dmg) {
        if (!isAlive) return;
        AudioManager.playSound(80, false, cone ? "plastichit.mp3" : "splat.mp3",
                                          cone ? "plastichit2.mp3" : "splat2.mp3");
        if (isLiving()) {
            if (hp > ZombieRegistry.CONE_D1)        hitFlash(eating ? eNormal  : wNormal,  eating ? ZombieAssets.CONE_EAT         : ZombieAssets.CONE_WALK);
            else if (hp > ZombieRegistry.CONE_D2)   hitFlash(eating ? eD1      : wD1,      eating ? ZombieAssets.CONE_EAT_D1      : ZombieAssets.CONE_WALK_D1);
            else if (hp > ZombieRegistry.CONE_BARE) hitFlash(eating ? eD2      : wD2,      eating ? ZombieAssets.CONE_EAT_D2      : ZombieAssets.CONE_WALK_D2);
            else if (!fallen)                       hitFlash(eating ? eBare    : wBare,    eating ? ZombieAssets.SHARED_EAT_BARE   : ZombieAssets.SHARED_WALK_BARE);
            else                                    hitFlash(eating ? eArmless : wArmless, eating ? ZombieAssets.SHARED_EAT_ARMLESS : ZombieAssets.SHARED_WALK_ARMLESS);
        } else if (!finalDeath) {
            hitFlash(eating ? headlesseating : headless, eating ? ZombieAssets.SHARED_HEADLESS_EAT : ZombieAssets.SHARED_HEADLESS);
        }
        super.hit(dmg);
    }
}