import greenfoot.*; 
import java.util.ArrayList;

public class Explosion extends SpriteAnimator
{
    public GreenfootImage[] explosion;
    public ArrayList<Zombie> zombies;
    public Explosion(ArrayList<Zombie> zombies) {
        this.zombies = zombies;
        explosion = importSprites("spudow",8);
        
    }
    public void addedToWorld(World world) {
        AudioPlayer.play(80, "potato_mine.mp3");
        
        for (int i = zombies.size()-1; i >= 0; i--) {
            if (Math.abs(zombies.get(i).getX() - getX()) < 44) {
                zombies.get(i).takeDmg(150);
            } 
        }
       
        
    }
    public void act()
    {
        if (frame <= 8) {
            animate(explosion, 100L, false);
        } else {
            getWorld().removeObject(this);
            return;
            
        }
       
    }
}
