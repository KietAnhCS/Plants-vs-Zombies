import greenfoot.*;
import java.util.List;

public class BonkChoy extends Plant {
    private GreenfootImage[] idle, pRight, pfStart, pfLoop, pfEnd, kRight;
    private int pfStage = 0, frameIndex = 0, punchCount = 0;
    private boolean adjusted = false, isUsingPF = false;
    private long lastAttackTime = System.currentTimeMillis(), pfTimer = 0, lastFrameTime = 0;

    private long idleTimer = System.currentTimeMillis();
    private final long DEATH_DELAY = 14000; 

    public BonkChoy() {
        maxHp = 300; hp = maxHp;
    
        kRight = loadResized("bonkchoyknockoutone", 15);
        idle = loadResized("bonkchoyidle_three", 23);
        pRight = loadResized("bonkchoyattackone", 10);
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
        if (!adjusted) { setLocation(getX(), getY() - 15); adjusted = true; } 
        
        if (isUsingPF) {
            handlePlantFood();
            resetIdleTimer();
        } else {
            handleNormalCombat();
        }

        checkIdleDeath();
    }

    private void checkIdleDeath() {
        if (System.currentTimeMillis() - idleTimer > DEATH_DELAY) {
            spawnSunsAndDie();
        }
    }

    private void resetIdleTimer() {
        idleTimer = System.currentTimeMillis();
    }

    private void spawnSunsAndDie() {
        PlayScene world = (PlayScene) getWorld();
        if (world != null) {
            PlayScene.addObject(new Sun(100), getX(), getY() - 10);
            world.removeObject(this);
        }
    }

    public void activatePlantFood() {
        isUsingPF = true; pfStage = 0; frameIndex = 0;
    }

    private void handlePlantFood() {
        long now = System.currentTimeMillis();
        if (pfStage == 0) {
            playOnce(pfStart, 40);
            if (frameIndex >= pfStart.length - 1) { pfStage = 1; frameIndex = 0; pfTimer = now; }
        } else if (pfStage == 1) {
            playLoop(pfLoop, 30);
            for (Zombie z : getObjectsInRange(75, Zombie.class)) z.hit(2);
            if (now - pfTimer > 2500) { pfStage = 2; frameIndex = 0; }
        } else if (pfStage == 2) {
            playOnce(pfEnd, 50);
            if (frameIndex >= pfEnd.length - 1) isUsingPF = false;
        }
    }

    private void handleNormalCombat() {
        List<Zombie> targets = getObjectsAtOffset(40, 0, Zombie.class);
        boolean isKO = (punchCount >= 3);

        if (!targets.isEmpty()) {
            playLoop(isKO ? kRight : pRight, 40);
            applyDmg(targets, 400, isKO ? 30 : 15, isKO);
            resetIdleTimer();
        } else {
            playLoop(idle, 40);
            if (System.currentTimeMillis() - lastAttackTime > 1000) punchCount = 0;
        }
    }

    private void applyDmg(List<Zombie> targets, int delay, int dmg, boolean ko) {
        if (System.currentTimeMillis() - lastAttackTime > delay) {
            for (Zombie z : targets) z.hit(dmg);
            if (ko) {
                hp = Math.min(maxHp, hp + (maxHp / 50));
                punchCount = 0;
            } else punchCount++;
            
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
        if (System.currentTimeMillis() - lastFrameTime > delay && frameIndex < anim.length - 1) {
            frameIndex++;
            setImage(anim[frameIndex]);
            lastFrameTime = System.currentTimeMillis();
        }
    }

    @Override
    public void hit(int dmg) {
        if (getWorld() == null) return;
        resetIdleTimer();
        hp -= dmg;
        if (hp <= 0) getWorld().removeObject(this);
    }
}