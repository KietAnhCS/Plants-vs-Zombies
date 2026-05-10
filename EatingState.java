import greenfoot.*;

public class EatingState implements IZombieState 
{
    private Zombie zombie;

    public EatingState(Zombie zombie) {
        this.zombie = zombie;
    }

    @Override
    public void enter() {
    }

    @Override
    public void update() {
        zombie.playEating();

        if (!zombie.checkEating()) {
            zombie.setState(new WalkingState(zombie)); 
        }
    }

    @Override
    public void exit() {
    }

    @Override
    public GreenfootImage[] getAnimation() {
        int index = 0;
        int currentHp = zombie.getHp();
        
        for (int i = 0; i < zombie.config.thresholds.length; i++) {
            if (currentHp <= zombie.config.thresholds[i]) {
                index = i + 1;
            }
        }
        
        if (index >= zombie.config.eatKeys.length) {
            index = zombie.config.eatKeys.length - 1;
        }

        return zombie.config.eatKeys[index].getImages();
    }
}