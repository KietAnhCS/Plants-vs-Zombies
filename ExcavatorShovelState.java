import greenfoot.*;

public class ExcavatorShovelState implements IZombieState {
    private Zombie zombie; // This was likely missing!
    private boolean triggered = false;

    public ExcavatorShovelState(Zombie zombie) {
        this.zombie = zombie;
    }

    @Override
    public void enter() {
        zombie.setFrame(1);
        triggered = false;
    }

    @Override
    public void update() {
        // Trigger the throw at frame 15
        if (!triggered && zombie.getCurrentFrame() == 15) {
            tossPlant();
            triggered = true;
        }

        if (zombie.animate(getAnimation(), 100, false)) {
            zombie.setState(new WalkingState(zombie));
        }
    }

    private void tossPlant() {
        PlayScene scene = (PlayScene) zombie.getWorld();
        Plant target = zombie.target;

        if (target == null || scene == null || target.getWorld() == null) {
            zombie.target = null; 
            zombie.eating = false;
            return;
        }

        int px = target.getX();
        int py = target.getY();

        int destX = 1200, destY = -200; 
        int targetGridX = -1;
        boolean out = true;
        
        for (int c = 8; c >= 0; c--) {
            if (scene.GridManager.Board[5][c] == null) {
                destX = scene.GridManager.getXCoord(c, 5);
                destY = scene.GridManager.getYCoord(c, 5);
                targetGridX = c;
                out = false;
                break; 
            }
        }

        scene.GridManager.removePlantFromBoard(target);
        scene.removeObject(target); 

        scene.addObject(new TossedPlant(target, destX, destY, targetGridX, out), px, py);
        
        zombie.target = null;
        zombie.eating = false;
        AudioManager.getInstance().playSound(80, false, "shovel.mp3");
    }

    @Override public void exit() {}
    @Override public GreenfootImage[] getAnimation() { 
        return ((ExcavatorZombie)zombie).getShovelSprites(); 
    }
}