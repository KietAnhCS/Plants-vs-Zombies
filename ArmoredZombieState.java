import greenfoot.*;

public class ArmoredZombieState extends ZombieState {
    private final int[] thresholds;
    private final GreenfootImage[][] walkAnims;
    private final GreenfootImage[][] eatAnims;

    public ArmoredZombieState(Zombie zombie, int[] thresholds,
                               GreenfootImage[][] walkAnims, GreenfootImage[][] eatAnims) {
        super(zombie);
        this.thresholds = thresholds;
        this.walkAnims = walkAnims;
        this.eatAnims = eatAnims;
    }

    @Override
    public GreenfootImage[] getAnimation() {
        return zombie.eating ? getAnim(eatAnims) : getAnim(walkAnims);
    }

    private GreenfootImage[] getAnim(GreenfootImage[][] animSet) {
        for (int i = 0; i < thresholds.length; i++) {
            if (zombie.hp > thresholds[i]) return animSet[i];
        }
        return animSet[animSet.length - 1];
    }
}