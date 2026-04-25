import greenfoot.*;

public class SeedPacket extends Actor
{
    public long deltaTime;
    public long rechargeTime; 
    public long lastFrame; 
    
    public int sunCost;
    public String name;
    
    public boolean recharged = false; 
    public boolean selected = false;  
    
    private GreenfootImage imageBright; 
    private GreenfootImage imageDark;   
    private GreenfootImage rechargeOverlay; 
    
    private PlayScene PlayScene;
    private boolean doneRechargeTime = false;

    public boolean isUsed = false; 

    public SeedPacket(long rechargeTime, boolean recharged, int sunCost, String name) {
        this.rechargeTime = rechargeTime;
        this.recharged = recharged;
        this.sunCost = sunCost;
        this.name = name;
        
        this.imageBright = new GreenfootImage(name + "1.png"); 
        this.imageDark = new GreenfootImage(name + "2.png");   
        
        this.lastFrame = System.currentTimeMillis();
        setImage(imageDark);
    }

    public void addedToWorld(World world) {
        PlayScene = (PlayScene)world;
        rechargeOverlay = new GreenfootImage(imageBright.getWidth(), imageBright.getHeight());
        doneRechargeTime = true;
        recharged = true;
        
        updateAppearance();
    }

    public void act() {
        if (!isUsed) {
            doneRechargeTime = true;
            recharged = true;
        }
        updateAppearance();
    }

    private void updateCooldown() {
        if (!doneRechargeTime) {
            deltaTime = System.currentTimeMillis() - lastFrame;
            if (deltaTime >= rechargeTime) {
                doneRechargeTime = true;
                recharged = true; 
            }
        }
    }

    public void updateAppearance() {
        if (PlayScene == null || PlayScene.seedbank == null) return;

        if (isUsed) {
            setImage(imageDark);
            getImage().setTransparency(130);
            recharged = false; 
            return;
        }

        int currentSun = PlayScene.seedbank.sunCounter.sun; 
        if (currentSun < sunCost || selected) {
            setImage(imageDark);
            recharged = (currentSun >= sunCost); 
        } else {
            setImage(imageBright);
            getImage().setTransparency(255); 
            recharged = true;
        }
    }

    public void startRecharge() {
        
        this.isUsed = true; 
        this.recharged = false;
        updateAppearance();
    }

    public void setSelected(boolean bool) {
        this.selected = bool;
    }

    public TransparentObject addImage() { return null; }
    public Plant getPlant() { return null; }
}