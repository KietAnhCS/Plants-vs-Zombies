import greenfoot.*;

public class NutcrackerChoppingState implements IZombieState {
    private Zombie zombie;
    public NutcrackerChoppingState(Zombie zombie) {
        this.zombie = zombie;
    }
    @Override
    public void enter() {
        zombie.setFrame(1);
    }
    @Override
    public void update() {
        zombie.playEating(); 

        boolean animationFinished = zombie.animate(getAnimation(), 40, false);

        if (animationFinished) {
            if (zombie.target == null || zombie.target.getWorld() == null || zombie.target.getHp() <= 0) {
               zombie.setState(new NutcrackerRechargeState(zombie));
            } else {
                zombie.setFrame(1);
            }
        }
    }
    @Override
    public void exit() {}
    @Override
    public GreenfootImage[] getAnimation() {
        return zombie.getCurrentAnimation(true); 
    }
}