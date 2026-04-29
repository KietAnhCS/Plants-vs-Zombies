import greenfoot.*;
import java.util.List;

public class BonkChoy3 extends Plant {
    private GreenfootImage[] idle, pRight, kRight;
    private int frameIndex = 0, punchCount = 0;
    private boolean adjusted = false;
    private long lastAttackTime = System.currentTimeMillis(), lastFrameTime = 0;

    public BonkChoy3() {
        maxHp = PlantRegistry.BONK3_HP;
        hp = maxHp;
        
        kRight = loadResized(PlantAssets.BONKCHOY_KO, 15);
        idle = loadResized(PlantAssets.BONKCHOY_IDLE, 23);
        pRight = loadResized(PlantAssets.BONKCHOY_ATTACK, 10);
        
        if (idle != null && idle.length > 0) {
            setImage(idle[0]);
        }
    }

    private GreenfootImage[] loadResized(String prefix, int count) {
        GreenfootImage[] imgs = importSprites(prefix, count);
        for (GreenfootImage img : imgs) {
            if (img != null) {
                int newWidth = (int)(img.getWidth() * 0.45);
                int newHeight = (int)(img.getHeight() * 0.45);
                img.scale(Math.max(1, newWidth), Math.max(1, newHeight));
            }
        }
        return imgs;
    }

    @Override
    public void addedToWorld(World world) {
        super.addedToWorld(world); 
        world.addObject(new HealthBar(this, 50), getX(), getY());
    }
    
    @Override
    public void update() {
        if (getWorld() == null) return;
        if (!adjusted) { 
            setLocation(getX(), getY() - 15); 
            adjusted = true; 
        } 
        handleNormalCombat();
    }

    private void handleNormalCombat() {
        List<Zombie> targets = getIntersectingObjects(Zombie.class); 
        boolean isKO = (punchCount >= 3);
        boolean beingEaten = (hp < maxHp);

        if (!targets.isEmpty()) {
            playLoop(isKO ? kRight : pRight, 20);
            int dmg;
            if (beingEaten) {
                dmg = isKO ? PlantRegistry.BONK3_DMG_KO : PlantRegistry.BONK3_DMG_KO - 10;
            } else {
                dmg = isKO ? PlantRegistry.BONK3_DMG_KO : PlantRegistry.BONK3_DMG_NORMAL;
            }
            
            applyDmg(targets, PlantRegistry.BONK3_ATTACK_DELAY, dmg, isKO);
        } else {
            playLoop(idle, 40);
            if (System.currentTimeMillis() - lastAttackTime > 1000) {
                punchCount = 0;
            }
        }
    }

    private void applyDmg(List<Zombie> targets, int delay, int dmg, boolean ko) {
        if (System.currentTimeMillis() - lastAttackTime > delay) {
            for (Zombie z : targets) {
                if (z.getWorld() != null) z.hit(dmg);
            }
            if (ko) {
                punchCount = 0;
                this.hp = Math.min(maxHp, this.hp + (int)(maxHp * 0.35));
            } else {
                punchCount++;
            }
            AudioManager.playSound(80, false, PlantAssets.SOUND_BONK);
            lastAttackTime = System.currentTimeMillis();
        }
    }

    private void playLoop(GreenfootImage[] anim, int delay) {
        if (anim == null || anim.length == 0) return;
        if (System.currentTimeMillis() - lastFrameTime > delay) {
            frameIndex = (frameIndex + 1) % anim.length;
            setImage(anim[frameIndex]);
            lastFrameTime = System.currentTimeMillis();
        }
    }
}