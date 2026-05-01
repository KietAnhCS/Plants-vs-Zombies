import greenfoot.*;

public class SpriteAnimator extends PhysicsBody {
    public long deltaTime;
    public long lastFrame    = System.nanoTime();
    public long currentFrame = System.nanoTime();
    public GreenfootImage[] previousSprites = null;
    public int frame = 0;

    private GreenfootImage[] flashSprites   = null;
    private String           flashFilename  = null;
    private int              flashFrameA    = -1;
    private int              flashFrameB    = -1;
    private long             flashStartTime = -1;
    private static final long FLASH_DURATION_MS = 500;

    public GreenfootImage[] importSprites(String filename, int frames) {
        GreenfootImage[] sprites = new GreenfootImage[frames];
        for (int i = 0; i < frames; i++) {
            sprites[i] = new GreenfootImage(filename + (i + 1) + ".png");
        }
        return sprites;
    }

    public GreenfootImage[] importSprites(String filename, int frames, double scaleFactor) {
        GreenfootImage[] sprites = new GreenfootImage[frames];
        for (int i = 0; i < frames; i++) {
            GreenfootImage img = new GreenfootImage(filename + (i + 1) + ".png");
            img.scale((int)(img.getWidth() * scaleFactor), (int)(img.getHeight() * scaleFactor));
            sprites[i] = img;
        }
        return sprites;
    }

    public boolean animate(GreenfootImage[] sprite, long duration) {
        return animate(sprite, duration, true);
    }

    public boolean animate(GreenfootImage[] sprite, long duration, boolean loop) {
        resolveFlash();

        currentFrame = System.nanoTime();
        deltaTime    = (currentFrame - lastFrame) / 1_000_000;

        if (!sprite.equals(previousSprites)) {
            frame           = 0;
            previousSprites = sprite;
            setImage(sprite[0]);
            lastFrame = currentFrame;
            return false;
        }

        if (deltaTime >= duration) {
            lastFrame    = currentFrame;
            int next     = frame + 1;
            if (next < sprite.length) {
                frame = next;
                setImage(sprite[frame]);
            } else if (loop) {
                frame = 0;
                setImage(sprite[frame]);
            } else {
                return true;
            }
        }
        return false;
    }

    public void hitFlash(GreenfootImage[] sprite, String filename) {
        flashSprites   = sprite;
        flashFilename  = filename;
        flashFrameA    = frame < sprite.length ? frame : 0;
        flashFrameB    = (flashFrameA + 1) < sprite.length ? flashFrameA + 1 : 0;
        flashStartTime = System.nanoTime();

        sprite[flashFrameA] = new GreenfootImage("flash" + filename + (flashFrameA + 1) + ".png");
        sprite[flashFrameB] = new GreenfootImage("flash" + filename + (flashFrameB + 1) + ".png");
        setImage(sprite[flashFrameA]);
    }

    private void resolveFlash() {
        if (flashSprites == null) return;
        if ((System.nanoTime() - flashStartTime) / 1_000_000 < FLASH_DURATION_MS) return;

        flashSprites[flashFrameA] = new GreenfootImage(flashFilename + (flashFrameA + 1) + ".png");
        flashSprites[flashFrameB] = new GreenfootImage(flashFilename + (flashFrameB + 1) + ".png");

        flashSprites   = null;
        flashFilename  = null;
        flashFrameA    = -1;
        flashFrameB    = -1;
        flashStartTime = -1;
    }

    public void setFrame(int toFrame) {
        frame = toFrame - 1;
    }

    public int getCurrentFrame() {
        return frame + 1;
    }
}