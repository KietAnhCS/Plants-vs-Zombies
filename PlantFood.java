import greenfoot.*;

public class PlantFood extends SmoothMover
{
    public boolean selected = false;
    
    private long rechargeTime = 20000; 
    private long lastUsedTime;
    private long deltaTime;
    private boolean recharged = true;

    private GreenfootImage imageBright;
    private GreenfootImage imageDark;
    private GreenfootImage rechargeOverlay;

    public void addedToWorld(World world) {
        imageBright = new GreenfootImage("plantfood.png");
        imageDark = new GreenfootImage("plantfood.png");
        imageDark.setTransparency(100); 
        
        rechargeOverlay = new GreenfootImage(imageBright.getWidth(), imageBright.getHeight());
        
        setImage(imageBright);
        selected = false;
        recharged = true;
    }

    public void act() {
        updateCooldown();
        updateAppearance();
        handleMouse();
    }

    private void updateCooldown() {
        if (!recharged) {
            deltaTime = System.currentTimeMillis() - lastUsedTime;
            if (deltaTime >= rechargeTime) {
                recharged = true;
            }
        }
    }

    private void updateAppearance() {
        if (!recharged) {
            GreenfootImage tempImage = new GreenfootImage(imageDark);
            rechargeOverlay.clear();
            rechargeOverlay.setColor(new Color(0, 0, 0, 150));
            
            double progress = (double)deltaTime / rechargeTime;
            int height = (int)(tempImage.getHeight() * (1.0 - progress));
            
            rechargeOverlay.fillRect(0, 0, tempImage.getWidth(), height);
            tempImage.drawImage(rechargeOverlay, 0, 0);
            setImage(tempImage);
        } else {
            setImage(imageBright);
        }
    }

    private void handleMouse() {
        if (!recharged) return;

        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse != null) {
            if (Greenfoot.mouseClicked(null)) {
                PlayScene myWorld = (PlayScene)getWorld();
                myWorld.moveHitbox(); 
                
                if (intersects(myWorld.hitbox)) {
                    if (!selected) {
                        selected = true;
                        myWorld.addObject(new clickPlantFood(this), mouse.getX(), mouse.getY());
                    }
                }
            }
        }
    }

    public void usePlantFood() {
        this.recharged = false;
        this.lastUsedTime = System.currentTimeMillis();
        this.selected = false;
        this.deltaTime = 0;
    }

    public void setSelected(boolean bool) {
        selected = bool;
    }
}