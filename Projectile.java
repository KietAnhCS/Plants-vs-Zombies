import greenfoot.*;
import java.util.ArrayList;
import java.util.List;

public class Projectile extends SpriteAnimator {
    private int speed;
    private boolean hit = false;
    private int frameCount;
    private int yPos; 
    private int damage;
    private GreenfootImage[] projectileSprites;

    public Projectile(String name, int frameCount, int yPos, int dmg, int speed) {
        this.frameCount = frameCount;
        this.yPos = yPos;
        this.damage = dmg;
        this.speed = speed;
        
        this.projectileSprites = importSprites(name, frameCount);
        
        if (projectileSprites != null && projectileSprites.length > 0) {
            setImage(projectileSprites[0]);
        }
    }

    @Override
    public void update() {
        PlayScene world = (PlayScene) getWorld();
        if (world == null) return;

        if (!world.getObjects(Overlay.class).isEmpty()) return;

        if (hit) {
            handleHitAnimation(world);
            return;
        }

        move((double) speed);

        if (isAtEdge()) {
            world.removeObject(this);
            return;
        }

        checkCollision(world);
    }

    private void handleHitAnimation(PlayScene world) {
        if (animate(projectileSprites, 50, false)) {
            world.removeObject(this);
        }
    }

    private void checkCollision(PlayScene world) {
        if (world.level == null || world.level.zombieRow == null) return;
        if (yPos < 0 || yPos >= world.level.zombieRow.size()) return;

        List<Zombie> row = world.level.zombieRow.get(yPos);
        if (row == null || row.isEmpty()) return;

        for (Zombie z : new ArrayList<>(row)) {
            if (getWorld() == null) return;
            if (z == null || !z.isLiving()) continue;

            if (Math.abs(z.getX() - getX()) < 30) {
                z.hit(damage);
                this.hit = true;
                this.frame = 0;
                break;
            }
        }
    }
}