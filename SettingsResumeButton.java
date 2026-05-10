import greenfoot.*;

public class SettingsResumeButton extends Button {
    
    public SettingsResumeButton() {
        super("resume1.png", "resume2.png"); 
        
        
        double scaleFactor = 0.18; 
        
        if (this.idle != null) {
            this.idle.scale((int)(this.idle.getWidth() * scaleFactor), (int)(this.idle.getHeight() * scaleFactor));
        }
        if (this.hover != null) {
            this.hover.scale((int)(this.hover.getWidth() * scaleFactor), (int)(this.hover.getHeight() * scaleFactor));
        }
        
        setImage(this.idle);
    }

    @Override
    public void addedToWorld(World world) {
        addTextToImage(this.idle);
        addTextToImage(this.hover);
        
        setImage(this.idle);
    }
    
    private void addTextToImage(GreenfootImage img) {
        if (img != null) {
            img.setColor(Color.WHITE);
            img.setFont(new Font("Arial", true, false, 24)); 
            
            int textX = (img.getWidth() / 2) - 60; 
            int textY = (img.getHeight() / 2) + 10;
            
        }
    }
    
    @Override
    protected void onClick() {
        World currentWorld = getWorld();
        
        if (currentWorld instanceof Arena) {
            ((Arena)currentWorld).closeSettingsMenu();
        } else if (currentWorld instanceof PlayScene) {
            ((PlayScene)currentWorld).closeSettingsMenu();
        }
    }
}