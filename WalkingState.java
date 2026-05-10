import greenfoot.*;
public class WalkingState implements IZombieState {
    private Zombie zombie;
    public WalkingState(Zombie zombie) {
        this.zombie = zombie;
    }
    @Override
    public void enter() {}
    @Override
    public void update() {
        if (zombie.checkEating()) {
            zombie.setState(new EatingState(zombie));
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