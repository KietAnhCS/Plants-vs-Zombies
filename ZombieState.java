import greenfoot.*;

public abstract class ZombieState {
    protected Zombie zombie;
    
    public ZombieState(Zombie zombie) {
        this.zombie = zombie;
    }

    public void update() {
        zombie.isEating();
        if (zombie.eating) zombie.playEating();
        else zombie.move(-zombie.walkSpeed);
    }

    public abstract GreenfootImage[] getAnimation();
}