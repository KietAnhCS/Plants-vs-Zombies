import greenfoot.*;
import java.util.List;

public class GatlingPea2 extends Plant {

    private static final PlantType TYPE              = PlantType.GATLING_PEA_2;
    private static final long      POWER_UP_DURATION = GameConstants.POWER_UP_DURATION;
    private static final long      BURST_INTERVAL    = GameConstants.GATLING2_BURST_INTERVAL;

    private GreenfootImage[] idle;
    private GreenfootImage[] shoot;
    private PlantState state          = PlantState.IDLE;
    private int        shootCount     = 0;
    private long       powerUpStartTime;
    private long       lastActionTime = System.currentTimeMillis();

    public GatlingPea2() {
        maxHp = TYPE.hp;
        hp    = maxHp;
        shoot = importSprites(PlantAssets.GATLING_PEA, 19);
        idle  = importSprites(PlantAssets.GATLING_PEA, 19);
        setImage(idle[0]);
    }

    public void activatePlantFood() {
        state            = PlantState.PEA_POWERED_UP;
        powerUpStartTime = System.currentTimeMillis();
        hp               = maxHp;
    }

    @Override
    public void hit(int dmg) {
        if (getWorld() == null || !isLiving()) return;
        hp -= dmg;
        hitFlash(state == PlantState.SHOOTING ? shoot : idle, PlantAssets.GATLING_PEA);
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
        if (state == PlantState.PEA_POWERED_UP &&
            System.currentTimeMillis() - powerUpStartTime > POWER_UP_DURATION) {
            state = PlantState.IDLE;
        }
    }

    private void handleAction() {
        long currentTime = System.currentTimeMillis();

        if (state == PlantState.PEA_POWERED_UP) {
            animate(shoot, 40, false);
            if (currentTime - lastActionTime >= 50) {
                fireFirePea();
                lastActionTime = currentTime;
            }
            return;
        }

        if (state == PlantState.SHOOTING) {
            if (shootCount < TYPE.burstCount) {
                animate(shoot, 100, false);
                if (currentTime - lastActionTime >= BURST_INTERVAL) {
                    fireFirePea();
                    shootCount++;
                    lastActionTime = currentTime;
                }
            } else {
                animate(idle, 225, true);
                if (currentTime - lastActionTime >= TYPE.shootDelay) {
                    shootCount     = 0;
                    lastActionTime = currentTime;
                }
            }
            return;
        }

        animate(idle, 225, true);
        shootCount = 0;
    }

    private void fireFirePea() {
        if (getWorld() == null) return;
        AudioManager.playSound(80, false, PlantAssets.SOUND_THROW, PlantAssets.SOUND_THROW2);
        getWorld().addObject(new FirePea(getYPos()), getX() + 25, getY() - 17);
    }

    private void checkZombieInRow() {
        if (playScene == null || playScene.level == null) return;
        List<Zombie> rowZombies = playScene.level.zombieRow.get(getYPos());
        if (rowZombies == null || rowZombies.isEmpty()) {
            if (state != PlantState.PEA_POWERED_UP) state = PlantState.IDLE;
            return;
        }
        boolean hasTarget = rowZombies.stream().anyMatch(z ->
            z.getWorld() != null &&
            z.getX() > getX() &&
            z.getX() <= playScene.getWidth() + 10
        );
        if      ( hasTarget && state == PlantState.IDLE)     state = PlantState.SHOOTING;
        else if (!hasTarget && state == PlantState.SHOOTING) state = PlantState.IDLE;
    }
}