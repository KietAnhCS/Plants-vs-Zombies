import greenfoot.*;

public class BrickheadState extends ZombieState {

    public BrickheadState(Zombie zombie) {
        super(zombie);
    }

    @Override
    public GreenfootImage[] getAnimation() {
        return zombie.eating ? getEatAnim() : getWalkAnim();
    }

    private GreenfootImage[] getWalkAnim() {
        Brickhead b = (Brickhead) zombie;
        if (b.hp > ZombieRegistry.BRICK_D1)      return b.wNormal;
        if (b.hp > ZombieRegistry.BRICK_D2)      return b.wD1;
        if (b.hp > ZombieRegistry.BRICK_BARE)    return b.wD2;
        return (b.hp > ZombieRegistry.BRICK_ARMLESS) ? b.wBare : b.wArmless;
    }

    private GreenfootImage[] getEatAnim() {
        Brickhead b = (Brickhead) zombie;
        if (b.hp > ZombieRegistry.BRICK_D1)      return b.eNormal;
        if (b.hp > ZombieRegistry.BRICK_D2)      return b.eD1;
        if (b.hp > ZombieRegistry.BRICK_BARE)    return b.eD2;
        return (b.hp > ZombieRegistry.BRICK_ARMLESS) ? b.eBare : b.eArmless;
    }
}