import greenfoot.*;
import java.util.List;

public class Repeater extends Plant {
    private static final PlantType TYPE = PlantType.REPEATER;
    private GreenfootImage[] idle, shoot;
    private boolean adjusted = false;
    private long lastAttackTime = System.currentTimeMillis();
    private PlayScene cachedPlayScene;

    public Repeater() {
        setMaxHp(TYPE.hp);
        setHp(TYPE.hp);
        setDamage(TYPE.damage);
        setCost(TYPE.cost);
        idle  = importSprites(PlantAssets.REPEATER_IDLE, 7);
        shoot = importSprites(PlantAssets.REPEATER_SHOOT, 3);
        if (idle != null && idle.length > 0) setImage(idle[0]);
    }

    @Override
    public void addedToWorld(World world) {
        super.addedToWorld(world);
        if (world instanceof PlayScene) cachedPlayScene = (PlayScene) world;
        
    }

    @Override
    public void hit(int dmg) {
        if (getWorld() == null || !isLiving()) return;
        hitFlash(getState() == PlantState.SHOOTING ? PlantAssets.REPEATER_SHOOT : PlantAssets.REPEATER_IDLE);
        super.hit(dmg);
    }

    @Override
    public void update() {
        if (getWorld() == null) return;
        if (!adjusted) {
            setLocation(getX(), getY() - 15);
            adjusted = true;
        }
        handleCombat();
    }

    private void handleCombat() {
        if (getState() == PlantState.MERGING) return;
        if (checkZombieInRow()) {
            setState(PlantState.SHOOTING);
            animate(shoot, 100, true);
            executeShoot();
        } else {
            setState(PlantState.IDLE);
            animate(idle, 300, true);
        }
    }

    private boolean checkZombieInRow() {
        int myRow = getYPos();
        if (myRow == -1 || cachedPlayScene == null || cachedPlayScene.level == null) return false;
        List<Zombie> rowZombies = cachedPlayScene.level.zombieRow.get(myRow);
        return rowZombies.stream().anyMatch(z ->
            z.getWorld() != null &&
            z.getX() > getX() &&
            z.getX() <= cachedPlayScene.getWidth() + 10
        );
    }

    private void executeShoot() {
        if (System.currentTimeMillis() - lastAttackTime <= TYPE.shootDelay) return;
        for (int i = 0; i < 2; i++) {
            AudioManager.getInstance().playSound(80, false, PlantAssets.SOUND_THROW);
            getWorld().addObject(new Pea(getYPos()), getX() + 25 + (i * 15), getY() - 17);
        }
        lastAttackTime = System.currentTimeMillis();
    }
}