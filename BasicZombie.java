import greenfoot.*;

public class BasicZombie extends Zombie {

    public GreenfootImage[] wNormal, wArmless;
    public GreenfootImage[] eNormal, eArmless;

    public BasicZombie() {
        super();
        this.maxHp = ZombieRegistry.BASIC_HP;
        this.hp = maxHp;
        this.walkSpeed = (Greenfoot.getRandomNumber(6) + 22) / 100.0;
        this.damage = ZombieRegistry.BASIC_DAMAGE;
        loadSprites();
        this.currentState = new BasicZombieState(this);
    }

    private void loadSprites() {
        wNormal  = importSprites(ZombieAssets.BASIC_WALK, 7);
        wArmless = importSprites(ZombieAssets.BASIC_WALK_ARMLESS, 7);
        eNormal  = importSprites(ZombieAssets.BASIC_EAT, 7);
        eArmless = importSprites(ZombieAssets.BASIC_EAT_ARMLESS, 7);
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
            if (!fallen) hitFlash(eating ? eNormal  : wNormal,  eating ? ZombieAssets.BASIC_EAT        : ZombieAssets.BASIC_WALK);
            else         hitFlash(eating ? eArmless : wArmless, eating ? ZombieAssets.BASIC_EAT_ARMLESS : ZombieAssets.BASIC_WALK_ARMLESS);
        } else if (!finalDeath) {
            hitFlash(eating ? headlesseating : headless, eating ? ZombieAssets.SHARED_HEADLESS_EAT : ZombieAssets.SHARED_HEADLESS);
        }
        super.hit(dmg);
    }
}