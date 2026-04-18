import greenfoot.*;

public class Setting extends Actor {
    public Setting() {
        
        setImage(new GreenfootImage("setting.png"));
    }

    public void act() {
        
        if ("escape".equals(Greenfoot.getKey())) {
            getWorld().removeObject(this);
        }
    }
}