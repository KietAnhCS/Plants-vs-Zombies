import greenfoot.*;
import java.util.List;

public class BonkChoy extends Plant {
    private GreenfootImage[] idle, pLeft, pRight, pBoth, pfStart, pfLoop, pfEnd, kLeft, kRight;
    private int yOffset = -15, xOffset = 0, pfStage = 0, frameIndex = 0, punchCount = 0;
    private boolean adjusted = false, isUsingPF = false;
    private long lastAttackTime = System.currentTimeMillis(), pfTimer = 0, lastFrameTime = 0;

    public BonkChoy() {
        maxHp = 300; hp = maxHp;
        kRight = loadResized("bonkchoyknockoutone", 15);
        kLeft = loadResized("bonkchoyknockouttwo", 15);
        idle = loadResized("bonkchoyidle_three", 31);
        pRight = loadResized("bonkchoyattackone", 10);
        pLeft = loadResized("bonkchoyattacktwo", 10);
        pBoth = loadResized("bonkchoyattackthree", 20);
        pfStart = loadResized("bonkchoyplantfoodstart", 30);
        pfLoop = loadResized("bonkchoyplantfood", 30);
        pfEnd = loadResized("bonkchoyplantfoodend", 10);
        setImage(idle[0]);
    }

    private GreenfootImage[] loadResized(String prefix, int count) {
        GreenfootImage[] imgs = importSprites(prefix, count);
        for (GreenfootImage img : imgs) img.scale(img.getWidth() / 2, img.getHeight() / 2);
        return imgs;
    }

    @Override
    public void update() {
        if (getWorld() == null) return;
        if (!adjusted) { setLocation(getX() + xOffset, getY() + yOffset); adjusted = true; }
        if (isUsingPF) handlePlantFood();
        else handleNormalCombat();
    }

    public void activatePlantFood() {
        if (!isUsingPF) { isUsingPF = true; pfStage = 0; frameIndex = 0; }
    }

    private void handlePlantFood() {
        if (pfStage == 0) {
            playOnce(pfStart, 40);
            if (frameIndex >= pfStart.length - 1) { pfStage = 1; frameIndex = 0; pfTimer = System.currentTimeMillis(); }
        } else if (pfStage == 1) {
            playLoop(pfLoop, 30);
            for (Zombie z : getObjectsInRange(75, Zombie.class)) z.hit(2);
            if (System.currentTimeMillis() - pfTimer > 2500) { pfStage = 2; frameIndex = 0; }
        } else if (pfStage == 2) {
            playOnce(pfEnd, 50);
            if (frameIndex >= pfEnd.length - 1) { isUsingPF = false; pfStage = 0; frameIndex = 0; }
        }
    }

    private void handleNormalCombat() {
        List<Zombie> r = getObjectsAtOffset(40, 0, Zombie.class);
        List<Zombie> l = getObjectsAtOffset(-40, 0, Zombie.class);
        boolean isKO = (punchCount >= 3);

        if (!r.isEmpty() && !l.isEmpty()) {
            playLoop(pBoth, 40);
            applyDmg(r, l, 400, 15, false);
        } else if (!r.isEmpty()) {
            playLoop(isKO ? kRight : pRight, 40);
            applyDmg(r, null, 400, isKO ? 30 : 15, isKO);
        } else if (!l.isEmpty()) {
            playLoop(isKO ? kLeft : pLeft, 40);
            applyDmg(null, l, 400, isKO ? 30 : 15, isKO);
        } else {
            playLoop(idle, 40);
            if (System.currentTimeMillis() - lastAttackTime > 1000) punchCount = 0;
        }
    }

    private void applyDmg(List<Zombie> r, List<Zombie> l, int delay, int dmg, boolean ko) {
        if (System.currentTimeMillis() - lastAttackTime > delay) {
            if (r != null) for (Zombie z : r) z.hit(dmg);
            if (l != null) for (Zombie z : l) z.hit(dmg);
            if (ko) {
                hp = Math.min(maxHp, hp + (maxHp * 10 / 100));
                punchCount = 0;
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

    private void playOnce(GreenfootImage[] anim, int delay) {
        if (System.currentTimeMillis() - lastFrameTime > delay) {
            if (frameIndex < anim.length - 1) { frameIndex++; setImage(anim[frameIndex]); }
            lastFrameTime = System.currentTimeMillis();
        }
    }

    @Override
    public void hit(int dmg) {
        if (getWorld() == null) return;
        List<Zombie> l = getObjectsAtOffset(-40, 0, Zombie.class);
        if (!l.isEmpty()) return; 
        hp -= dmg;
        if (hp <= 0) getWorld().removeObject(this);
    }
}