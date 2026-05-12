import greenfoot.*;
import java.util.List;

public class Peashooter extends Plant {

    private static final PlantType TYPE             = PlantType.PEASHOOTER;
    private static final long       POWER_UP_DURATION = GameConstants.POWER_UP_DURATION;
    private static final long       POWER_UP_DELAY    = GameConstants.POWER_UP_SHOOT_DELAY;

    private GreenfootImage[] idle;
    private GreenfootImage[] shoot;
    private boolean shootOnce = false;
    private long powerUpStartTime;
    private long lastFrameTimeNano = System.nanoTime();
    private long currentShootDelay = TYPE.shootDelay;
    private PlayScene cachedPlayScene;

    public Peashooter() {
        setMaxHp(TYPE.hp);
        setHp(TYPE.hp);
        setDamage(TYPE.damage);
        setCost(TYPE.cost);
        
        shoot = importSprites(PlantAssets.PEASHOOTER_SHOOT, 3);
        idle  = importSprites(PlantAssets.PEASHOOTER_IDLE,  9);
        
        if (idle != null && idle.length > 0) {
            setImage(idle[0]);
        }
    }

    @Override
    public void addedToWorld(World world) {
        super.addedToWorld(world);
        if (world instanceof PlayScene) {
            cachedPlayScene = (PlayScene) world;
        }
    }

    @Override
    public void hit(int dmg) {
        if (getWorld() == null || !isLiving()) return;
    
        String assetPath = (getState() == PlantState.SHOOTING) 
                           ? PlantAssets.PEASHOOTER_SHOOT 
                           : PlantAssets.PEASHOOTER_IDLE;
        
        hitFlash(assetPath); 
    
        setHp(getHp() - dmg);
        if (getHp() <= 0) {
            onDeath();
        }
    }

    public void activatePlantFood() {
        setState(PlantState.PEA_POWERED_UP);
        powerUpStartTime = System.currentTimeMillis();
        currentShootDelay = POWER_UP_DELAY;
    }

    @Override
    public void update() {
        if (getWorld() == null) return;
        
        updatePowerUpStatus();
        handleAction();
        
        if (getWorld() != null) {
            checkZombieInRow();
        }
    }

    private void updatePowerUpStatus() {
        if (getState() == PlantState.PEA_POWERED_UP &&
            System.currentTimeMillis() - powerUpStartTime > POWER_UP_DURATION) {
            setState(PlantState.IDLE);
            currentShootDelay = TYPE.shootDelay;
        }
    }

    private void handleAction() {
        if (getState() == PlantState.IDLE) {
            animate(idle, 300, true);
            lastFrameTimeNano = System.nanoTime();
            shootOnce = false;
            return;
        }

        boolean poweredUp = (getState() == PlantState.PEA_POWERED_UP);
        long elapsedMillis = (System.nanoTime() - lastFrameTimeNano) / 1_000_000;

        if (elapsedMillis >= currentShootDelay) {
            animate(shoot, poweredUp ? 50 : 100, false);
            
            if (!shootOnce && frame >= 1) {
                executeShoot();
                shootOnce = true;
            }
            
            if (frame >= shoot.length - 1) {
                lastFrameTimeNano = System.nanoTime();
                shootOnce = false;
            }
        } else {
            animate(idle, 300, true);
        }
    }

    private void executeShoot() {
        int myRow = getYPos();
        if (myRow == -1 || cachedPlayScene == null) return;

        AudioManager.getInstance().playSound(80, false, PlantAssets.SOUND_THROW);
        
        cachedPlayScene.addObject(new Pea(myRow), getX() + 25, getY() - 17);
    }

    private void checkZombieInRow() {
        if (getState() == PlantState.MERGING || getState() == PlantState.PEA_POWERED_UP) return;

        int myRow = getYPos();
        if (myRow == -1 || cachedPlayScene == null || cachedPlayScene.level == null) {
            setState(PlantState.IDLE);
            return;
        }

        List<Zombie> rowZombies = cachedPlayScene.level.zombieRow.get(myRow);
        boolean hasTarget = rowZombies.stream().anyMatch(z ->
            z.getWorld() != null &&
            z.getX() > getX() &&
            z.getX() <= cachedPlayScene.getWidth() + 10
        );

        if (hasTarget && getState() == PlantState.IDLE) {
            setState(PlantState.SHOOTING);
        } else if (!hasTarget && getState() == PlantState.SHOOTING) {
            setState(PlantState.IDLE);
        }
    }
}