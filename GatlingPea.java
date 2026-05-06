import greenfoot.*;
import java.util.List;

public class GatlingPea extends Plant {

    private static final PlantType TYPE = PlantType.GATLING_PEA;

    private GreenfootImage[] idle;
    private GreenfootImage[] shoot;
    private PlantState state = PlantState.IDLE;
    private int  shootCount  = 0;
    private boolean resetFrame = false;
    private long lastFrame2  = System.nanoTime();
    private long deltaTime2;

    public GatlingPea() {
        maxHp = TYPE.hp;
        hp    = maxHp;
        idle  = importSprites(PlantAssets.GATLING_PEA, 19);
        shoot = importSprites(PlantAssets.GATLING_PEA, 19);
        setImage(idle[0]);
    }

    @Override
    public void hit(int dmg) {
        if (!isLiving()) return;
        hitFlash(state == PlantState.SHOOTING ? shoot : idle, PlantAssets.GATLING_PEA);
        hp -= dmg;
    }

    @Override
    public void update() {
        if (getWorld() == null) return;
        PlayScene world = (PlayScene) getWorld();
        currentFrame = System.nanoTime();
        checkForZombies(world);

        if (state == PlantState.IDLE) {
            animate(idle, 225, true);
            lastFrame2 = System.nanoTime();
            return;
        }

        deltaTime2 = (currentFrame - lastFrame2) / 1_000_000;

        if (deltaTime2 < TYPE.shootDelay) {
            animate(idle, 225, true);
            shootCount = 0;
            resetFrame = false;
            return;
        }

        if (shootCount >= TYPE.burstCount) {
            lastFrame2 = currentFrame;
            return;
        }

        if (!resetFrame) {
            frame      = 0;
            resetFrame = true;
        }

        if (frame >= 4) {
            AudioManager.playSound(80, false, PlantAssets.SOUND_THROW, PlantAssets.SOUND_THROW2);
            world.addObject(new Pea(getYPos()), getX() + 25, getY() - 17);
            frame = 0;
            shootCount++;
        }

        if (frame < 0) frame = 0;
        animate(shoot, 30, false);
    }

    private void checkForZombies(PlayScene world) {
        List<Zombie> zombiesInRow = world.level.zombieRow.get(getYPos());
        if (zombiesInRow == null || zombiesInRow.isEmpty()) {
            state = PlantState.IDLE;
            return;
        }
        boolean found = false;
        for (Zombie z : zombiesInRow) {
            if (z != null && z.getWorld() != null &&
                z.getX() > getX() && z.getX() <= world.getWidth() + 10) {
                found = true;
                break;
            }
        }
        state = found ? PlantState.SHOOTING : PlantState.IDLE;
    }
}