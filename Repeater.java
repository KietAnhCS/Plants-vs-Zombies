import greenfoot.*;

public class Repeater extends Plant {
    private static final int   SHOTS_PER_CYCLE = 2;
    private GreenfootImage[] idle;
    private GreenfootImage[] shoot;
    private boolean shooting    = false;
    private boolean inShootAnim = false;
    private int     shootCount  = 0;
    private long    lastShot    = System.nanoTime();

    public Repeater() {
        maxHp = PlantRegistry.REPEATER_HP;
        hp    = maxHp;
        idle  = importSprites(PlantAssets.REPEATER_IDLE,  7);
        shoot = importSprites(PlantAssets.REPEATER_SHOOT, 3);
    }

    @Override
    public void hit(int dmg) {
        if (!isLiving()) return;
        hitFlash(inShootAnim ? shoot : idle,
                 inShootAnim ? PlantAssets.REPEATER_SHOOT : PlantAssets.REPEATER_IDLE);
        hp -= dmg;
    }

    @Override
    public void update() {
        if (getWorld() == null) return;
        PlayScene world = (PlayScene) getWorld();
        checkZombieInRow(world);

        if (!shooting) {
            inShootAnim = false;
            animate(idle, 225, true);
            return;
        }

        long elapsed = (System.nanoTime() - lastShot) / 1_000_000;
        if (elapsed < PlantRegistry.REPEATER_SHOOT_DELAY) {
            inShootAnim = false;
            animate(idle, 225, true);
            return;
        }

        inShootAnim = true;
        if (animate(shoot, 70, false)) {
            AudioManager.playSound(80, false, PlantAssets.SOUND_THROW, PlantAssets.SOUND_THROW2);
            world.addObject(new Pea(getYPos()), getX() + 25, getY() - 17);
            shootCount++;
            if (shootCount >= SHOTS_PER_CYCLE) {
                shootCount = 0;
                lastShot   = System.nanoTime();
                inShootAnim = false;
            }
        }
    }

    private void checkZombieInRow(PlayScene world) {
        shooting = false;
        for (Zombie z : world.level.zombieRow.get(getYPos())) {
            if (z != null && z.getWorld() != null
                    && z.getX() > getX() && z.getX() <= world.getWidth() + 10) {
                shooting = true;
                break;
            }
        }
    }
}