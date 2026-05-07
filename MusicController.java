public class MusicController {
    private final PlayScene scene;
    private String currentBGM = "";

    public MusicController(PlayScene scene) {
        this.scene = scene;
    }

    public void resetBGM() { currentBGM = ""; }

    public void update() {
        if (scene.level == null || scene.loseOnce || scene.winOnce
                || scene.level.choosingCard
                || !scene.getObjects(CrazyDave.class).isEmpty()) return;

        int    w      = scene.level.getWaveNumber();
        String target = w <= 3 ? "sans.mp3"
                      : (w >= 14) ? "finalwavemp3.mp3"
                      : "intro3.mp3";

        if (!currentBGM.equals(target)) {
            currentBGM = target;
            AudioManager.playBGM(target);
        }
    }
}