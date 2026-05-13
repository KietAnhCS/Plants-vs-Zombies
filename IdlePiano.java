import greenfoot.*; 

public class IdlePiano extends IdleZombie
{
    GreenfootImage[] idle;
    private static final int SCALE_WIDTH = 110;
    private static final int SCALE_HEIGHT = 110;
    public IdlePiano() {
        idle = importSprites("pianozombie", 11,0.45);
        for (GreenfootImage img : idle) {
            if (img != null) {
                img.scale(SCALE_WIDTH, SCALE_HEIGHT);
            }
        }
    }
    public void act()
    {
        animate(idle, 250, true);
    }
}