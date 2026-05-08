import greenfoot.*;
import java.util.Map;
import java.util.HashMap;

public class SpriteCache {
    private Map<String, GreenfootImage[]> cache;

    public SpriteCache() {
        cache = new HashMap<>();
    }

    public GreenfootImage[] getSprites(String prefix, int frameCount) {
        if (cache.containsKey(prefix)) {
            return cache.get(prefix);
        }

        GreenfootImage[] images = new GreenfootImage[frameCount];
        for (int i = 0; i < frameCount; i++) {
            try {
                images[i] = new GreenfootImage(prefix + (i + 1) + ".png");
            } catch (Exception e) {
                System.out.println("Lỗi: Không tìm thấy file " + prefix + (i + 1));
            }
        }

        cache.put(prefix, images);
        return images;
    }

    public GreenfootImage[] getSprites(String prefix, int frameCount, double scaleFactor) {
        String cacheKey = prefix + "_scaled_" + scaleFactor;
        
        if (cache.containsKey(cacheKey)) {
            return cache.get(cacheKey);
        }

        GreenfootImage[] original = getSprites(prefix, frameCount);
        GreenfootImage[] scaled = new GreenfootImage[frameCount];

        for (int i = 0; i < frameCount; i++) {
            if (original[i] != null) {
                scaled[i] = new GreenfootImage(original[i]);
                int newWidth = (int) Math.max(1, original[i].getWidth() * scaleFactor);
                int newHeight = (int) Math.max(1, original[i].getHeight() * scaleFactor);
                scaled[i].scale(newWidth, newHeight);
            }
        }

        cache.put(cacheKey, scaled);
        return scaled;
    }

    public void clearCache() {
        cache.clear();
    }
}