import greenfoot.*;
import java.util.List;
import java.util.ArrayList;

public class Lawnmower extends Actor {
    private int speed = 0;
    private boolean isMoving = false;
    private boolean isManualTrigger = false;
    private final int ROW_THRESHOLD = 40;
    private List<Zombie> damagedZombies = new ArrayList<>();
    private int spawnX = -1;
    private int spawnY = -1;
    private boolean spawnSaved = false;
    private boolean used = false;

    public Lawnmower() {
        setImage("lawn_mower1.png");
    }

    public void act() {
        if (getWorld() == null) return;
        if (!spawnSaved) {
            spawnX = getX();
            spawnY = getY();
            spawnSaved = true;
        }
        if (isMoving) {
            handleMovement();
        } else {
            checkActivation();
        }
        handleBoundaries();
    }

    private void checkActivation() {
        if (Greenfoot.mousePressed(this)) {
            MouseInfo mouse = Greenfoot.getMouseInfo();
            if (mouse != null && mouse.getButton() == 1) {
                isManualTrigger = true;
                markUsed();
                startEngine();
                return;
            }
        }
        Zombie z = (Zombie) getOneIntersectingObject(Zombie.class);
        if (z != null && z.isLiving() && Math.abs(z.getY() - this.getY()) < ROW_THRESHOLD) {
            isManualTrigger = false;
            markUsed();
            startEngine();
        }
    }

    private void markUsed() {
        if (!used) {
            used = true;
            PlayScene ps = (PlayScene) getWorld();
            if (ps != null && ps.level != null) {
                ps.level.trackLawnmowerUsed(spawnX, spawnY);
            }
        }
    }

    private void startEngine() {
        setImage("lawn_mower2.png");
        speed = 8;
        isMoving = true;
    }

    private void handleMovement() {
        if (getWorld() == null) return;
        move(speed);
        List<Zombie> targets = getIntersectingObjects(Zombie.class);
        for (Zombie z : targets) {
            if (z != null && z.isLiving() && Math.abs(z.getY() - this.getY()) < ROW_THRESHOLD) {
                if (!damagedZombies.contains(z)) {
                    z.hit(9999);
                    damagedZombies.add(z);
                }
            }
        }
        damagedZombies.removeIf(z -> z.getWorld() == null || !intersects(z));
    }

    private void handleBoundaries() {
        if (getWorld() == null) return;
        if (getX() >= 990) {
            getWorld().removeObject(this);
        }
    }
}