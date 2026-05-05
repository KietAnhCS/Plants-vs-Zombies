import greenfoot.*; 


public class IdlePiano extends IdleZombie
{
    
    GreenfootImage[] idle;
    public IdlePiano() {
        idle = importSprites("pianozombie", 11);
        
    }
    
    public void act()
    {
        animate(idle, 250, true);
       
    }
}
