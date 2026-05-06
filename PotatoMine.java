import greenfoot.*;
import java.util.List;

public class PotatoMine extends Plant {

    private static final PlantType TYPE          = PlantType.POTATO_MINE;
    private static final long      ARMING_TIME   = TYPE.shootDelay;
    private static final int       EXPLODE_RANGE = GameConstants.POTATO_EXPLOSION_RANGE;

    private GreenfootImage[] idle;
    private GreenfootImage[] arm;
    private PlantState state    = PlantState.POTATO_ARMING;
    private boolean    playSFX  = false;
    private long       startTime;

    public PotatoMine() {
        maxHp     = TYPE.hp;
        hp        = maxHp;
        idle      = importSprites(PlantAssets.POTATO_IDLE,   5);
        arm       = importSprites(PlantAssets.POTATO_ARMING, 3);
        startTime = System.currentTimeMillis();
        if (arm != null && arm.length > 0) setImage(arm[0]);
    }

    @Override
    public void update() {
        if (getWorld() == null || isMerging || isDragging) return;
        playScene = (PlayScene) getWorld();
        if (System.currentTimeMillis() - startTime < ARMING_TIME) return;
        switch (state) {
            case POTATO_ARMING:
                if (!playSFX) {
                    AudioManager.playSound(80, false, PlantAssets.SOUND_DIRT_RISE);
                    playSFX = true;
                }
                animate(arm, 200, false);
                if (frame >= arm.length - 1) state = PlantState.POTATO_ARMED;
                break;
            case POTATO_ARMED:
                animate(idle, 200, true);
                checkExplosion();
                break;
            case POTATO_EXPLODING:
                explode();
                break;
        }
    }

    @Override
    public void hit(int dmg) {
        if (getWorld() == null || !isLiving()) return;
        if (state == PlantState.POTATO_ARMING) {
            hp -= dmg;
            hitFlash(arm, PlantAssets.POTATO_ARMING);
        }
    }

    public PlantState getState() {
        return state;
    }

    public void checkExplosion() {
        if (getWorld() == null || playScene == null || playScene.level == null) return;
        List<Zombie> zombies = playScene.level.zombieRow.get(getYPos());
        if (zombies == null || zombies.isEmpty()) return;
        for (Zombie z : zombies) {
            if (z != null && z.getWorld() != null &&
                Math.abs(z.getX() - getX()) < EXPLODE_RANGE) {
                state = PlantState.POTATO_EXPLODING;
                return;
            }
        }
    }

    private void explode() {
        World world = getWorld();
        if (world == null) return;
        world.addObject(new Explosion(playScene.level.zombieRow.get(getYPos())), getX(), getY() - 25);
        AudioManager.playSound(90, false, PlantAssets.SOUND_POTATO_EXPLODE);
        if (playScene != null && playScene.GridManager != null) {
            playScene.GridManager.removePlant(getXPos(), getYPos());
        }
        world.removeObject(this);
    }
}