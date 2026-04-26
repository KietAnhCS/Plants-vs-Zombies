import greenfoot.*;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

public class Needle3 extends Projectile
{
    private Set<Zombie> hitZombies = new HashSet<>();
    private int pierceDamage = 10;
    private int targetY;
    private int speed = 6;
    private int verticalStep;

    public Needle3(int yPos, int offset) {
        super("needle", 2, yPos, 10, 6);
        this.targetY = yPos + offset;
        this.verticalStep = (offset > 0) ? 1 : -1;
    }

    @Override
    public void act() {
        if (getWorld() == null) return;

        int nextX = getX() + speed;
        int nextY = getY();

        if (verticalStep > 0 && getY() < targetY) {
            nextY += 2;
        } else if (verticalStep < 0 && getY() > targetY) {
            nextY -= 2;
        }
        
        setLocation(nextX, nextY);
        checkPiercingCollision();
        
        if (isAtEdge()) {
            getWorld().removeObject(this);
        }
    }

    private void checkPiercingCollision() {
        if (getWorld() == null) return;
        List<Zombie> zombies = getIntersectingObjects(Zombie.class);
        for (Zombie z : zombies) {
            if (!hitZombies.contains(z)) {
                z.hit(pierceDamage); 
                hitZombies.add(z);  
            }
        }
    }
}