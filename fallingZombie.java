import greenfoot.*;

public class FallingZombie extends SpriteAnimator {
    private GreenfootImage[] fallSprites;
    
    public FallingZombie(GreenfootImage[] imgs) {
        this.fallSprites = imgs;
        if (fallSprites != null && fallSprites.length > 0) {
            setImage(fallSprites[0]);
        }
    }
    
    @Override
    public void addedToWorld(World world) {
        super.addedToWorld(world);
    }
    
    @Override
    public void update() {
        if (getWorld() == null) return;
        if (animate(fallSprites, 150L, false)) {
            getWorld().removeObject(this);
        }
    }
}