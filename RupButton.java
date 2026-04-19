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
            
                int upgradeCost = currentLevel * 50;

                if (ps.seedbank.getSun() >= upgradeCost && currentLevel < 9) {
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
        
        GreenfootImage bg = new GreenfootImage("Up", 24, Color.WHITE, new Color(0, 0, 0, 150));
        
        int nextCost = currentLevel * 50;
        String displayLevel = (currentLevel < 9) ? String.valueOf(currentLevel) : "MAX";
        
        try {
            GreenfootImage numImg = new GreenfootImage("text" + currentLevel + ".png");
            bg.drawImage(numImg, 5, 5);
        } catch (Exception e) {
            bg.setColor(Color.YELLOW);
            
            bg.drawString(displayLevel, 5, 20); 
            
            if (currentLevel < 9) {
                bg.setFont(new Font("Arial", false, false, 12));
                bg.drawString("$" + nextCost, 5, 35);
            }
        }
        setImage(bg);
    }
}