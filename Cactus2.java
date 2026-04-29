import greenfoot.*;
import java.util.List;

public class Cactus2 extends Plant {
    private GreenfootImage[] idle;
    private GreenfootImage[] shoot;
    private boolean shootOnce = false;
    private boolean shooting = false;
    private boolean isPoweredUp = false;
    private long powerUpStartTime;
    private long lastFrame2 = System.nanoTime();
    private long deltaTime2;
    private final long POWER_UP_DURATION = 3000L;
    private final long BASE_SHOOT_DELAY = 1500L;
    private long shootDelay = 1500L;
    private PlayScene cachedPlayScene;
    
    public Cactus2() {
        maxHp = 60;
        hp = maxHp;
        shoot = importSprites("cactusshoot", 2);
        idle = importSprites("cactus", 4);
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
            hitFlash(shootOnce ? shoot : idle, shootOnce ? "peashootershoot" : "peashooter");
        }
    }
    
    public void activatePlantFood() {
        this.isPoweredUp = true;
        this.powerUpStartTime = System.currentTimeMillis();
        this.shootDelay = 300L;
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
            AudioManager.playSound(80, false, "throw.mp3", "throw2.mp3");
            
            int offset = 50; 
    
            getWorld().addObject(new Needle2(getY(), -offset), getX(), getY());
            
            getWorld().addObject(new Needle2(getY(), offset), getX(), getY());
            
    
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