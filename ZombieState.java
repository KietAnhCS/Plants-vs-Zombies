import greenfoot.*;

public abstract class ZombieState {
    protected final Zombie zombie;

    public ZombieState(Zombie zombie) {
        this.zombie = zombie;
    }

    public void update() {
        if (zombie.checkEating()) {
            zombie.playEating();
        } else {
            zombie.walk();
        }
        zombie.setState(this);
    }

    public abstract GreenfootImage[] getAnimation();
}