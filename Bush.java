import greenfoot.*; 


public class Bush extends animatedObject
{
    
    GreenfootImage[] bush;
    public Bush() {
        bush = importSprites("bush",3);
    }
    public void act()
    {
        animate(bush, 300, true);
        
    }
}
