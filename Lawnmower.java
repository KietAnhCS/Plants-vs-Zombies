import greenfoot.*;
import java.util.List;
import java.util.ArrayList;

public class Lawnmower extends Actor
{
    private int speed = 0;
    private boolean isMoving = false;
    
    private final int ROW_THRESHOLD = 40; 

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

    private void handleMovement()
    {
        if (getWorld() == null) return;
        
        move(speed);
        
        
        List<Zombie> targets = getIntersectingObjects(Zombie.class);
        
        for (Zombie z : targets) {
            
            if (z != null && z.getWorld() != null && Math.abs(z.getY() - this.getY()) < ROW_THRESHOLD) {
               
                z.takeDmg(9999); 
                
                if (z.getWorld() != null) {
                    getWorld().removeObject(z);
                }
            }
        }
    }

    private void checkActivation()
    {
    
        Zombie z = (Zombie) getOneIntersectingObject(Zombie.class);
        
        
        if (z != null && Math.abs(z.getY() - this.getY()) < ROW_THRESHOLD)
        {
            setImage("lawn_mower2.png"); 
            speed = 8; 
            isMoving = true;
        }
    }

    private void handleBoundaries()
    {
        if (getWorld() == null) return;

        if (getX() >= getWorld().getWidth() - 2) {
            getWorld().removeObject(this);
        }
    }
}