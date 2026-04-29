import greenfoot.*;

public class FallingSun extends FallingObject
{
    private PlayScene playScene;
    private GreenfootImage[] sunSprites;
    private boolean beenClicked = false;
    private boolean landed = false;
    private long landedTime = 0;

    public FallingSun() {
        super(2.5, 0, 0, 0, 3000);
        sunSprites = importSprites("sun", 2);
    }

    public void act() {
        update();
    }

    public void update() {
        playScene = (PlayScene) getWorld();
        if (playScene == null) return;
        if (!playScene.getObjects(Overlay.class).isEmpty()) return;

        animate(sunSprites, 200, true);

        if (!beenClicked) {
            if (Greenfoot.mouseClicked(this) || isTouching(ThuyThan.class)) {
                collectSun();
            } else {
                handleFallingAndWaiting();
            }
        } else {
            flyToCounter();
        }

        checkRemoval();
    }

    public void collectSun() {
        if (beenClicked) return;
        beenClicked = true;
        AudioManager.playSound(80, false, "points.mp3");
        playScene.getSunManager().add(25);
    }

    private void handleFallingAndWaiting() {
        if (!landed) {
            super.update();
            if (elapsedTime >= fallTime) {
                landed = true;
                landedTime = System.currentTimeMillis();
            }
        } else {
            if (System.currentTimeMillis() - landedTime > 10000) {
                fadeOut(5);
            }
        }
    }

    private void flyToCounter() {
        int targetX = SunDisplay.x;
        int targetY = SunDisplay.y;
        turnTowards(targetX, targetY);
        move(25);
    }

    private void fadeOut(int amount) {
        int trans = getImage().getTransparency();
        if (trans > amount) getImage().setTransparency(trans - amount);
        else getImage().setTransparency(0);
    }

    private void checkRemoval() {
        int targetX = SunDisplay.x;
        int targetY = SunDisplay.y;
        double dist = Math.hypot(getX() - targetX, getY() - targetY);
        if (getImage().getTransparency() == 0 || (beenClicked && dist < 35)) {
            getWorld().removeObject(this);
        }
    }

    @Override
    public void addedToWorld(World world) {
        playScene = (PlayScene) world;
    }
}