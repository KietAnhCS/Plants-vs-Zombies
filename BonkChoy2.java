import greenfoot.*;
import java.util.List;

public class BonkChoy2 extends Plant {
    private static final PlantType TYPE = PlantType.BONK_CHOY_2;
    private GreenfootImage[] idle, pRight, kRight;
    private int punchCount = 0;
    private boolean adjusted = false;
    private long lastAttackTime = System.currentTimeMillis();
    private PlayScene cachedPlayScene;

    public BonkChoy2() {
        setMaxHp(TYPE.hp);
        setHp(TYPE.hp);
        setDamage(TYPE.damage);
        setCost(TYPE.cost);
        
        // Sử dụng ảnh gốc chất lượng cao
        kRight = importSprites(PlantAssets.BONKCHOY_KO, 15);
        idle   = importSprites(PlantAssets.BONKCHOY_IDLE, 23);
        pRight = importSprites(PlantAssets.BONKCHOY_ATTACK, 10);
        
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
        
        // Xác định assetPath để hitFlash đồng bộ với động tác hiện tại
        String assetPath = PlantAssets.BONKCHOY_IDLE;
        if (getState() == PlantState.BONK_PUNCHING) assetPath = PlantAssets.BONKCHOY_ATTACK;
        else if (getState() == PlantState.BONK_KO_PUNCH) assetPath = PlantAssets.BONKCHOY_KO;
        
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

        List<Zombie> targets = getObjectsAtOffset(25, 0, Zombie.class);
        
        if (!targets.isEmpty()) {
            // Level 2: Chỉ cần 3 phát đấm thường là có 1 phát KO (nhanh hơn Level 1)
            PlantState attackState = (punchCount >= 3) ? PlantState.BONK_KO_PUNCH : PlantState.BONK_PUNCHING;
            setState(attackState);
            
            boolean beingEaten = getHp() < getMaxHp();
            int dmg = (attackState == PlantState.BONK_KO_PUNCH ? TYPE.damage * 2 : TYPE.damage) + (beingEaten ? 5 : 0);
            
            // Tốc độ diễn hoạt cực nhanh (20ms) tạo cảm giác đấm liên thanh
            animate(attackState == PlantState.BONK_KO_PUNCH ? kRight : pRight, 20, false);
            applyDmg(targets, (int) TYPE.shootDelay, dmg, attackState);
        } else {
            setState(PlantState.IDLE);
            animate(idle, 40, true);
            
            if (System.currentTimeMillis() - lastAttackTime > 1000) {
                punchCount = 0;
            }
        }
    }

    private void applyDmg(List<Zombie> targets, int delay, int dmg, PlantState attackState) {
        if (System.currentTimeMillis() - lastAttackTime <= delay) return;
        
        for (Zombie z : targets) {
            if (z.getWorld() != null) z.hit(dmg);
        }
        
        if (attackState == PlantState.BONK_KO_PUNCH) {
            punchCount = 0;
            // Level 2: Hồi máu vượt trội (35% HP tối đa) khi tung cú đấm KO
            setHp(Math.min(getMaxHp(), getHp() + (int)(getMaxHp() * 0.35)));
        } else {
            punchCount++;
        }
        
        AudioManager.getInstance().playSound(80, false, PlantAssets.SOUND_BONK);
        lastAttackTime = System.currentTimeMillis();
    }
}