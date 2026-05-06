import greenfoot.*;
import java.util.List;

public class Peashooter extends Plant {

    private static final PlantType TYPE             = PlantType.PEASHOOTER;
    private static final long      POWER_UP_DURATION = GameConstants.POWER_UP_DURATION;
    private static final long      POWER_UP_DELAY    = GameConstants.POWER_UP_SHOOT_DELAY;

    private GreenfootImage[] idle;
    private GreenfootImage[] shoot;
    private PlantState state = PlantState.IDLE;
    private boolean shootOnce = false;
    private long powerUpStartTime;
    private long lastFrame2 = System.nanoTime();
    private long shootDelay = TYPE.shootDelay;
    private PlayScene cachedPlayScene;

    public Peashooter() {
        maxHp = TYPE.hp;
        hp    = maxHp;
        shoot = importSprites(PlantAssets.PEASHOOTER_SHOOT, 3);
        idle  = importSprites(PlantAssets.PEASHOOTER_IDLE,  9);
        setImage(idle[0]);
    }

    @Override
    public void addedToWorld(World world) {
        super.addedToWorld(world);
        if (world instanceof PlayScene) cachedPlayScene = (PlayScene) world;
    }

    @Override
    public void hit(int dmg) {
        if (getWorld() == null || !isLiving()) return;
        hitFlash(
            state == PlantState.SHOOTING ? shoot : idle,
            state == PlantState.SHOOTING ? PlantAssets.PEASHOOTER_SHOOT : PlantAssets.PEASHOOTER_IDLE
        );
        hp -= dmg;
    }

    public void activatePlantFood() {
        state            = PlantState.PEA_POWERED_UP;
        powerUpStartTime = System.currentTimeMillis();
        shootDelay       = POWER_UP_DELAY;
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
        if (state == PlantState.PEA_POWERED_UP &&
            System.currentTimeMillis() - powerUpStartTime > POWER_UP_DURATION) {
            state      = PlantState.IDLE;
            shootDelay = TYPE.shootDelay;
        }
    }

    private void handleAction() {
        if (state == PlantState.IDLE) {
            animate(idle, 300, true);
            lastFrame2 = System.nanoTime();
            shootOnce  = false;
            return;
        }

        boolean poweredUp     = state == PlantState.PEA_POWERED_UP;
        long    elapsedMillis = (System.nanoTime() - lastFrame2) / 1_000_000;

        if (elapsedMillis >= shootDelay) {
            animate(shoot, poweredUp ? 50 : 100, false);
            if (!shootOnce && frame >= 1) {
                executeShoot();
                shootOnce = true;
            }
            if (frame >= shoot.length - 1) {
                lastFrame2 = System.nanoTime();
                shootOnce  = false;
            }
        } else {
            animate(idle, 300, true);
        }
    }

    private void executeShoot() {
        int myRow = getYPos();
        if (myRow == -1 || cachedPlayScene == null) return;
        AudioManager.playSound(80, false, PlantAssets.SOUND_THROW, PlantAssets.SOUND_THROW2);
        cachedPlayScene.addObject(new Pea(myRow), getX() + 25, getY() - 17);
    }

    private void checkZombieInRow() {
        int myRow = getYPos();
        if (myRow == -1 || cachedPlayScene.level == null) {
            if (state != PlantState.PEA_POWERED_UP) state = PlantState.IDLE;
            return;
        }
        List<Zombie> rowZombies = cachedPlayScene.level.zombieRow.get(myRow);
        boolean hasTarget = rowZombies.stream().anyMatch(z ->
            z.getWorld() != null &&
            z.getX() > getX() &&
            z.getX() <= cachedPlayScene.getWidth() + 10
        );
        if      ( hasTarget && state == PlantState.IDLE)        state = PlantState.SHOOTING;
        else if (!hasTarget && state == PlantState.SHOOTING)    state = PlantState.IDLE;
    }
}