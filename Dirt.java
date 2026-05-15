import greenfoot.*;

public class Dirt extends SpriteAnimator {
    private final GreenfootImage[] dirtSprites;

    public Dirt() {
        dirtSprites = importSprites("dirt", 4);
    }

    @Override
    public void update() {
        boolean isFinished = animate(dirtSprites, 50L, false);

        if (isFinished) {
            getWorld().removeObject(this);
        }
    }
}