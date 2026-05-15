import greenfoot.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Comparator;

public class BonkChoy extends Plant {
    private static final PlantType TYPE = PlantType.BONK_CHOY;
    private GreenfootImage[] idle, pRight, kRight;
    private int punchCount = 0;
    private long lastAttackTime = System.currentTimeMillis();
    private PlayScene cachedPlayScene;

    public BonkChoy() {
        setMaxHp(TYPE.hp);
        setHp(TYPE.hp);
        setDamage(TYPE.damage);
        setCost(TYPE.cost);

        kRight = importSprites(PlantAssets.BONKCHOY_KO,     15, 0.45);
        idle   = importSprites(PlantAssets.BONKCHOY_IDLE,   23, 0.45);
        pRight = importSprites(PlantAssets.BONKCHOY_ATTACK, 10, 0.45);

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
        String assetPath = PlantAssets.BONKCHOY_IDLE;
        if (getState() == PlantState.BONK_PUNCHING)  assetPath = PlantAssets.BONKCHOY_ATTACK;
        else if (getState() == PlantState.BONK_KO_PUNCH) assetPath = PlantAssets.BONKCHOY_KO;
        hitFlash(assetPath);
        super.hit(dmg);
    }

    @Override
    public void update() {
        if (getWorld() == null) return;
        handleCombat();
    }

    private void handleCombat() {
        if (getState() == PlantState.MERGING) return;

        List<Zombie> targets = getObjectsInRange(50, Zombie.class)
            .stream()
            .filter(z -> z.getWorld() != null && z.getX() >= getX() - 10)
            .sorted(Comparator.comparingInt(Zombie::getX))
            .collect(Collectors.toList());

        if (!targets.isEmpty()) {
            PlantState attackState = (punchCount >= 9) ? PlantState.BONK_KO_PUNCH : PlantState.BONK_PUNCHING;
            setState(attackState);
            boolean beingEaten = getHp() < getMaxHp();
            int dmg = (attackState == PlantState.BONK_KO_PUNCH ? TYPE.damage * 2 : TYPE.damage) + (beingEaten ? 5 : 0);
            animate(attackState == PlantState.BONK_KO_PUNCH ? kRight : pRight, 40, true);
            applyDmg(targets, (int) TYPE.shootDelay, dmg, attackState);
        } else {
            setState(PlantState.IDLE);
            animate(idle, 40, true);
            if (System.currentTimeMillis() - lastAttackTime > 1000) punchCount = 0;
        }
    }

    private void applyDmg(List<Zombie> targets, int delay, int dmg, PlantState attackState) {
        if (System.currentTimeMillis() - lastAttackTime <= delay) return;

        if (!targets.isEmpty()) {
            Zombie primaryTarget = targets.get(0);
            if (primaryTarget.getWorld() != null) primaryTarget.hit(dmg);
        }

        if (attackState == PlantState.BONK_KO_PUNCH) {
            punchCount = 0;
            setHp(Math.min(getMaxHp(), getHp() + (int)(getMaxHp() * 0.15)));
        } else {
            punchCount++;
        }

        AudioManager.getInstance().playSound(80, false, PlantAssets.SOUND_BONK);
        lastAttackTime = System.currentTimeMillis();
    }

    @Override
    public String getPlantName() {
        return TYPE.name();
    }
}