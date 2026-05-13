import greenfoot.*;
import java.util.List;
import java.util.ArrayList;

public class Lawnmower extends Actor
{
    private int speed = 0;
    private boolean isMoving = false;
    private boolean isManualTrigger = false;
    private final int ROW_THRESHOLD = 40; 
    private List<Zombie> damagedZombies = new ArrayList<>();

    public Lawnmower()
    {
        setImage("lawn_mower1.png"); 
    }

    public void act()
    {
        if (getWorld() == null) return;

        if (isMoving) {
            handleMovement();
        } else {
            checkActivation();
        }

        handleBoundaries();
    }

    private void checkActivation()
    {
        if (Greenfoot.mousePressed(this)) 
        {
            MouseInfo mouse = Greenfoot.getMouseInfo();
            if (mouse != null && mouse.getButton() == 1) 
            {
                isManualTrigger = true;
                startEngine();
                return;
            }
        }

        Zombie z = (Zombie) getOneIntersectingObject(Zombie.class);
        if (z != null && z.isLiving() && Math.abs(z.getY() - this.getY()) < ROW_THRESHOLD)
        {
            isManualTrigger = false;
            startEngine();
        }
    }

    private void startEngine() {
        setImage("lawn_mower2.png"); 
        speed = 8; 
        isMoving = true;
    }

    private void handleMovement()
    {
        if (getWorld() == null) return;
        move(speed);
        
        List<Zombie> targets = getIntersectingObjects(Zombie.class);
        
        for (Zombie z : targets) {
            if (z != null && z.isLiving() && Math.abs(z.getY() - this.getY()) < ROW_THRESHOLD) {
                if (!damagedZombies.contains(z)) {
                    if (isManualTrigger) {
                        z.hit(9999);
                    } else {
                        z.hit(9999);
                    }
                    damagedZombies.add(z);
                }
            }
        }
        
        damagedZombies.removeIf(z -> z.getWorld() == null || !intersects(z));
    }

    private void handleBoundaries()
    {
        if (getWorld() == null) return;
        
        if (getX() >= 990) {
            getWorld().removeObject(this);
        }
    }
}