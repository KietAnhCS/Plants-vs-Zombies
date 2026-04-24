import greenfoot.*;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

public class Needle extends Projectile
{
    private Set<Zombie> hitZombies = new HashSet<>();
    private int pierceDamage = 15;

    public Needle(int yPos) {
        
        super("needle", 2, yPos, 10, 6);
    }

    @Override
    public void act() {
       
        setLocation(getX() + 6, getY());
        
        checkPiercingCollision();
        
        if (isAtEdge()) {
            getWorld().removeObject(this);
        }
    }

    private void checkPiercingCollision() {
        
        List<Zombie> zombies = getIntersectingObjects(Zombie.class);
        
        for (Zombie z : zombies) {
           
            if (!hitZombies.contains(z)) {
                z.hit(pierceDamage); 
                hitZombies.add(z);  
                
            }
        }
    }
}