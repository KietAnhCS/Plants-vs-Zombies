import greenfoot.*;

public class WinLossHandler {
    private final PlayScene scene;

    public WinLossHandler(PlayScene scene) {
        this.scene = scene;
    }

    public void update() {
        if (!scene.loseOnce && scene.hasLost()) {
            scene.loseOnce = true;
            scene.stopAllMusic();
            AudioManager.playSound(80, false, "losemusic.mp3");
            scene.addObject(new DelayAudio("scream.mp3", 70, false, 4000L), 0, 0);
            scene.addObject(new Transition(false,
                new ResultScreen(scene.restartWorld), "gameover.png", 5), 365, 215);
        } else if (!scene.winOnce && scene.hasWon()) {
            scene.winOnce = true;
            scene.finishLevel();
            scene.addObject(scene.winPlant,
                Greenfoot.getRandomNumber(266) + 400, 215);
        }
    }
}