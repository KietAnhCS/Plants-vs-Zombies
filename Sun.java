import greenfoot.*;
import java.util.*;

public class Sun extends FallingObject {
    public int sunValue = 25; 
    private PlayScene PlayScene;
    private GreenfootImage[] sunSprites;
    private boolean beenClicked = false; // Cái khóa nằm ở đây
    private long lifetimeStart; 

    public Sun() {
        this(25);
    }

    public Sun(int value) {
        super(-3.5, 0.15, Random.Int(-1, 1), 1, 800L); 
        this.sunValue = value;
        sunSprites = importSprites("sun", 2);
    }

    public void update() {
        if (getWorld() == null) return;
        animate(sunSprites, 200, true);

        if (!beenClicked) {
            handleAutoFadeOut();
            applyFallingPhysics();
        } else {
            flyToCounter();
        }

        checkRemoval();
    }

    public boolean isPickedUp() {
        return beenClicked;
    }

    public void collectByHero() {
        if (beenClicked) return;
        beenClicked = true;
        setRotation(0); 
    }

    private void applyFallingPhysics() {
        currentFrame = System.nanoTime();
        deltaTime = (currentFrame - lastFrame) / 1000000;
        if (deltaTime < fallTime) {
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
        PlayScene = (PlayScene)world;
        lastFrame = System.nanoTime(); 
        lifetimeStart = System.currentTimeMillis(); 
    }
}