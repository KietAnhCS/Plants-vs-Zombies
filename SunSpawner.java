import greenfoot.*;

public class SunSpawner {
    private final PlayScene scene;
    private long lastSpawn;

    public SunSpawner(PlayScene scene) {
        this.scene = scene;
        this.lastSpawn = System.currentTimeMillis();
    }

    public void update() {
        if (scene.level.choosingCard
                || !scene.getObjects(CrazyDave.class).isEmpty()) return;
        long now = System.currentTimeMillis();
        if (now - lastSpawn >= 5000) {
            lastSpawn = now;
            scene.addObject(new FallingSun(),
                Greenfoot.getRandomNumber(700) + 200, -30);
        }
    }
}