import greenfoot.*;

public class SettingsMenuPanel extends Actor {
    private int barWidth = 200; 
    public SettingsMenuPanel() {
        GreenfootImage bg = new GreenfootImage("setting.png");
        
        bg.setColor(Color.WHITE);
        bg.setFont(new Font("Arial", true, false, 28)); 
        
        int titleX = (bg.getWidth() / 2) - 130; 
        bg.drawString("MENU", titleX + 90, 50); 
        
        bg.setFont(new Font("Arial", true, false, 16)); 
        
        int sliderCenterX = bg.getWidth() / 2;
        int bgmY = 120; 
        int sfxY = 220; 
        
        int labelX = (sliderCenterX - (barWidth / 2)) - 120; 
        bg.drawString("MUSIC", labelX + 105, bgmY + 5); 
        bg.drawString("SOUND FX", labelX + 73, sfxY + 5);
        
        setImage(bg);
    }
}