import greenfoot.*; 
public class IdleCone extends IdleZombie
{
    GreenfootImage[] idle;
    public IdleCone() {
        idle = importSprites("coneheadidle", 4);
    }
    public void act()
    {
        animate(idle, 200, true);
        
    }
}
