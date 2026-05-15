import greenfoot.*;

public class ExcavatorWalkingState implements IZombieState {
    private ExcavatorZombie zombie;

    public ExcavatorWalkingState(ExcavatorZombie zombie) {
        this.zombie = zombie;
    }

    @Override
    public void enter() {}

    @Override
    public void update() {
        if (zombie.isWaiting()) {
            return;
        }
        if (zombie.checkEating()) {
            if (zombie.canShovel()) {
                zombie.startShovel();
            }
            return;
        }
        zombie.walk();
    }

    @Override
    public void exit() {}

    @Override
    public GreenfootImage[] getAnimation() {
        return zombie.getCurrentAnimation(false);
    }
}