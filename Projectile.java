import greenfoot.*;
import java.util.ArrayList;
import java.util.List;

public class Projectile extends SpriteAnimator {
    private int speed;
    private boolean hit = false;
    private boolean foundTarget = false;
    private Zombie hitZombie;
    private int frameCount;
    private int yPos; 
    private int damage;
    private GreenfootImage[] image;

    public Projectile(String name, int frameCount, int yPos, int dmg, int speed) {
        this.frameCount = frameCount;
        this.image = importSprites(name, frameCount);
        this.yPos = yPos;
        this.damage = dmg;
        this.speed = speed;
        if (image != null && image.length > 0) {
            setImage(image[0]);
        }
    }

    public void act() {
        
        PlayScene world = (PlayScene) getWorld();
        if (world == null) return;

        if (!world.getObjects(Overlay.class).isEmpty()) {
            return; 
        }
        if (hit) {
            handleHitAnimation(world);
            return;
        }

        move(speed);

        if (isAtEdge()) {
            world.removeObject(this);
            return;
        }

        checkCollision(world);
    }

    private void handleHitAnimation(PlayScene world) {
        
        if (frame >= frameCount - 1) {
            world.removeObject(this);
        } else {
            animate(image, 150, false);
        }
    }

    private void checkCollision(PlayScene world) {
        
        if (world.level == null || world.level.zombieRow == null) return;

        if (yPos < 0 || yPos >= world.level.zombieRow.size()) {
            return; 
        }

        List<Zombie> row = world.level.zombieRow.get(yPos);
        if (row == null || row.isEmpty()) return;

        for (Zombie z : new ArrayList<>(row)) {
            if (getWorld() == null) return;
            if (z == null || z.getWorld() == null) continue;

            if (Math.abs(z.getX() - getX()) < 30) {
                if (!hit) {
                    z.hit(damage);
                    this.hit = true;
                    this.frame = 0;
                }
                break;
            }
        }
    }

    public int getYPos() {
        return this.yPos;
    }
}