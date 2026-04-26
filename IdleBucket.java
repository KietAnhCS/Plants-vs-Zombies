import greenfoot.*; 
public class IdleBucket extends IdleZombie
{
    GreenfootImage[] idle;
    public IdleBucket() {
        idle = importSprites("bucketheadidle", 4);
    }
    public void act()
    {
        animate(idle, 300, true);
       
    }
}
