import greenfoot.*;
import java.util.List;

public class Cactus extends Plant {

    private static final PlantType TYPE = PlantType.CACTUS;

    private GreenfootImage[] idle;
    private GreenfootImage[] shoot;
    private PlantState state = PlantState.IDLE;
    private long lastShootTime = System.currentTimeMillis();

    public Cactus() {
        maxHp = TYPE.hp;
        hp    = maxHp;
        shoot = importSprites(PlantAssets.CACTUS_SHOOT, 2);
        idle  = importSprites(PlantAssets.CACTUS_IDLE,  4);
        setImage(idle[0]);
    }

    @Override
    public void hit(int dmg) {
        if (isLiving()) {
            hitFlash(
                state == PlantState.SHOOTING ? shoot : idle,
                state == PlantState.SHOOTING ? PlantAssets.CACTUS_SHOOT : PlantAssets.CACTUS_IDLE
            );
            hp -= dmg;
        }
    }

    @Override
    public void update() {
        if (getWorld() == null) return;
        playScene = (PlayScene) getWorld();
        checkZombieInRow();
        handleAction();
    }

    private void handleAction() {
        long currentTime = System.currentTimeMillis();

        if (state == PlantState.IDLE) {
            animate(idle, 150, true);
            return;
        }

        if (currentTime - lastShootTime >= TYPE.shootDelay) {
            state = PlantState.SHOOTING;
            animate(shoot, 150, false);
            if (frame == 1) fireNeedle();
            if (frame >= shoot.length - 1) {
                state = PlantState.IDLE;
                lastShootTime = currentTime;
                setFrame(0);
            }
        } else {
            animate(idle, 150, true);
        }
    }

    private void fireNeedle() {
        if (getWorld() == null) return;
        AudioManager.playSound(80, false, PlantAssets.SOUND_THROW);
        getWorld().addObject(new Needle(getYPos()), getX() + 30, getY() - 8);
    }

    private void checkZombieInRow() {
        if (playScene == null || playScene.level == null) return;
        List<Zombie> rowZombies = playScene.level.zombieRow.get(getYPos());
        if (rowZombies == null || rowZombies.isEmpty()) {
            state = PlantState.IDLE;
            return;
        }
        boolean hasTarget = rowZombies.stream().anyMatch(z ->
            z.getWorld() != null &&
            z.getX() > getX() &&
            z.getX() <= playScene.getWidth() + 10
        );
        if (!hasTarget) state = PlantState.IDLE;
        else if (state == PlantState.IDLE) state = PlantState.SHOOTING;
    }
}