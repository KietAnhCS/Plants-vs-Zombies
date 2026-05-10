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
            if (isTouching(ThuyThan.class)) {
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

    public void collect() {
        if (pickedUp) return;

        pickedUp = true;
        setRotation(0);
        AudioManager.getInstance().playSound(80, false, "points.mp3");

        PlayScene currentScene = getPlayScene();
        if (currentScene != null && currentScene.getSunManager() != null) {
            currentScene.getSunManager().add(this.value);
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
        PlayScene currentScene = getPlayScene();
        if (currentScene == null) return;
        
        List<SunDisplay> displays = currentScene.getObjects(SunDisplay.class);
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
        if (getImage() == null) return;
        int trans = getImage().getTransparency();
        getImage().setTransparency(Math.max(0, trans - amount));
    }

    private void checkRemoval() {
        if (getWorld() == null) return;

        boolean reachedCounter = false;
        PlayScene currentScene = getPlayScene();
        
        if (currentScene != null && pickedUp) {
            List<SunDisplay> displays = currentScene.getObjects(SunDisplay.class);
            if (!displays.isEmpty()) {
                SunDisplay ds = displays.get(0);
                reachedCounter = Math.hypot(getX() - ds.getX(), getY() - ds.getY()) < 20;
            }
        }

        if (getImage().getTransparency() == 0 || (pickedUp && reachedCounter)) {
            getWorld().removeObject(this);
        }
    }

    private PlayScene getPlayScene() {
        if (scene != null) return scene;
        if (getWorld() instanceof PlayScene) {
            scene = (PlayScene) getWorld();
            return scene;
        }
        return null;
    }

    @Override
    public void addedToWorld(World world) {
        if (world instanceof PlayScene) {
            scene = (PlayScene) world;
        }
        lifetimeStart = System.currentTimeMillis(); 
    }
}