import greenfoot.*;

public class DeadState implements IZombieState {
    private Zombie zombie;
    private boolean hasFinishedDeathAnim = false;

    public DeadState(Zombie zombie) {
        this.zombie = zombie;
    }

    @Override
    public void enter() {
        zombie.setFrame(0);
        if (zombie.getWorld() instanceof PlayScene) {
            PlayScene scene = (PlayScene) zombie.getWorld();
            if (scene.level != null && scene.level.zombieRow != null) {
                scene.level.zombieRow.get(zombie.getYPos()).remove(zombie);
            }
        }
    }

    @Override
    public void update() {
        if (hasFinishedDeathAnim) return;
        if (zombie.animate(getAnimation(), 200, false)) {
            hasFinishedDeathAnim = true;
            cleanup();
        }
    }

    private void cleanup() {
        World world = zombie.getWorld();
        if (world != null) {
            world.removeObject(zombie);
        }
    }

    @Override
    public void exit() {}

    @Override
    public GreenfootImage[] getAnimation() {
        return zombie.getDeadSprites();
    }
}