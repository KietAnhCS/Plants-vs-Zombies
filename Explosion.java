import greenfoot.*;
import java.util.List;

public class Explosion extends SpriteAnimator {
    
    private GreenfootImage[] explosionSprites;
    private List<Zombie> targetZombies;
    private boolean damageDealt = false;

    public Explosion(List<Zombie> zombies) {
        this.targetZombies = zombies;
        explosionSprites = importSprites("spudow", 8);
        
        if (explosionSprites != null && explosionSprites.length > 0) {
            setImage(explosionSprites[0]);
        }
    }

    @Override
    public void addedToWorld(World world) {
        AudioManager.getInstance().playSound(80, false, PlantAssets.SOUND_POTATO_EXPLODE);
        
        dealExplosionDamage();
    }

    private void dealExplosionDamage() {
        if (targetZombies == null || damageDealt) return;

        for (int i = targetZombies.size() - 1; i >= 0; i--) {
            Zombie z = targetZombies.get(i);
            if (z != null && z.getWorld() != null) {
                if (Math.abs(z.getX() - getX()) < 50) {
                    z.hit(PlantType.POTATOMINE.damage); 
                }
            }
        }
        damageDealt = true;
    }

    public void act() {
        animate(explosionSprites, 60, false);

        if (frame >= explosionSprites.length - 1) {
            if (getWorld() != null) {
                getWorld().removeObject(this);
            }
        }
    }
}