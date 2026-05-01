import greenfoot.*;
import java.util.List;

public class ThuyThan extends PhysicsBody {
    private static final int SPEED        = 10;
    private static final int PICKUP_RANGE = 80;

    private final GreenfootImage imageRight;
    private final GreenfootImage imageLeft;

    private int     targetX, targetY;
    private boolean isMoving = false;

    public ThuyThan() {
        imageRight = new GreenfootImage("thuythan.png");
        imageRight.scale(80, 80);
        imageLeft  = new GreenfootImage(imageRight);
        imageLeft.mirrorHorizontally();
        setImage(imageRight);
    }

    @Override
    public void act() {
        handleRightClick();
        moveToTarget();
        autoCollect();
    }

    private void handleRightClick() {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse == null || !Greenfoot.mousePressed(null) || mouse.getButton() != 3) return;
        targetX  = mouse.getX();
        targetY  = mouse.getY();
        isMoving = true;
        setImage(targetX < getX() ? imageLeft : imageRight);
    }

    private void moveToTarget() {
        if (!isMoving) return;
        double distance = Math.hypot(targetX - getX(), targetY - getY());
        if (distance > SPEED) {
            setLocation(
                getX() + (targetX - getX()) / distance * SPEED,
                getY() + (targetY - getY()) / distance * SPEED
            );
        } else {
            setLocation(targetX, targetY);
            isMoving = false;
        }
    }

    private void autoCollect() {
        for (Sun s : getObjectsInRange(PICKUP_RANGE, Sun.class)) {
            if (s.getWorld() != null) s.collect();
        }
        for (FallingSun fs : getObjectsInRange(PICKUP_RANGE, FallingSun.class)) {
            if (fs.getWorld() != null) fs.collectSun();
        }
    }
}