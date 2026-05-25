import greenfoot.*;

public class MusicController {
    private final PlayScene scene;
    private String currentBGM = "";

    public MusicController(PlayScene scene) {
        this.scene = scene;
    }

    public void resetBGM() {
        currentBGM = "";
    }

    public void update() {
        if (scene == null || scene.level == null) return;

        boolean shouldStop = scene.loseOnce || scene.winOnce 
                            || scene.level.choosingCard 
                            || !scene.getObjects(CrazyDave.class).isEmpty();

        if (shouldStop) {
            if (!currentBGM.equals("")) {
                AudioManager.stopBGM();
                currentBGM = ""; 
            }
            return;
        }

        int w = scene.level.getWaveNumber();
        String target;

        if (w <= 1) {
            target = "sans.mp3";
        } else if (w >= 4) {
            target = "truehero.mp3";
        } else {
            target = "ghost.mp3";
        }

        if (!currentBGM.equals(target)) {
            currentBGM = target;
            AudioManager.playBGM(target);
        }
    }
}