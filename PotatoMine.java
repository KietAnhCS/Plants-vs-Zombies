import greenfoot.*;
import java.util.List;

public class PotatoMine extends Plant {
    private static final PlantType TYPE      = PlantType.POTATO_MINE;
    private static final long      ARMING_TIME   = TYPE.shootDelay; 
    private static final int        EXPLODE_RANGE = GameConstants.POTATO_EXPLOSION_RANGE;

    private GreenfootImage[] idle;
    private GreenfootImage[] arm;
    private boolean playSFX  = false;
    private long startTime;

    public PotatoMine() {
        setMaxHp(TYPE.hp);
        setHp(TYPE.hp);
        setDamage(TYPE.damage);
        setCost(TYPE.cost);
        
        this.idle = importSprites(PlantAssets.POTATO_IDLE, 5);
        this.arm  = importSprites(PlantAssets.POTATO_ARMING, 3);
        this.startTime = System.currentTimeMillis();
        
        if (arm != null && arm.length > 0) {
            setImage(arm[0]);
        }
        
        setState(PlantState.POTATO_ARMING);
    }

    @Override
    public void hit(int dmg) {
        if (getWorld() == null || !isLiving()) return;

        String assetPath = (getState() == PlantState.POTATO_ARMED) 
                           ? PlantAssets.POTATO_IDLE 
                           : PlantAssets.POTATO_ARMING;
        
        hitFlash(assetPath);
        
        setHp(getHp() - dmg);
        if (getHp() <= 0) onDeath();
    }

    @Override
    public void update() {
        if (getWorld() == null || isDragging) return;
        
        PlayScene playScene = (PlayScene) getWorld();

        if (getState() == PlantState.POTATO_ARMING && (System.currentTimeMillis() - startTime < ARMING_TIME)) {
            return; 
        }

        switch (getState()) {
            case POTATO_ARMING:
                if (!playSFX) {
                    AudioManager.getInstance().playSound(80, false, PlantAssets.SOUND_DIRT_RISE);
                    playSFX = true;
                }
                
                if (animate(arm, 200, false)) {
                    setState(PlantState.POTATO_ARMED);
                    setFrame(0);
                }
                break;

            case POTATO_ARMED:
                animate(idle, 200, true);
                checkExplosion(playScene);
                break;

            case POTATO_EXPLODING:
                executeExplosion(playScene);
                break;
        }
    }

    private void checkExplosion(PlayScene playScene) {
        if (playScene == null || playScene.level == null) return;
        
        List<Zombie> zombies = playScene.level.zombieRow.get(getYPos());
        if (zombies == null || zombies.isEmpty()) return;
        
        int currentX = getX();
        for (Zombie z : zombies) {
            if (z != null && z.getWorld() != null) {
                if (Math.abs(z.getX() - currentX) < EXPLODE_RANGE) {
                    setState(PlantState.POTATO_EXPLODING);
                    break;
                }
            }
        }
    }

    private void executeExplosion(PlayScene playScene) {
        World world = getWorld();
        if (world == null) return;

        Explosion explosionEffect = new Explosion();
        world.addObject(explosionEffect, getX(), getY() - 25);
        
        AudioManager.getInstance().playSound(90, false, PlantAssets.SOUND_POTATO_EXPLODE);

        if (playScene.GridManager != null) {
            playScene.GridManager.removePlant(getXPos(), getYPos());
        }
        
        world.removeObject(this);
    }
}