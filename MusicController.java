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

        if (w <= 2) {
            target = "sans.mp3";
        } else if (w >= 5) {
            target = "finalwavemp3.mp3";
        } else {
            target = "intro3.mp3";
        }

        if (!currentBGM.equals(target)) {
            currentBGM = target;
            AudioManager.playBGM(target);
        }
    }
}