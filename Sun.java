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

    private void collect() {
        if (pickedUp) return;

        pickedUp = true;
        setRotation(0);

        AudioPlayer.play(90, "points.mp3");

        if (scene != null && scene.getSunManager() != null) {
            scene.getSunManager().add(value);
        }
    }

    public boolean isPickedUp() {
        return pickedUp;
    }

    public int getValue() {
        return value;
    }

    public void collectByHero() {
        collect();
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
        turnTowards(SunDisplay.x, SunDisplay.y);
        move(15);
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

        boolean reachedCounter =
            Math.abs(getX() - SunDisplay.x) < 20 &&
            Math.abs(getY() - SunDisplay.y) < 20;

        if (getImage().getTransparency() == 0 || (pickedUp && reachedCounter)) {
            getWorld().removeObject(this);
        }
    }

    @Override
    public void addedToWorld(World world) {
        scene = (PlayScene) world;
        lifetimeStart = System.currentTimeMillis(); 
    }
}