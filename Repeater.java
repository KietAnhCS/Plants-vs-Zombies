import greenfoot.*;

public class Repeater extends Plant {

    private static final PlantType TYPE           = PlantType.REPEATER;
    private static final int       SHOTS_PER_CYCLE = 2;

    private GreenfootImage[] idle;
    private GreenfootImage[] shoot;
    private PlantState state      = PlantState.IDLE;
    private int        shootCount = 0;
    private long       lastShot   = System.nanoTime();

    public Repeater() {
        maxHp = TYPE.hp;
        hp    = maxHp;
        idle  = importSprites(PlantAssets.REPEATER_IDLE,  7);
        shoot = importSprites(PlantAssets.REPEATER_SHOOT, 3);
    }

    @Override
    public void hit(int dmg) {
        if (!isLiving()) return;
        hitFlash(
            state == PlantState.SHOOTING ? shoot : idle,
            state == PlantState.SHOOTING ? PlantAssets.REPEATER_SHOOT : PlantAssets.REPEATER_IDLE
        );
        hp -= dmg;
    }

    @Override
    public void update() {
        if (getWorld() == null) return;
        PlayScene world = (PlayScene) getWorld();
        checkZombieInRow(world);

        if (state == PlantState.IDLE) {
            animate(idle, 225, true);
            return;
        }

        long elapsed = (System.nanoTime() - lastShot) / 1_000_000;
        if (elapsed < TYPE.shootDelay) {
            animate(idle, 225, true);
            return;
        }

        state = PlantState.SHOOTING;
        if (animate(shoot, 70, false)) {
            AudioManager.playSound(80, false, PlantAssets.SOUND_THROW, PlantAssets.SOUND_THROW2);
            world.addObject(new Pea(getYPos()), getX() + 25, getY() - 17);
            shootCount++;
            if (shootCount >= SHOTS_PER_CYCLE) {
                shootCount = 0;
                lastShot   = System.nanoTime();
                state      = PlantState.IDLE;
            }
        }
    }

    private void checkZombieInRow(PlayScene world) {
        if (state == PlantState.SHOOTING) return;
        state = PlantState.IDLE;
        for (Zombie z : world.level.zombieRow.get(getYPos())) {
            if (z != null && z.getWorld() != null &&
                z.getX() > getX() && z.getX() <= world.getWidth() + 10) {
                state = PlantState.SHOOTING;
                break;
            }
        }
    }
}