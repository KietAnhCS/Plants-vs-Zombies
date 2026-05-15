import greenfoot.*;

public class DebugHandler {
    private final PlayScene scene;

    public DebugHandler(PlayScene scene) {
        this.scene = scene;
    }

    public void update() {
        String key = Greenfoot.getKey();
        if ("1".equals(key)) {
            scene.stopAllMusic();
            Greenfoot.setWorld(new Arena());
        }
    }
}