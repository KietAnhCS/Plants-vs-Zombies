import greenfoot.*;
import java.util.*;

public class FallingSun extends FallingObject {
    private PlayScene playScene;
    private GreenfootImage[] sunSprites;
    private boolean beenClicked = false;
    private boolean landed = false;
    private boolean isBeingStolen = false;
    private int targetY;

    public FallingSun() {
        super(2.5, 0, 0, 0, 5000);
        sunSprites = importSprites("sun", 2);
    }

    @Override
    public void addedToWorld(World world) {
        playScene = (PlayScene) world;
        targetY = 100 + Greenfoot.getRandomNumber(world.getHeight() - 200);
        this.elapsedTime = 0;
        this.landed = false;
        this.beenClicked = false;
    }

    public void setBeingStolen(boolean stolen) {
        this.isBeingStolen = stolen;
    }

    public void act() {
        update();
    }

    public void update() {
        if (playScene == null) playScene = (PlayScene) getWorld();
        if (playScene == null) return;
        
        if (!playScene.getObjects(Overlay.class).isEmpty()) return;

        animate(sunSprites, 200, true);

        if (!beenClicked) {
            if (Greenfoot.mouseClicked(this) || isTouching(ThuyThan.class)) {
                collectSun();
            } else {
                handleFalling();
            }
        } else {
            flyToCounter();
        }

        checkRemoval();
    }

    public void collectSun() {
        if (beenClicked) return;
        beenClicked = true;
        AudioManager.getInstance().playSound(80, false, "points.mp3");
        
        if (playScene.getSunManager() != null) {
            playScene.getSunManager().add(20);
        }
    }

    private void handleFalling() {
        if (isBeingStolen) return; 

        if (!landed) {
            if (getY() >= targetY) {
                landed = true;
                return;
            }

            super.update(); 

            if (elapsedTime >= fallTime) {
                landed = true;
            }
        }
    }

    private void flyToCounter() {
        List<SunDisplay> displays = playScene.getObjects(SunDisplay.class);
        if (!displays.isEmpty()) {
            SunDisplay ds = displays.get(0);
            turnTowards(ds.getX(), ds.getY());
            move(25);
        } else {
            if (getWorld() != null) {
                getWorld().removeObject(this);
            }
        }
    }

    private void checkRemoval() {
        if (getWorld() == null || !beenClicked) return;

        List<SunDisplay> displays = playScene.getObjects(SunDisplay.class);
        if (!displays.isEmpty()) {
            SunDisplay ds = displays.get(0);
            double dist = Math.hypot(getX() - ds.getX(), getY() - ds.getY());
            if (dist < 35) {
                getWorld().removeObject(this);
            }
        }
    }
}