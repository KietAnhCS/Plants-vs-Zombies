import greenfoot.*;

public class PlantFood extends PhysicsBody
{
    public boolean selected = false;
    private long rechargeTime = 20000; 
    private long lastUsedTime;
    private long deltaTime;
    private boolean recharged = true;

    private GreenfootImage imageBright;
    private GreenfootImage imageDark;

    public void addedToWorld(World world) {
        imageBright = new GreenfootImage("plantfood.png");
        imageDark = new GreenfootImage("plantfood.png");
        imageDark.setTransparency(100); 
        
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
                setImage(imageBright);
            }
        }
    }

    private void updateAppearance() {
        if (!recharged) {
           
            GreenfootImage base = new GreenfootImage(imageDark);
            
            double progress = (double)deltaTime / rechargeTime;
            if (progress > 1.0) progress = 1.0;
            
            int overlayHeight = (int)(base.getHeight() * (1.0 - progress));
            
            if (overlayHeight > 0) {
                GreenfootImage overlay = new GreenfootImage(base.getWidth(), overlayHeight);
                overlay.setColor(new Color(0, 0, 0, 180));
                overlay.fill();
                
                base.drawImage(overlay, 0, 0);
            }
            
            setImage(base);
        }
    }

    private void handleMouse() {
        if (!recharged || selected) return;

        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse != null) {
            if (Greenfoot.mouseClicked(null)) {
                PlayScene myWorld = (PlayScene)getWorld();
                myWorld.moveHitbox(); 
                
                if (intersects(myWorld.hitbox)) {
                    SunCounter sc = (SunCounter)myWorld.getObjects(SunCounter.class).get(0);
                    if (sc != null && sc.sun >= 500) {
                        selected = true;
                        myWorld.addObject(new clickPlantFood(this), mouse.getX(), mouse.getY());
                    }
                }
            }
        }
    }

    public void usePlantFood() {
        PlayScene myWorld = (PlayScene)getWorld();
        if (myWorld != null) {
            java.util.List<SunCounter> counters = myWorld.getObjects(SunCounter.class);
            if (!counters.isEmpty()) {
                SunCounter sc = counters.get(0);
                sc.sun -= 500;
                sc.updateText();
            }
        }
        this.recharged = false;
        this.lastUsedTime = System.currentTimeMillis();
        this.selected = false;
        this.deltaTime = 0;
    }

    public void setSelected(boolean bool) {
        selected = bool;
    }
}