import greenfoot.*;
import java.util.List;

public class Cactus2 extends Plant {

    private static final PlantType TYPE             = PlantType.CACTUS_2;
    private static final long      POWER_UP_DURATION  = GameConstants.POWER_UP_DURATION;
    private static final long      POWER_UP_DELAY     = GameConstants.POWER_UP_SHOOT_DELAY;

    private GreenfootImage[] idle;
    private GreenfootImage[] shoot;
    private PlantState state = PlantState.IDLE;
    private boolean shootOnce = false;
    private long powerUpStartTime;
    private long lastFrame2 = System.nanoTime();
    private long deltaTime2;
    private long shootDelay = TYPE.shootDelay;
    private PlayScene cachedPlayScene;

    public Cactus2() {
        maxHp = TYPE.hp;
        hp    = maxHp;
        shoot = importSprites(PlantAssets.CACTUS_SHOOT, 2);
        idle  = importSprites(PlantAssets.CACTUS_IDLE,  4);
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
            state == PlantState.SHOOTING ? PlantAssets.CACTUS_SHOOT : PlantAssets.CACTUS_IDLE
        );
        hp -= dmg;
    }

    public void activatePlantFood() {
        state             = PlantState.PEA_POWERED_UP;
        powerUpStartTime  = System.currentTimeMillis();
        shootDelay        = POWER_UP_DELAY;
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
            lastFrame2 = currentFrame;
            shootOnce  = false;
            return;
        }

        deltaTime2 = (currentFrame - lastFrame2) / 1_000_000;
        boolean poweredUp = state == PlantState.PEA_POWERED_UP;

        if (deltaTime2 < shootDelay) {
            animate(poweredUp ? shoot : idle, poweredUp ? 2 : 200, !poweredUp);
            shootOnce = false;
        } else {
            if (!shootOnce) {
                shootOnce = true;
                frame     = 0;
            }
            if (frame >= 1 && shootOnce) executeShoot();
            animate(shoot, poweredUp ? 2 : 10, false);
        }
    }

    private void executeShoot() {
        if (getYPos() == -1) return;
        AudioManager.playSound(80, false, PlantAssets.SOUND_THROW, PlantAssets.SOUND_THROW2);
        getWorld().addObject(new Needle2(getY(), -50), getX(), getY());
        getWorld().addObject(new Needle2(getY(),  50), getX(), getY());
        lastFrame2 = currentFrame;
        shootOnce  = false;
    }

    private void checkZombieInRow() {
        int myRow = getYPos();
        if (myRow == -1 || cachedPlayScene.level == null) {
            state = PlantState.IDLE;
            return;
        }
        List<Zombie> rowZombies = cachedPlayScene.level.zombieRow.get(myRow);
        boolean hasTarget = rowZombies.stream().anyMatch(z ->
            z.getWorld() != null &&
            z.getX() > getX() &&
            z.getX() <= cachedPlayScene.getWidth() + 10
        );
        if (!hasTarget && state != PlantState.PEA_POWERED_UP) state = PlantState.IDLE;
        else if (hasTarget && state == PlantState.IDLE)        state = PlantState.SHOOTING;
    }
}