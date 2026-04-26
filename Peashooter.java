import greenfoot.*;
import java.util.List;

public class Peashooter extends Plant {
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
    
    public Peashooter() {
        maxHp = 60;
        hp = maxHp;
        shoot = importSprites("peashootershoot",3);
        idle = importSprites("peashooter",9);
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
    public void hit (int dmg) {
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
        
        if (!shooting && !isPoweredUp) {
            animate(idle, 300, true);
            lastFrame2 = System.nanoTime();
            shootOnce = false;
            return;
        }
    
        long elapsedMillis = (System.nanoTime() - lastFrame2) / 1_000_000;
    
        if (elapsedMillis >= shootDelay) {
            animate(shoot, isPoweredUp ? 50 : 100, false);
            if (!shootOnce && frame >= 1) { 
                executeShoot();
                shootOnce = true; 
            }
            if (frame >= shoot.length - 1) {
                lastFrame2 = System.nanoTime();
                shootOnce = false;
            }
        } else {
            animate(idle, 300, true);
        }
    }
    
    private void executeShoot() {
        int myRow = getYPos();
        if (myRow != -1 && cachedPlayScene != null) {
            AudioPlayer.play(80, "throw.mp3", "throw2.mp3");
            cachedPlayScene.addObject(new Pea(myRow), getX() + 25, getY() - 17);
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