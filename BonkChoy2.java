import greenfoot.*;
import java.util.List;

public class BonkChoy2 extends Plant {
    private GreenfootImage[] idle, pRight, kRight;
    private int frameIndex = 0, punchCount = 0;
    private boolean adjusted = false;
    private long lastAttackTime = System.currentTimeMillis(), lastFrameTime = 0;

    public BonkChoy2() {
        maxHp = 1000; 
        hp = maxHp;
        
        kRight = loadResized("bonkchoyknockoutone", 15);
        idle = loadResized("bonkchoyidle_three", 23);
        pRight = loadResized("bonkchoyattackone", 10);
        
        if (idle != null && idle.length > 0) {
            setImage(idle[0]);
        }
    }

    private GreenfootImage[] loadResized(String prefix, int count) {
        
        GreenfootImage[] imgs = importSprites(prefix, count);
        
        for (GreenfootImage img : imgs) {
            if (img != null) {
                
                int oldWidth = img.getWidth();
                int oldHeight = img.getHeight();
                
                int newWidth = (int)(oldWidth * 0.45);
                int newHeight = (int)(oldHeight * 0.45);
                
                if (newWidth <= 0) newWidth = 1;
                if (newHeight <= 0) newHeight = 1;
                
                img.scale(newWidth, newHeight);
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
        
        List<Zombie> targets = getObjectsAtOffset(25, 0, Zombie.class); 
        boolean isKO = (punchCount >= 3);
        boolean beingEaten = (hp < maxHp);

        if (!targets.isEmpty()) {
            playLoop(isKO ? kRight : pRight, 20);
            int dmg;
            if (beingEaten) {
                dmg = isKO ? 15 : 10;
            } else {
                dmg = isKO ? 6 : 5;
            }
            
            applyDmg(targets, 200, dmg, isKO);
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
                this.hp += (int)(maxHp * 0.35);
                if (this.hp > maxHp) this.hp = maxHp;
            } else {
                punchCount++;
            }
            AudioManager.playSound(80, false, "bonk.mp3");
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