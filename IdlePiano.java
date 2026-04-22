import greenfoot.*; 


public class Basic extends IdleZombie
{
    
    GreenfootImage[] idle;
    public Basic() {
        idle = importSprites("zombieidle", 4);
        
    }
    
    public void act()
    {
        animate(idle, 250, true);
       
    }
}
