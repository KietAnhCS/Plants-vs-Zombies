import greenfoot.*; 

public class IdleExcavator extends IdleZombie
{
    GreenfootImage[] idle;
    public IdleExcavator() {
        idle = importSprites("Excavator Zombie_Idle", 20,0.45);
        if (idle != null && idle.length > 0) {
            setImage(idle[0]);
        }
    }
    public void act()
    {
        animate(idle, 250, true);
    }
}