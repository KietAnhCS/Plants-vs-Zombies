import greenfoot.*;
import java.util.List;

public class Cactus3 extends Plant {
    private GreenfootImage[] idle;
    private GreenfootImage[] shoot;
    private boolean shootOnce = false;
    private boolean shooting = false;
    private boolean isPoweredUp = false;
    private long powerUpStartTime;
    private long lastFrame2 = System.nanoTime();
    private long deltaTime2;
    private final long POWER_UP_DURATION = PlantRegistry.POWER_UP_DURATION;
    private final long BASE_SHOOT_DELAY = PlantRegistry.CACTUS3_SHOOT_DELAY;
    private long shootDelay = PlantRegistry.CACTUS3_SHOOT_DELAY;
    private PlayScene cachedPlayScene;
    
    public Cactus3() {
        maxHp = PlantRegistry.CACTUS3_HP;
        hp = maxHp;
        shoot = importSprites(PlantAssets.CACTUS_SHOOT, 2);
        idle = importSprites(PlantAssets.CACTUS_IDLE, 4);
        setImage(idle[0]);
    }
    
    @Override
    public void addedToWorld(World world) {
        super.addedToWorld(world);
        if (world instanceof PlayScene) {
            this.cachedPlayScene = (PlayScene) world;
        }
    }
    
    @Override
    public void hit(int dmg) {
        if (getWorld() != null && isLiving()) {
            hitFlash(shootOnce ? shoot : idle, shootOnce ? PlantAssets.CACTUS_SHOOT : PlantAssets.CACTUS_IDLE);
            hp -= dmg;
        }
    }
    
    public void activatePlantFood() {
        this.isPoweredUp = true;
        this.powerUpStartTime = System.currentTimeMillis();
        this.shootDelay = PlantRegistry.POWER_UP_SHOOT_DELAY;
    }
    
    @Override
    public void update() {
        if (getWorld() == null) return;
        updatePowerUpStatus();
        currentFrame = System.nanoTime();
        handleAction();
        if (getWorld() != null) checkZombieInRow();
    }
    
    private void updatePowerUpStatus() {
        if (isPoweredUp && (System.currentTimeMillis() - powerUpStartTime > POWER_UP_DURATION)) {
            isPoweredUp = false;
            shootDelay = BASE_SHOOT_DELAY;
        }
    }
    
    private void handleAction() {
        if (!(shooting || isPoweredUp)) {
            animate(idle, 300, true);
            lastFrame2 = currentFrame;
            shootOnce = false;
            return;
        }

        deltaTime2 = (currentFrame - lastFrame2) / 1000000;

        if (deltaTime2 < shootDelay) {
            animate(isPoweredUp ? shoot : idle, isPoweredUp ? 2 : 200, !isPoweredUp);
            shootOnce = false;
        } else {
            if (!shootOnce) {
                shootOnce = true;
                frame = 0;
            }
            if (frame >= 1 && shootOnce) executeShoot();
            animate(shoot, isPoweredUp ? 2 : 10, false);
        }
    }
    
    private void executeShoot() {
        int myRow = getYPos();
        if (myRow != -1) {
            AudioManager.playSound(80, false, PlantAssets.SOUND_THROW, PlantAssets.SOUND_THROW2);
            
            getWorld().addObject(new Needle3(getY(), -50), getX(), getY());
            getWorld().addObject(new Needle3(getY(), 0), getX(), getY());
            getWorld().addObject(new Needle3(getY(), 50), getX(), getY());
            
            lastFrame2 = currentFrame;
            shootOnce = false;
        }
    }
    
    private void checkZombieInRow() {
        int myRow = getYPos();
        if (myRow == -1 || cachedPlayScene.level == null) {
            shooting = false;
            return;
        }

        List<Zombie> rowZombies = cachedPlayScene.level.zombieRow.get(myRow);
        shooting = rowZombies.stream().anyMatch(z -> z.getWorld() != null && z.getX() > getX() && z.getX() <= cachedPlayScene.getWidth() + 10);
    }
}