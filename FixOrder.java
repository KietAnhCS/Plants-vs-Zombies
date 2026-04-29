import greenfoot.*;

public class FixOrder extends Actor {
    private WaveManager level;
    private long delayTime;
    private long startTime;
    private long pausedTime;
    private boolean wasPaused = false;
    private PlayScene playScene; 

    public FixOrder(WaveManager level, long delayTimeMillis) {
        this.level = level;
        this.delayTime = delayTimeMillis;
        this.startTime = System.currentTimeMillis();
        setImage(new GreenfootImage(1, 1)); 
    }

    @Override
    protected void addedToWorld(World world) {
        if (world instanceof PlayScene) {
            this.playScene = (PlayScene) world;
        }
    }

    @Override
    public void act() {
        if (getWorld() == null || playScene == null) return;

        if (level != null && level.choosingCard) {
            if (!wasPaused) {
                pausedTime = System.currentTimeMillis() - startTime;
                wasPaused = true;
            }
            return;
        }

        if (wasPaused) {
            startTime = System.currentTimeMillis() - pausedTime;
            wasPaused = false;
        }

        long elapsed = System.currentTimeMillis() - startTime;

        if (elapsed >= delayTime) {
            playScene.applyDefaultPaintOrder();
            getWorld().removeObject(this);
        }
    }
}