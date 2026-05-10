import greenfoot.*;
import java.util.List;

public class GatlingPea extends Plant {
    private static final PlantType TYPE = PlantType.GATLING_PEA;
    private GreenfootImage[] idle, shoot;
    private boolean adjusted = false;
    private long lastAttackTime = System.currentTimeMillis();
    private PlayScene cachedPlayScene;
    
    private int burstCount = 0;
    private long lastBurstTime = 0;
    private static final int BURST_INTERVAL = 100;

    public GatlingPea() {
        setMaxHp(TYPE.hp);
        setHp(TYPE.hp);
        setDamage(TYPE.damage);
        setCost(TYPE.cost);
        
        idle = importSprites(PlantAssets.GATLING_PEA, 19);
        shoot = importSprites(PlantAssets.GATLING_PEA, 19);
        
        if (idle != null && idle.length > 0) setImage(idle[0]);
    }

    @Override
    public void addedToWorld(World world) {
        super.addedToWorld(world);
        if (world instanceof PlayScene) cachedPlayScene = (PlayScene) world;
        world.addObject(new HealthBar(this, 50), getX(), getY());
    }

    @Override
    public void hit(int dmg) {
        if (getWorld() == null || !isLiving()) return;
        
        hitFlash(PlantAssets.GATLING_PEA);
        
        setHp(getHp() - dmg);
        if (getHp() <= 0) onDeath();
    }

    @Override
    public void update() {
        if (getWorld() == null) return;
        if (!adjusted) {
            setLocation(getX(), getY() - 15);
            adjusted = true;
        }
        handleCombat();
    }

    private void handleCombat() {
        if (getState() == PlantState.MERGING) return;

        boolean hasTarget = checkZombieInRow();
        
        if (hasTarget || burstCount > 0) {
            setState(PlantState.SHOOTING);
            animate(shoot, 60, false);
            executeShoot();
        } else {
            setState(PlantState.IDLE);
            animate(idle, 60, true);
        }
    }

    private boolean checkZombieInRow() {
        int myRow = getYPos();
        if (myRow == -1 || cachedPlayScene == null || cachedPlayScene.level == null) return false;
        List<Zombie> rowZombies = cachedPlayScene.level.zombieRow.get(myRow);
        return rowZombies.stream().anyMatch(z ->
            z.getWorld() != null &&
            z.getX() > getX() &&
            z.getX() <= cachedPlayScene.getWidth() + 10
        );
    }

    private void executeShoot() {
        long currentTime = System.currentTimeMillis();

        if (burstCount == 0) {
            if (currentTime - lastAttackTime >= TYPE.shootDelay) {
                burstCount = 4;
                lastAttackTime = currentTime;
            }
        }

        if (burstCount > 0 && currentTime - lastBurstTime >= BURST_INTERVAL) {
            AudioManager.getInstance().playSound(80, false, PlantAssets.SOUND_THROW);
            getWorld().addObject(new Pea(getYPos()), getX() + 25, getY() - 17);
            
            burstCount--;
            lastBurstTime = currentTime;
        }
    }
}