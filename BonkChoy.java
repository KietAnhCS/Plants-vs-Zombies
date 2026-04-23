import greenfoot.*;
import java.util.List;

public class BonkChoy extends Plant {
    private GreenfootImage[] idle, pRight, kRight;
    private int frameIndex = 0, punchCount = 0;
    private boolean adjusted = false;
    private long lastAttackTime = System.currentTimeMillis(), lastFrameTime = 0;

    public BonkChoy() {
        maxHp = 350; 
        hp = maxHp;
        kRight = loadResized("bonkchoyknockoutone", 15);
        idle = loadResized("bonkchoyidle_three", 23);
        pRight = loadResized("bonkchoyattackone", 10);
        setImage(idle[0]);
    }

    private GreenfootImage[] loadResized(String prefix, int count) {
        GreenfootImage[] imgs = importSprites(prefix, count);
        for (GreenfootImage img : imgs) {
            img.scale(img.getWidth() / 2, img.getHeight() / 2);
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
        List<Zombie> targets = getObjectsAtOffset(50, 0, Zombie.class); 
        boolean isKO = (punchCount >= 9);
        boolean beingEaten = (hp < maxHp);

        if (!targets.isEmpty()) {
            playLoop(isKO ? kRight : pRight, 40);
            int dmg;
            if (beingEaten) {
                dmg = isKO ? 35 : 25;
            } else {
                dmg = isKO ? 6 : 5;
            }
            applyDmg(targets, 400, dmg, isKO);
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
                this.hp += (int)(maxHp * 0.5);
                if (this.hp > maxHp) this.hp = maxHp;
            } else {
                punchCount++;
            }
            AudioPlayer.play(80, "bonk.mp3");
            lastAttackTime = System.currentTimeMillis();
        }
    }

    private void playLoop(GreenfootImage[] anim, int delay) {
        if (System.currentTimeMillis() - lastFrameTime > delay) {
            frameIndex = (frameIndex + 1) % anim.length;
            setImage(anim[frameIndex]);
            lastFrameTime = System.currentTimeMillis();
        }
    }

    @Override
    public void activatePlantFood() {
        this.hp = maxHp;
    }
}