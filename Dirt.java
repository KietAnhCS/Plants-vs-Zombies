import greenfoot.*;

public class Dirt extends SpriteAnimator {
    private final GreenfootImage[] dirt;

    public Dirt() {
        dirt = importSprites("dirt", 4);
    }

    @Override
    public void act() {
        if (getWorld() == null) return;
        boolean isFinished = animate(dirt, 50L, false);

        if (isFinished) {
            getWorld().removeObject(this);
        }
    }
}