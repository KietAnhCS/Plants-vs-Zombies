import greenfoot.*;

public class RupButton extends Actor {
    private int currentLevel = 1;
    private final int MAX_LEVEL = 10;
    private final int BASE_COST = 125;

    public RupButton() {
        updateAppearance();
    }

    public void act() {
        if (Greenfoot.mouseClicked(this)) {
            World world = getWorld();
            if (world instanceof PlayScene) {
                PlayScene ps = (PlayScene) world;
                int upgradeCost = currentLevel * BASE_COST;

                if (ps.seedbank.getSun() >= upgradeCost && currentLevel < MAX_LEVEL) {
                    ps.seedbank.addSun(-upgradeCost); 
                    ps.upgradeProbabilities();
                    currentLevel++;
                    updateAppearance();
                    AudioPlayer.play(80, "achievement.mp3");
                }
            }
        }
    }

    private void updateAppearance() {
        GreenfootImage bg = new GreenfootImage(80, 60);
        bg.setColor(new Color(0, 0, 0, 150));
        bg.fill();
        
        String displayLevel = (currentLevel < MAX_LEVEL) ? "LV: " + currentLevel : "MAX";
        int nextCost = currentLevel * BASE_COST;

        try {
            GreenfootImage numImg = new GreenfootImage("text" + currentLevel + ".png");
            bg.drawImage(numImg, (bg.getWidth()-numImg.getWidth())/2, 5);
        } catch (Exception e) {
            bg.setColor(Color.WHITE);
            bg.setFont(new Font("Arial", true, false, 18));
            bg.drawString(displayLevel, 10, 25); 
            
            if (currentLevel < MAX_LEVEL) {
                bg.setColor(Color.YELLOW);
                bg.setFont(new Font("Arial", false, false, 14));
                bg.drawString("$" + nextCost, 10, 50);
            }
        }
        setImage(bg);
    }
}