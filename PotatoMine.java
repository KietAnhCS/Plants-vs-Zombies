import greenfoot.*;
import java.util.List;

public class PotatoMine extends Plant {
    private GreenfootImage[] idle;
    private GreenfootImage[] arm;
    
    private boolean playOnce = false;
    public boolean armed = false;
    private boolean playSFX = false;
    
    private long startTime;
    private final long ARMING_TIME = PlantRegistry.POTATO_ARMING_TIME;

    public PotatoMine() {
        idle = importSprites(PlantAssets.POTATO_IDLE, 5);
        arm = importSprites(PlantAssets.POTATO_ARMING, 3);
        maxHp = PlantRegistry.POTATO_HP;
        hp = maxHp;
        startTime = System.currentTimeMillis();
        
        if (arm != null && arm.length > 0) {
            setImage(arm[0]);
        }
    }

    @Override
    public void update() {
        if (getWorld() == null || isMerging || isDragging) return;
        playScene = (PlayScene) getWorld();

        long elapsed = System.currentTimeMillis() - startTime;

        if (elapsed < ARMING_TIME) {
            return; 
        }

        if (!armed) {
            if (!playSFX) {
                AudioManager.playSound(80, false, PlantAssets.SOUND_DIRT_RISE);
                playSFX = true;
            }
            
            animate(arm, 200, false);
            
            if (frame >= arm.length - 1) {
                armed = true;
            }
        } else {
            animate(idle, 200, true);
            checkExplosion();
        }
    }

    @Override
    public void hit(int dmg) {
        if (getWorld() == null) return;
        if (isLiving() && !armed) {
            hp -= dmg;
            hitFlash(arm, PlantAssets.POTATO_ARMING);
        }
    }

    public void checkExplosion() {
        if (getWorld() == null || playScene == null || playScene.level == null) return;

        List<Zombie> zombies = playScene.level.zombieRow.get(getYPos());
        if (zombies == null || zombies.isEmpty()) return;

        for (Zombie i : zombies) {
            if (i != null && i.getWorld() != null) {
                if (Math.abs(i.getX() - getX()) < PlantRegistry.POTATO_EXPLOSION_RANGE) {
                    explode();
                    return;
                }
            }
        }
    }

    private void explode() {
        World world = getWorld();
        if (world == null) return;
        
        world.addObject(new Explosion(playScene.level.zombieRow.get(getYPos())), getX(), getY() - 25);
        AudioManager.playSound(90, false, PlantAssets.SOUND_POTATO_EXPLODE);
        
        if (playScene != null && playScene.GridManager != null) {
            playScene.GridManager.removePlant(getXPos(), getYPos());
        }
        
        world.removeObject(this);
    }
}