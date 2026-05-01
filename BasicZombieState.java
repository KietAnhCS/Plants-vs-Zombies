import greenfoot.*;

public class BasicZombieState extends ZombieState {
    public BasicZombieState(Zombie zombie) {
        super(zombie);
    }

    @Override
    public GreenfootImage[] getAnimation() {
        BasicZombie b = (BasicZombie) zombie;
        boolean armless = b.hp <= ZombieRegistry.BASIC_ARMLESS;
        if (zombie.eating) return armless ? b.eArmless : b.eNormal;
        return armless ? b.wArmless : b.wNormal;
    }
}