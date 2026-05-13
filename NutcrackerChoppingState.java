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

        // Use the animate method to check if the 25-frame cycle is finished
        boolean animationFinished = zombie.animate(getAnimation(), 40, false);

        if (animationFinished) {
            // Only AFTER the full chop is done, check if we should stop
            if (zombie.target == null || zombie.target.getWorld() == null || zombie.target.getHp() <= 0) {
               zombie.setState(new NutcrackerRechargeState(zombie));
            } else {
               // If plant is still alive, start another chop cycle
                zombie.setFrame(1);
            }
        }
    }
    @Override
    public void exit() {}
    @Override
    public GreenfootImage[] getAnimation() {
        return zombie.getCurrentAnimation(true); // Returns Chopping sprites
    }
}