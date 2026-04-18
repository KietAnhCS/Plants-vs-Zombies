import greenfoot.*;  
public class IdleZombie extends animatedObject
{
    public void act()
    {
     
    }
    public void addedToWorld(World world) {
        frame = Random.Int(1,4);
    }
}
