import greenfoot.*;

public class BasicZombieState extends ZombieState {

    public BasicZombieState(Zombie zombie) {
        super(zombie);
    }

    public GreenfootImage[] getAnimation() {
        BasicZombie b = (BasicZombie) zombie;
        if (zombie.eating) return (b.hp > ZombieRegistry.BASIC_ARMLESS) ? b.eNormal : b.eArmless;
        return (b.hp > ZombieRegistry.BASIC_ARMLESS) ? b.wNormal : b.wArmless;
    }
}