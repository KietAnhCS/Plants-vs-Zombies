import greenfoot.*;

public interface ISceneManager {
    
    void pauseGame();
    void resumeGame();
    boolean isPaused();
    
    void gameOver(boolean won);
    boolean isGameOver();
    
    void openSettingsMenu();
    void closeSettingsMenu();
    
    void stopAllMusic();
    World getRestartWorld();
}