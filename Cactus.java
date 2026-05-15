import greenfoot.*;
import java.util.List;

public class Cactus extends Plant {
    private static final PlantType TYPE = PlantType.CACTUS;
    private GreenfootImage[] idle;
    private GreenfootImage[] shoot;
    private long lastShootTime = System.currentTimeMillis();
    private boolean shootOnce = false;

    public Cactus() {
        setMaxHp(TYPE.hp);
        setHp(TYPE.hp);
        setDamage(TYPE.damage);
        setCost(TYPE.cost);
        
        shoot = importSprites(PlantAssets.CACTUS_SHOOT, 2);
        idle  = importSprites(PlantAssets.CACTUS_IDLE,  4);
        
        if (idle != null && idle.length > 0) {
            setImage(idle[0]);
        }
    }

    @Override
    public void hit(int dmg) {
        if (getWorld() == null || !isLiving()) return;

        String assetPath = (getState() == PlantState.SHOOTING) 
                           ? PlantAssets.CACTUS_SHOOT 
                           : PlantAssets.CACTUS_IDLE;
        
        hitFlash(assetPath);
        
        setHp(getHp() - dmg);
        if (getHp() <= 0) {
            onDeath();
        }
    }

    @Override
    public void update() {
        if (getWorld() == null) return;
        
        checkZombieInRow();
        handleAction();
    }

    private void handleAction() {
        if (getState() == PlantState.MERGING) return;

        long currentTime = System.currentTimeMillis();
        
        if (getState() == PlantState.IDLE) {
            animate(idle, 150, true);
            shootOnce = false;
            return;
        }

        if (currentTime - lastShootTime >= TYPE.shootDelay) {
            animate(shoot, 150, false);
            
            if (!shootOnce && frame >= 1) {
                fireNeedle();
                shootOnce = true;
            }
            
            if (frame >= shoot.length - 1) {
                lastShootTime = currentTime;
                shootOnce = false;
            }
        } else {
            animate(idle, 150, true);
        }
    }

    private void fireNeedle() {
        if (getWorld() == null) return;
        AudioManager.getInstance().playSound(80, false, PlantAssets.SOUND_THROW);
        getWorld().addObject(new Needle(getYPos()), getX() + 30, getY() - 8);
    }

    private void checkZombieInRow() {
        if (getState() == PlantState.MERGING) return;

        PlayScene playScene = (PlayScene) getWorld();
        if (playScene == null || playScene.level == null) return;
        
        int row = getYPos();
        if (row == -1) return;

        List<Zombie> rowZombies = playScene.level.zombieRow.get(row);
        if (rowZombies == null || rowZombies.isEmpty()) {
            setState(PlantState.IDLE);
            return;
        }

        boolean hasTarget = rowZombies.stream().anyMatch(z ->
            z.getWorld() != null &&
            z.getX() > getX() &&
            z.getX() <= playScene.getWidth() + 10
        );

        if (hasTarget) {
            if (getState() == PlantState.IDLE) {
                setState(PlantState.SHOOTING);
            }
        } else {
            setState(PlantState.IDLE);
        }
    }

    @Override
    public String getPlantName() {
        return TYPE.name();
    }
}