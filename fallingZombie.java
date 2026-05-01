import greenfoot.*;

public class fallingZombie extends SpriteAnimator {
    private final GreenfootImage[] fall;
    private boolean fading = false;

    public fallingZombie(GreenfootImage[] fall) {
        this.fall = fall;
    }

    @Override
    public void act() {
        if (!fading) {
            if (animate(fall, 200, false)) fading = true;
            return;
        }

        int t = getImage().getTransparency() - 3;
        if (t <= 0) {
            getWorld().removeObject(this);
        } else {
            getImage().setTransparency(t);
        }
    }
}