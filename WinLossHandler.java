import greenfoot.*;
import java.util.List;

public class WinLossHandler {
    private final PlayScene scene;
    private boolean isFinished = false;

    public WinLossHandler(PlayScene scene) {
        this.scene = scene;
    }

    public void update() {
        if (isFinished) return;
        if (scene.hasLost() && !scene.loseOnce) {
            handleLoss();
        } else if (scene.hasWon() && !scene.winOnce) {
            handleWin();
        }
    }

    private void handleWin() {
        isFinished = true;
        scene.winOnce = true;
        scene.stopAllMusic();
        scene.removeObjects(scene.getObjects(DelayAudio.class));
        scene.finishLevel();
        Greenfoot.setWorld(new VictoryScreen());
    }

    private void handleLoss() {
        isFinished = true;
        scene.loseOnce = true;
        scene.finishLevel();
        scene.stopAllMusic();
        AudioManager.getInstance().playSound(80, false, "losemusic.mp3");
        scene.addObject(new DelayAudio("scream.mp3", 70, false, 2000L), 0, 0);
        scene.addObject(new Transition(false,
            new ResultScreen(scene.restartWorld), "gameover.png", 5), 555, 349);
    }
}