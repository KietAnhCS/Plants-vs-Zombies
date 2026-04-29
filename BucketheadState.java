import greenfoot.*;

public class BucketheadState extends ZombieState {

    public BucketheadState(Zombie zombie) {
        super(zombie);
    }

    @Override
    public GreenfootImage[] getAnimation() {
        return zombie.eating ? getEatAnim() : getWalkAnim();
    }

    private GreenfootImage[] getWalkAnim() {
        Buckethead b = (Buckethead) zombie;
        if (b.hp > ZombieRegistry.BUCKET_D1)      return b.wNormal;
        if (b.hp > ZombieRegistry.BUCKET_D2)      return b.wD1;
        if (b.hp > ZombieRegistry.BUCKET_BARE)    return b.wD2;
        return (b.hp > ZombieRegistry.BUCKET_ARMLESS) ? b.wBare : b.wArmless;
    }

    private GreenfootImage[] getEatAnim() {
        Buckethead b = (Buckethead) zombie;
        if (b.hp > ZombieRegistry.BUCKET_D1)      return b.eNormal;
        if (b.hp > ZombieRegistry.BUCKET_D2)      return b.eD1;
        if (b.hp > ZombieRegistry.BUCKET_BARE)    return b.eD2;
        return (b.hp > ZombieRegistry.BUCKET_ARMLESS) ? b.eBare : b.eArmless;
    }
}