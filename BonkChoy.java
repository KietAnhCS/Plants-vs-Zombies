import greenfoot.*;
import java.util.List;

public class BonkChoy extends Plant {
    private static final PlantType TYPE = PlantType.BONK_CHOY;
    private GreenfootImage[] idle, pRight, kRight;
    private int punchCount = 0;
    private boolean adjusted = false;
    private long lastAttackTime = System.currentTimeMillis();
    private PlayScene cachedPlayScene;

    public BonkChoy() {
        setMaxHp(TYPE.hp);
        setHp(TYPE.hp);
        setDamage(TYPE.damage);
        setCost(TYPE.cost);
        
        kRight = loadResized(PlantAssets.BONKCHOY_KO, 15);
        idle   = loadResized(PlantAssets.BONKCHOY_IDLE, 23);
        pRight = loadResized(PlantAssets.BONKCHOY_ATTACK, 10);
        
        if (idle != null && idle.length > 0) setImage(idle[0]);
    }

    private GreenfootImage[] loadResized(String prefix, int count) {
        GreenfootImage[] imgs = importSprites(prefix, count);
        for (GreenfootImage img : imgs) {
            if (img != null) {
                img.scale(
                    Math.max(1, (int)(img.getWidth() * 0.45)), 
                    Math.max(1, (int)(img.getHeight() * 0.45))
                );
            }
        }
        return imgs;
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
            PlantState attackState = (punchCount >= 9) ? PlantState.BONK_KO_PUNCH : PlantState.BONK_PUNCHING;
            setState(attackState);
            
            boolean beingEaten = getHp() < getMaxHp();
            int dmg = (attackState == PlantState.BONK_KO_PUNCH ? TYPE.damage * 2 : TYPE.damage) + (beingEaten ? 5 : 0);
            
            animate(attackState == PlantState.BONK_KO_PUNCH ? kRight : pRight, 40, false);
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
            setHp(Math.min(getMaxHp(), getHp() + (int)(getMaxHp() * 0.15)));
        } else {
            punchCount++;
        }
        
        AudioManager.getInstance().playSound(80, false, PlantAssets.SOUND_BONK);
        lastAttackTime = System.currentTimeMillis();
    }
}