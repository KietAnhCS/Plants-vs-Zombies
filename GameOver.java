import greenfoot.*;  

public class GameOver extends World
{
    Hitbox hitbox = new Hitbox();
    
    public GameOver(World restart)
    {    
        super(1111, 602, 1, false); 
        
        addObject(hitbox,0,0);
        Greenfoot.setSpeed(50);
        addObject(new Retry(restart), 365, 395);
    }
    public void act() {
        
    }
    public void moveHitbox() {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse != null) {
            hitbox.setLocation(mouse.getX(), mouse.getY());
        }
    }
}
