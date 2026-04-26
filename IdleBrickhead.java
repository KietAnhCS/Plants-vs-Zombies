import greenfoot.*; 
public class IdleBrickhead extends IdleZombie
{
    GreenfootImage[] idle;
    public IdleBrickhead() {
        idle = importSprites("brickheadidle", 4);
    }
    public void act()
    {
        animate(idle, 300, true);
       
    }
}
