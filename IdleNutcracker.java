import greenfoot.*; 

public class IdleNutcracker extends IdleZombie
{
    GreenfootImage[] idle;
    public IdleNutcracker() {
        idle = importSprites("Nutcracker Zombie_Idle", 20,0.45);
        if (idle != null && idle.length > 0) {
            setImage(idle[0]);
        }
    }
    public void act()
    {
        animate(idle, 250, true);
    }
}