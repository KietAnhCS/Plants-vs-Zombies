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
        
        if (name.toLowerCase().contains("lilypad")) {
            doneRechargeTime = false;
            recharged = false;
        } else {
            doneRechargeTime = true;
            recharged = true;
        }
        updateAppearance();
    }

    public void act() {
        if (name.toLowerCase().contains("lilypad")) {
            updateCooldown(); 
        } else if (!isUsed) {
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

        if (isUsed && !name.toLowerCase().contains("lilypad")) {
            setImage(imageDark);
            getImage().setTransparency(130);
            recharged = false; 
            return;
        }

        if (!doneRechargeTime && name.toLowerCase().contains("lilypad")) {
            GreenfootImage tempImage = new GreenfootImage(imageDark);
            rechargeOverlay.clear();
            rechargeOverlay.setColor(new Color(0, 0, 0, 150)); 
            
            double progress = (double)deltaTime / rechargeTime;
            int height = (int)(tempImage.getHeight() * (1.0 - progress));
            
            rechargeOverlay.fillRect(0, 0, tempImage.getWidth(), height);
            tempImage.drawImage(rechargeOverlay, 0, 0);

            setImage(tempImage);
            recharged = false; 
        } else {
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
    }

    public void startRecharge() {
        if (name.toLowerCase().contains("lilypad")) {
            this.lastFrame = System.currentTimeMillis();
            this.deltaTime = 0;
            this.doneRechargeTime = false;
            this.recharged = false; 
            this.isUsed = false; 
        } else {
            this.isUsed = true; 
            this.recharged = false;
        }
        updateAppearance();
    }

    public void setSelected(boolean bool) {
        this.selected = bool;
    }

    public TransparentObject addImage() { return null; }
    public Plant getPlant() { return null; }
}