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

        // 1. Kiểm tra các điều kiện dừng nhạc hoàn toàn
        boolean shouldStop = scene.loseOnce || scene.winOnce 
                            || scene.level.choosingCard 
                            || !scene.getObjects(CrazyDave.class).isEmpty();

        if (shouldStop) {
            // Nếu nhạc đang chạy mà gặp điều kiện dừng, ta nên stop BGM hiện tại
            if (!currentBGM.equals("")) {
                AudioManager.stopBGM();
                currentBGM = ""; // Reset để sau đó có thể play lại từ đầu
            }
            return;
        }

        // 2. Xác định nhạc mục tiêu dựa trên Wave hiện tại
        int w = scene.level.getWaveNumber();
        String target;

        if (w <= 3) {
            target = "sans.mp3";
        } else if (w >= 14) {
            target = "finalwavemp3.mp3";
        } else {
            target = "intro3.mp3";
        }

        // 3. Chỉ thay đổi nhạc nếu nhạc mục tiêu khác với nhạc đang phát
        if (!currentBGM.equals(target)) {
            currentBGM = target;
            // Dùng playBGM (giả định hàm này có cơ chế stop nhạc cũ trước khi play nhạc mới)
            AudioManager.playBGM(target);
        }
    }
}