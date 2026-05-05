import greenfoot.*;

public class SpriteAnimator extends PhysicsBody {
    public long deltaTime;
    public long lastFrame = System.nanoTime();
    public long currentFrame = System.nanoTime();
    public GreenfootImage[] previousSprites = null;
    public int frame = 0;

    private GreenfootImage[] flashSprites = null;
    private String flashFilename = null;
    private int flashRemainingFrames = 0;

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
        currentFrame = System.nanoTime();
        deltaTime = (currentFrame - lastFrame) / 1_000_000;

        if (sprite != previousSprites) {
            frame = 0;
            previousSprites = sprite;
            setImage(sprite[0]);
            lastFrame = currentFrame;
            return false;
        }

        if (deltaTime >= duration) {
            lastFrame = currentFrame;
            frame++;

            if (frame >= sprite.length) {
                if (loop) frame = 0;
                else return true;
            }

            resolveFlash();
            
            if (flashRemainingFrames > 0) {
                try {
                    setImage(new GreenfootImage("flash" + flashFilename + (frame + 1) + ".png"));
                } catch (Exception e) {
                    setImage(sprite[frame]);
                }
                flashRemainingFrames--;
            } else {
                setImage(sprite[frame]);
            }
        }
        return false;
    }

    public void hitFlash(GreenfootImage[] sprite, String filename) {
        this.flashSprites = sprite;
        this.flashFilename = filename;
        this.flashRemainingFrames = 1; 
        try {
            setImage(new GreenfootImage("flash" + filename + (frame + 1) + ".png"));
        } catch (Exception e) {}
    }

    private void resolveFlash() {
        if (flashRemainingFrames <= 0) {
            flashSprites = null;
            flashFilename = null;
        }
    }

    public void setFrame(int toFrame) {
        frame = toFrame - 1;
    }

    public int getCurrentFrame() {
        return frame + 1;
    }
}