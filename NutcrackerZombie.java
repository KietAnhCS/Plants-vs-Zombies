import greenfoot.*;

public class NutcrackerZombie extends Zombie {
    private GreenfootImage[] wNormal, chop, recharge, death;

    public NutcrackerZombie() {
        super(new ZombieConfig(
            ZombieRegistry.NUTCRACKER_HP,
            ZombieRegistry.NUTCRACKER_DAMAGE,
            ZombieRegistry.NUTCRACKER_SPEED,
            "Nutcracker",
            new int[]{},
            null, null, null, 0
        ));

        wNormal  = importSprites(ZombieAssets.NUTCRACKER_WALK.path,     20, 0.45);
        chop     = importSprites(ZombieAssets.NUTCRACKER_CHOP.path,     25, 0.45);
        recharge = importSprites(ZombieAssets.NUTCRACKER_RECHARGE.path, 25, 0.45);
        death    = importSprites(ZombieAssets.NUTCRACKER_DEATH.path,    20, 0.45);

        if (wNormal != null && wNormal.length > 0) setImage(wNormal[0]);

        setState(new WalkingState(this));
    }

    @Override
    public void update() {
        if (getWorld() == null) return;
        if (!getWorld().getObjects(Overlay.class).isEmpty()) return;
        if (isLiving()) {
            if (currentState instanceof WalkingState && checkEating()) {
                setState(new NutcrackerChoppingState(this));
                return;
            }
            updateLogic();
            handleThresholds();
        } else {
            deathAnim();
        }
    }

    @Override
    public void hit(int dmg) {
        if (currentState instanceof NutcrackerRechargeState) dmg *= 2;
        if (isLiving()) {
            AudioManager.getInstance().playSound(80, false, "splat.mp3");
            String path = ZombieAssets.NUTCRACKER_WALK.path;
            if (currentState instanceof NutcrackerChoppingState)  path = ZombieAssets.NUTCRACKER_CHOP.path;
            if (currentState instanceof NutcrackerRechargeState) path = ZombieAssets.NUTCRACKER_RECHARGE.path;
            hitFlash(path);
        }
        super.hit(dmg);
    }

    @Override
    protected void deathAnim() {
        if (getWorld() == null) return;
        if (!resetAnim) {
            frame = 0;
            resetAnim = true;
            removeFromRow();
        }
        if (animate(death, 150, false)) {
            getWorld().removeObject(this);
        }
    }

    @Override
    public GreenfootImage[] getCurrentAnimation(boolean isChopping) {
        return isChopping ? chop : wNormal;
    }

    public GreenfootImage[] getRechargeSprites() {
        return recharge;
    }

    @Override
    protected void handleThresholds() {}
}