import greenfoot.*;

public class FallingZombie extends SpriteAnimator {
    private GreenfootImage[] images;
    
    public FallingZombie(GreenfootImage[] imgs) {
        this.images = imgs;
        if (images != null) setImage(images[0]);
    }
    
    public void act() {
        if (animate(images, 150, false)) {
            getWorld().removeObject(this);
        }
    }
}