import greenfoot.*;
import java.util.List;

public class Explosion extends SpriteAnimator {
    private GreenfootImage[] explosion;
    private boolean damageDealt = false;

    public Explosion() {
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
        if (damageDealt) return;

        List<Zombie> zombiesInRange = getObjectsInRange(80, Zombie.class);
        
        for (Zombie z : zombiesInRange) {
            if (z != null && z.getWorld() != null) {
                z.hit(1800); 
            }
        }
        damageDealt = true;
    }

    @Override
    public void act() {
        if (getWorld() == null) return;

        if (explosion != null && frame < explosion.length) {
            if (animate(explosion, 100L, false)) {
                getWorld().removeObject(this);
            }
        } else {
            getWorld().removeObject(this);
        }
    }
}