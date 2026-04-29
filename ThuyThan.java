import greenfoot.*;
import java.util.List;

public class ThuyThan extends Actor {
    private int targetX, targetY;
    private boolean isMoving = false;
    private int speed = 10; 
    private int pickupRange = 80; 

    private GreenfootImage imageRight;
    private GreenfootImage imageLeft;

    public ThuyThan() {
        imageRight = new GreenfootImage("thuythan.png");
        imageRight.scale(80, 80);
        
        imageLeft = new GreenfootImage(imageRight);
        imageLeft.mirrorHorizontally();
        
        setImage(imageRight);
    }

    public void act() {
        handleRightClick();
        moveToTarget();
        autoCollectObjects(); 
    }

    private void handleRightClick() {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse != null && Greenfoot.mousePressed(null) && mouse.getButton() == 3) {
            targetX = mouse.getX();
            targetY = mouse.getY();
            isMoving = true;
            
            if (targetX < getX()) {
                setImage(imageLeft);
            } else {
                setImage(imageRight);
            }
        }
    }

    private void moveToTarget() {
        if (isMoving) {
            int curX = getX();
            int curY = getY();
            double distance = Math.hypot(targetX - curX, targetY - curY);

            if (distance > speed) {
                double dx = (targetX - curX) / distance;
                double dy = (targetY - curY) / distance;
                
                int nextX = curX + (int)(dx * speed);
                int nextY = curY + (int)(dy * speed);
                
                setLocation(nextX, nextY);
            } else {
                setLocation(targetX, targetY);
                isMoving = false;
            }
        }
    }

    private void autoCollectObjects() {
        List<Sun> suns = getObjectsInRange(pickupRange, Sun.class);
        for (Sun s : suns) {
            if (s.getWorld() != null) {
                s.collect(); 
            }
        }

        List<FallingSun> fallingSuns = getObjectsInRange(pickupRange, FallingSun.class);
        for (FallingSun fs : fallingSuns) {
            if (fs.getWorld() != null) {
                fs.collectSun();
            }
        }
    }
}