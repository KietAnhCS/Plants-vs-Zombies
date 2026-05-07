import greenfoot.*;
public class IdleRa extends IdleZombie
{
    GreenfootImage[] idle;
    private static final int SCALE_WIDTH = 100;
    private static final int SCALE_HEIGHT = 100;
    public IdleRa() {
        idle = importSprites("Ra_Idle", 39);
        for (GreenfootImage img : idle) {
            if (img != null) {
                img.scale(SCALE_WIDTH, SCALE_HEIGHT);
            }
        }
    }
    public void act()
    {
        animate(idle, 200, true);
        
    }
}
