import greenfoot.*;
import java.util.List;

public class Explosion extends SpriteAnimator {
    private GreenfootImage[] explosion;
    private List<Zombie> zombies;
    private boolean damageDealt = false;

    public Explosion(List<Zombie> zombies) {
        this.zombies = zombies;
        this.explosion = importSprites("spudow", 8);
        if (explosion != null && explosion.length > 0) {
            setImage(explosion[0]);
        }
    }

    @Override
    public void addedToWorld(World world) {
        if (world == null) return;
        
        applyExplosionDamage();
    }

    private void applyExplosionDamage() {
        if (damageDealt || zombies == null) return;

        int currentX = getX();
        for (int i = zombies.size() - 1; i >= 0; i--) {
            Zombie z = zombies.get(i);
            if (z != null && z.getWorld() != null) {
                try {
                    if (Math.abs(z.getX() - currentX) < 60) {
                        z.takeDmg(1800); 
                    }
                } catch (Exception e) {
                    continue;
                }
            }
        }
        damageDealt = true;
    }

    @Override
    public void act() {
        if (getWorld() == null) return;

        if (explosion != null && frame < explosion.length) {
            animate(explosion, 100L, false);
        } else {
            getWorld().removeObject(this);
        }
    }
}