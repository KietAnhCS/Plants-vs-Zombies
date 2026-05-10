import greenfoot.*;
public class SpriteAnimator extends PhysicsBody {
    private static SpriteCache cache = new SpriteCache();
    private long lastMillis = 0;
    private GreenfootImage[] previousSprites = null;
    public int frame = 0;
    private int flashRemainingFrames = 0;
    private String flashFilename = null;

    public GreenfootImage[] importSprites(String filename, int frames) {
        return cache.getSprites(filename, frames);
    }

    public GreenfootImage[] importSprites(String filename, int frames, double scaleFactor) {
        return cache.getSprites(filename, frames, scaleFactor);
    }

    public boolean animate(GreenfootImage[] sprite, long duration) {
        return animate(sprite, duration, true);
    }

    public boolean animate(GreenfootImage[] sprite, long duration, boolean loop) {
        if (sprite == null || sprite.length == 0) return false;
        if (sprite != previousSprites) {
            frame = 0;
            previousSprites = sprite;
            flashRemainingFrames = 0;
            flashFilename = null;
            lastMillis = 0;
            setImage(sprite[0]);
            return false;
        }
        long currentMillis = System.currentTimeMillis();
        if (lastMillis == 0) lastMillis = currentMillis;
        long elapsed = currentMillis - lastMillis;
        if (elapsed >= duration) {
            lastMillis = currentMillis;
            frame++;
            if (frame >= sprite.length) {
                frame = loop ? 0 : sprite.length - 1;
                if (!loop) {
                    setImage(sprite[frame]);
                    return true;
                }
            }
            if (flashRemainingFrames > 0) {
                try {
                    setImage(new GreenfootImage("images/flash" + flashFilename + (frame + 1) + ".png"));
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

    private void applyImageWithOpacity(GreenfootImage img) {
        if (img == null) return;
        if (this instanceof Plant) {
            Plant p = (Plant) this;
            GreenfootImage copy = new GreenfootImage(img);
            copy.setTransparency(p.opaque ? 160 : 255);
            setImage(copy);
            return;
        }
        setImage(img);
    }

    public void hitFlash(String filename) {
        this.flashFilename = filename;
        this.flashRemainingFrames = 2;
        try {
            setImage(new GreenfootImage("images/flash" + filename + (frame + 1) + ".png"));
        } catch (Exception e) {}
    }

    public void setFrame(int toFrame) {
        this.frame = Math.max(0, toFrame - 1);
    }

    public int getCurrentFrame() {
        return frame + 1;
    }

    public static void clearAnimationCache() {
        cache.clearCache();
    }
}