import greenfoot.*;

public class Dirt extends SpriteAnimator {
    private final GreenfootImage[] dirt;
    private boolean done = false;

    public Dirt() {
        dirt = importSprites("dirt", 4);
    }

    @Override
    public void act() {
        if (done) {
            getWorld().removeObject(this);
            return;
        }
        animate(dirt, 50L, false);
        if (frame >= dirt.length - 1 && deltaTime >= 50L) {
            done = true;
        }
    }
}