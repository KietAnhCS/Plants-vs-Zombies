import greenfoot.*;

public class NutcrackerRechargeState implements IZombieState {
    private Zombie zombie;
    private long startTime;
    private final int duration = ZombieRegistry.NUTCRACKER_RECHARGE_MS;

    public NutcrackerRechargeState(Zombie zombie) {
        this.zombie = zombie;
    }
    @Override
    public void enter() {
        startTime = System.currentTimeMillis();
        zombie.target = null;
        zombie.eating = false;
    }
    @Override
    public void update() {
        // Do nothing (don't walk, don't eat)
        if (System.currentTimeMillis() - startTime > duration) {
            zombie.setState(new WalkingState(zombie));
        }
    }
    @Override
    public void exit() {}
    @Override
    public GreenfootImage[] getAnimation() {
        // Return the recharge sprites
        return ((NutcrackerZombie)zombie).getRechargeSprites();
    }
}