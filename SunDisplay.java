import greenfoot.*;

public class SunDisplay extends Actor
{
    private PlayScene playScene;
    private int lastDisplayedSun = -1;
    private final int charWidth = 12;
    private final int textY = 45; 

    public SunDisplay() {
        setImage("suncounter.png");
    }

    public void addedToWorld(World world) {
        if (world instanceof PlayScene) {
            playScene = (PlayScene) world;
            
            updateText();
        }
    }

    public void act() {
        if (playScene != null && playScene.getSunManager() != null) {
            int currentSun = playScene.getSunManager().getSun();
            if (currentSun != lastDisplayedSun) {
                lastDisplayedSun = currentSun;
                updateText();
            }
        }
    }

    public void updateText() {
        int sunValue = 0;
        if (playScene != null && playScene.getSunManager() != null) {
            sunValue = playScene.getSunManager().getSun();
        }

        if (sunValue > 999999) sunValue = 999999;
        if (sunValue < 0) sunValue = 0;

        GreenfootImage bg = new GreenfootImage("suncounter.png");
        String sunStr = String.valueOf(sunValue);
        int len = sunStr.length();
        int startX = (bg.getWidth() / 2) - ((len * charWidth) / 2) + 5; 

        for (int i = 0; i < len; i++) {
            char c = sunStr.charAt(i);
            try {
                GreenfootImage numImg = new GreenfootImage("text" + c + ".png");
                bg.drawImage(numImg, startX + (i * charWidth), textY);
            } catch (Exception e) {
            }
        }
        setImage(bg);
    }

    public void addSun(int amount) {
        if (playScene != null && playScene.getSunManager() != null) {
            playScene.getSunManager().add(amount);
        }
    }

    public void removeSun(int amount) {
        if (playScene != null && playScene.getSunManager() != null) {
            playScene.getSunManager().spend(amount);
        }
    }
}