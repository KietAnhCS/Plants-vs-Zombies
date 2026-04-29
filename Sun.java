import greenfoot.*;
import java.util.*;

public class Sun extends FallingObject {

    private int value = 25; 
    private PlayScene scene;
    private GreenfootImage[] sprites;

    private boolean pickedUp = false; 
    private long lifetimeStart;
    private boolean stationary = false;

    public Sun() {
        this(25);
    }

    public Sun(int value) {
        super(-3.5, 0.15, Random.Int(-1, 1), 1, 800L); 
        this.value = value;
        sprites = importSprites("sun", 2);
    }

    public Sun(int value, boolean stationary) {
        super(0, 0, 0, 0, 0L);
        this.value = value;
        this.stationary = stationary;
        sprites = importSprites("sun", 2);
    }

    public void update() {
        if (getWorld() == null) return;

        animate(sprites, 200, true);

        if (!pickedUp) {
            if (Greenfoot.mouseClicked(this) || isTouching(ThuyThan.class)) {
                collect();
            } else {
                handleAutoFadeOut();
                if (!stationary) applyFallingPhysics();
            }
        } else {
            flyToCounter();
        }

        checkRemoval();
    }

    protected void collect() {
        if (pickedUp) return;

        pickedUp = true;
        setRotation(0);

        AudioManager.playSound(80, false, "points.mp3");

        if (scene != null && scene.getSunManager() != null) {
            scene.getSunManager().add(value);
        }
    }

    private void applyFallingPhysics() {
        if (elapsedTime < fallTime) {
            double x = getExactX() + hSpeed;
            double y = getExactY() + vSpeed;
            setLocation(x, y);
            turn(rotate);
            vSpeed += acceleration;
        }
    }

    private void flyToCounter() {
        if (scene == null) return;
        
        List<SunDisplay> displays = scene.getObjects(SunDisplay.class);
        if (!displays.isEmpty()) {
            SunDisplay ds = displays.get(0);
            turnTowards(ds.getX(), ds.getY());
            move(15);
        } else {
            fadeOut(25);
        }
    }

    private void handleAutoFadeOut() {
        if ((System.currentTimeMillis() - lifetimeStart) > 12000) {
            fadeOut(10);
        }
    }

    private void fadeOut(int amount) {
        int trans = getImage().getTransparency();
        if (trans > amount) getImage().setTransparency(trans - amount);
        else getImage().setTransparency(0);
    }

    private void checkRemoval() {
        if (getWorld() == null) return;

        boolean reachedCounter = false;
        List<SunDisplay> displays = scene.getObjects(SunDisplay.class);
        if (!displays.isEmpty()) {
            SunDisplay ds = displays.get(0);
            reachedCounter = Math.abs(getX() - ds.getX()) < 20 && Math.abs(getY() - ds.getY()) < 20;
        }

        if (getImage().getTransparency() == 0 || (pickedUp && reachedCounter)) {
            getWorld().removeObject(this);
        }
    }

    @Override
    public void addedToWorld(World world) {
        if (world instanceof PlayScene) {
            scene = (PlayScene) world;
        }
        lifetimeStart = System.currentTimeMillis(); 
    }
}