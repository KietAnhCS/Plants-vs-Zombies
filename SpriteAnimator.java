import greenfoot.*;

public class SpriteAnimator extends PhysicsBody {
    private static SpriteCache cache = new SpriteCache();
    
    private long lastMillis = System.currentTimeMillis();
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
            applyImageWithOpacity(sprite[0]);
            lastMillis = System.currentTimeMillis();
            return false;
        }

        long currentMillis = System.currentTimeMillis();
        long elapsed = currentMillis - lastMillis;

        if (elapsed >= duration) {
            lastMillis = currentMillis - (elapsed % duration);
            frame++;

            if (frame >= sprite.length) {
                if (loop) {
                    frame = 0;
                } else {
                    frame = sprite.length - 1;
                    applyImageWithOpacity(sprite[frame]);
                    return true;
                }
            }

            if (flashRemainingFrames > 0) {
                try {
                    String path = "images/flash" + flashFilename + (frame + 1) + ".png";
                    applyImageWithOpacity(new GreenfootImage(path));
                } catch (Exception e) {
                    applyImageWithOpacity(sprite[frame]);
                }
                flashRemainingFrames--;
            } else {
                applyImageWithOpacity(sprite[frame]);
            }
        }
        return false;
    }

    private void applyImageWithOpacity(GreenfootImage img) {
        if (img == null) return;
        if (this instanceof Plant) {
            Plant p = (Plant) this;
            int alpha = p.opaque ? 160 : 255;
            if (img.getTransparency() != alpha) {
                img.setTransparency(alpha);
            }
        }
        setImage(img);
    }

    public void hitFlash(String filename) {
        this.flashFilename = filename;
        this.flashRemainingFrames = 2; 
        try {
            String path = "images/flash" + filename + (frame + 1) + ".png";
            applyImageWithOpacity(new GreenfootImage(path));
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