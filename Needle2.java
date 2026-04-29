import greenfoot.*;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

public class Needle2 extends Projectile {
    private Set<Zombie> hitZombies = new HashSet<>();
    private int pierceDamage = PlantRegistry.NEEDLE_PIERCE_DAMAGE;
    private int targetY;
    private int speed = 6;
    private int verticalStep;

    public Needle2(int yPos, int offset) {
        super("needle", 2, yPos, PlantRegistry.NEEDLE_PIERCE_DAMAGE, 6); 
        this.targetY = yPos + offset;
        this.verticalStep = (offset > 0) ? 2 : -2; 
        if (offset == 0) this.verticalStep = 0; 
    }

    @Override
    public void act() {
        if (getWorld() == null) return;

        int nextX = getX() + speed;
        int nextY = getY();

        if (verticalStep > 0 && getY() < targetY) {
            nextY += verticalStep;
        } else if (verticalStep < 0 && getY() > targetY) {
            nextY += verticalStep;
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