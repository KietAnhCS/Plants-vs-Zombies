import greenfoot.*;

public class ConeheadState extends ZombieState {

    public ConeheadState(Zombie zombie) {
        super(zombie);
    }

    @Override
    public GreenfootImage[] getAnimation() {
        return zombie.eating ? getEatAnim() : getWalkAnim();
    }

    private GreenfootImage[] getWalkAnim() {
        Conehead c = (Conehead) zombie;
        if (c.hp > ZombieRegistry.CONE_D1)      return c.wNormal;
        if (c.hp > ZombieRegistry.CONE_D2)      return c.wD1;
        if (c.hp > ZombieRegistry.CONE_BARE)    return c.wD2;
        return (c.hp > ZombieRegistry.CONE_ARMLESS) ? c.wBare : c.wArmless;
    }

    private GreenfootImage[] getEatAnim() {
        Conehead c = (Conehead) zombie;
        if (c.hp > ZombieRegistry.CONE_D1)      return c.eNormal;
        if (c.hp > ZombieRegistry.CONE_D2)      return c.eD1;
        if (c.hp > ZombieRegistry.CONE_BARE)    return c.eD2;
        return (c.hp > ZombieRegistry.CONE_ARMLESS) ? c.eBare : c.eArmless;
    }
}