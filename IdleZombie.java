import greenfoot.*;  
public class IdleZombie extends SpriteAnimator
{
    public void act()
    {
     
    }
    public void addedToWorld(World world) {
        frame = Random.Int(1,4);
    }
}
