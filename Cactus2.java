import greenfoot.*;
import java.util.List;

public class Cactus2 extends Plant {
    private static final PlantType TYPE = PlantType.CACTUS_2;
    private GreenfootImage[] idle, shoot;
    private boolean adjusted = false;
    private long lastAttackTime = System.currentTimeMillis();
    private PlayScene cachedPlayScene;
    private boolean shootOnce = false;

    public Cactus2() {
        setMaxHp(TYPE.hp);
        setHp(TYPE.hp);
        setDamage(TYPE.damage);
        setCost(TYPE.cost);
        
        idle = importSprites(PlantAssets.CACTUS_IDLE, 4);
        shoot = importSprites(PlantAssets.CACTUS_SHOOT, 2);
        
        if (idle != null && idle.length > 0) setImage(idle[0]);
    }

    @Override
    public void addedToWorld(World world) {
        super.addedToWorld(world);
        if (world instanceof PlayScene) cachedPlayScene = (PlayScene) world;
        world.addObject(new HealthBar(this, 50), getX(), getY());
    }

    @Override
    public void hit(int dmg) {
        if (getWorld() == null || !isLiving()) return;

        String assetPath = (getState() == PlantState.SHOOTING) 
                           ? PlantAssets.CACTUS_SHOOT 
                           : PlantAssets.CACTUS_IDLE;
        
        hitFlash(assetPath);
        
        setHp(getHp() - dmg);
        if (getHp() <= 0) onDeath();
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
            animate(shoot, 100, false);
            executeShoot();
        } else {
            setState(PlantState.IDLE);
            animate(idle, 300, true);
            shootOnce = false;
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

        if (frame >= 1 && !shootOnce) {
            AudioManager.getInstance().playSound(80, false, PlantAssets.SOUND_THROW, PlantAssets.SOUND_THROW2);
            
            getWorld().addObject(new Needle2(getY(), -50), getX(), getY());
            getWorld().addObject(new Needle2(getY(), 50), getX(), getY());
            
            shootOnce = true;
            lastAttackTime = System.currentTimeMillis();
        }
        
        if (frame >= shoot.length - 1) {
            shootOnce = false;
        }
    }
}