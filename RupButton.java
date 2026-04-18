import greenfoot.*;

public class RupButton extends Actor {
    private int currentLevel = 1;

    public RupButton() {
        updateAppearance();
    }

    public void act() {
        if (Greenfoot.mouseClicked(this)) {
            World world = getWorld();
            if (world instanceof PlayScene) {
                PlayScene ps = (PlayScene) world;
                if (ps.seedbank.getSun() >= 50 && currentLevel < 9) {
                    ps.seedbank.addSun(-50);
                    ps.upgradeProbabilities();
                    currentLevel++;
                    updateAppearance();
                    AudioPlayer.play(80, "achievement.mp3");
                }
            }
        }
    }

    private void updateAppearance() {
        GreenfootImage bg = new GreenfootImage("Up", 24, Color.WHITE, new Color(0, 0, 0, 150));
        try {
            GreenfootImage numImg = new GreenfootImage("text" + currentLevel + ".png");
            bg.drawImage(numImg, 5, 5);
        } catch (Exception e) {
            bg.setColor(Color.YELLOW);
            bg.drawString(String.valueOf(currentLevel), 5, 20);
        }
        setImage(bg);
    }
}