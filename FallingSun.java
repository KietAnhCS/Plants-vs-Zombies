import greenfoot.*;
import java.util.*;

public class FallingSun extends FallingObject {
    private GreenfootImage[] sunSprites;
    private boolean beenClicked   = false;
    private boolean landed        = false;
    private boolean beingStolen   = false;
    private int targetY;

    public FallingSun() {
        super(2.5, 0, 0, 0, 5000);
        sunSprites = importSprites("sun", 2);
    }

    @Override
    public void addedToWorld(World world) {
        targetY = 100 + Greenfoot.getRandomNumber(world.getHeight() - 200);
        elapsedTime = 0;
    }

    @Override
    public void act() {
        PlayScene playScene = (PlayScene) getWorld();
        if (playScene == null || !playScene.getObjects(Overlay.class).isEmpty()) return;
        animate(sunSprites, 200, true);
        if (beenClicked) {
            flyToCounter(playScene);
            checkRemoval(playScene);
        } else if (Greenfoot.mouseClicked(this)) {
            collectSun(playScene);
        } else if (!landed && !beingStolen) {
            if (getY() >= targetY) landed = true;
            else {
                super.update();
                if (elapsedTime >= fallTime) landed = true;
            }
        }
    }

    public void setBeingStolen(boolean stolen) { this.beingStolen = stolen; }
    
    public boolean isBeingStolen() { 
        return this.beingStolen; 
    }
    
    public void collectSun() {
        PlayScene playScene = (PlayScene) getWorld();
        if (playScene != null) collectSun(playScene);
    }

    private void collectSun(PlayScene playScene) {
        if (beenClicked) return;
        beenClicked = true;
        AudioManager.getInstance().playSound(80, false, "points.mp3");
        playScene.getSunManager().add(25);
    }

    private void flyToCounter(PlayScene playScene) {
        List<SunDisplay> displays = playScene.getObjects(SunDisplay.class);
        if (displays.isEmpty()) { getWorld().removeObject(this); return; }
        SunDisplay ds = displays.get(0);
        turnTowards(ds.getX(), ds.getY());
        move(25);
    }

    private void checkRemoval(PlayScene playScene) {
        List<SunDisplay> displays = playScene.getObjects(SunDisplay.class);
        if (displays.isEmpty()) return;
        SunDisplay ds = displays.get(0);
        if (Math.hypot(getX() - ds.getX(), getY() - ds.getY()) < 35)
            getWorld().removeObject(this);
    }
}
