import greenfoot.*;
public class EatingState implements IZombieState {
    private Zombie zombie;
    public EatingState(Zombie zombie) {
        this.zombie = zombie;
    }
    @Override
    public void enter() {}
    @Override
    public void update() {
        zombie.playEating();
        if (!zombie.checkEating()) {
            zombie.setState(new WalkingState(zombie));
        }
    }
    @Override
    public void exit() {}
    @Override
    public GreenfootImage[] getAnimation() {
        return zombie.getCurrentAnimation(true);
    }
}