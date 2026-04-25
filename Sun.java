import greenfoot.*;
import java.util.*;
public class Sun extends FallingObject {
    public int sunValue = 25; 
    private PlayScene playScene;
    private GreenfootImage[] sunSprites;
    private boolean beenClicked = false; 
    private long lifetimeStart;
    private boolean stationary = false;

    public Sun() {
        this(25);
    }

    public Sun(int value) {
        super(-3.5, 0.15, Random.Int(-1, 1), 1, 800L); 
        this.sunValue = value;
        sunSprites = importSprites("sun", 2);
    }

    public Sun(int value, boolean stationary) {
        super(0, 0, 0, 0, 0L);
        this.sunValue = value;
        this.stationary = stationary;
        sunSprites = importSprites("sun", 2);
    }

    public void update() {
        if (getWorld() == null) return;
        animate(sunSprites, 200, true);
        if (!beenClicked) {
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
        if (beenClicked) return;
        beenClicked = true;
        setRotation(0);
        AudioPlayer.play(90, "points.mp3");
        playScene.seedbank.sunCounter.addSun(sunValue);
    }

    public boolean isPickedUp() { return beenClicked; }

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
        turnTowards(SunCounter.x, SunCounter.y);
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
        boolean reachedCounter = Math.abs(getX() - SunCounter.x) < 20 && Math.abs(getY() - SunCounter.y) < 20;
        if (getImage().getTransparency() == 0 || (beenClicked && reachedCounter)) {
            getWorld().removeObject(this);
        }
    }

    @Override
    public void addedToWorld(World world) {
        playScene = (PlayScene) world;
        lifetimeStart = System.currentTimeMillis(); 
    }
}