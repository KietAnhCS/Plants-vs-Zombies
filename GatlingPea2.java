import greenfoot.*;
import java.util.List;

public class GatlingPea2 extends Plant {
    private GreenfootImage[] idle;
    private GreenfootImage[] shoot;
    
    private int shootCount = 0;
    private boolean shooting = false;
    private boolean isPoweredUp = false;
    
    private long powerUpStartTime;
    private final long POWER_UP_DURATION = PlantRegistry.POWER_UP_DURATION;
    
    private long lastActionTime = System.currentTimeMillis();
    private long shootDelay = PlantRegistry.GATLING2_SHOOT_DELAY;
    private final long BURST_INTERVAL = PlantRegistry.GATLING2_BURST_INTERVAL;

    public GatlingPea2() {
        maxHp = PlantRegistry.GATLING2_HP;
        hp = maxHp;
        shoot = importSprites(PlantAssets.GATLING_PEA, 19);
        idle = importSprites(PlantAssets.GATLING_PEA, 19);
        setImage(idle[0]);
    }

    public void activatePlantFood() {
        this.isPoweredUp = true;
        this.powerUpStartTime = System.currentTimeMillis();
        this.hp = maxHp;
    }

    @Override
    public void hit(int dmg) {
        if (getWorld() != null && isLiving()) {
            hp -= dmg;
            hitFlash(shooting ? shoot : idle, PlantAssets.GATLING_PEA);
        }
    }

    @Override
    public void update() {
        if (getWorld() == null) return;
        
        playScene = (PlayScene) getWorld();
        updatePowerUpStatus();
        checkZombieInRow();
        handleAction();
    }

    private void updatePowerUpStatus() {
        if (isPoweredUp && (System.currentTimeMillis() - powerUpStartTime > POWER_UP_DURATION)) {
            isPoweredUp = false;
        }
    }

    private void handleAction() {
        long currentTime = System.currentTimeMillis();

        if (isPoweredUp) {
            animate(shoot, 40, false);
            if (currentTime - lastActionTime >= 50) {
                fireFirePea();
                lastActionTime = currentTime;
            }
            return;
        }

        if (shooting) {
            if (shootCount < PlantRegistry.GATLING2_BURST_COUNT) {
                animate(shoot, 100, false);
                if (currentTime - lastActionTime >= BURST_INTERVAL) {
                    fireFirePea();
                    shootCount++;
                    lastActionTime = currentTime;
                }
            } else {
                animate(idle, 225, true);
                if (currentTime - lastActionTime >= shootDelay) {
                    shootCount = 0;
                    lastActionTime = currentTime;
                }
            }
        } else {
            animate(idle, 225, true);
            shootCount = 0;
        }
    }

    private void fireFirePea() {
        if (getWorld() != null) {
            AudioManager.playSound(80, false, PlantAssets.SOUND_THROW, PlantAssets.SOUND_THROW2);
            getWorld().addObject(new FirePea(getYPos()), getX() + 25, getY() - 17);
        }
    }

    private void checkZombieInRow() {
        if (playScene == null || playScene.level == null) return;

        List<Zombie> rowZombies = playScene.level.zombieRow.get(getYPos());
        if (rowZombies == null || rowZombies.isEmpty()) {
            shooting = false;
            return;
        }

        shooting = rowZombies.stream().anyMatch(z -> 
            z.getWorld() != null && 
            z.getX() > getX() && 
            z.getX() <= playScene.getWidth() + 10
        );
    }
}