import greenfoot.*; 
public class Dirt extends animatedObject
{
    public GreenfootImage[] dirt;
    
    public Dirt() {
        dirt = importSprites("dirt",4);
    }
    public void act()
    {
        if (frame <= 3) {
            animate(dirt, 50L, false);
        } else {
            getWorld().removeObject(this);
            return;
            
        }
        
    }
}
