import greenfoot.*;
import java.util.List;

public class BonkChoy3 extends Plant {

    private static final PlantType TYPE = PlantType.BONK_CHOY_3;

    private GreenfootImage[] idle, pRight, kRight;
    private int frameIndex = 0, punchCount = 0;
    private boolean adjusted = false;
    private long lastAttackTime = System.currentTimeMillis(), lastFrameTime = 0;
    private PlantState state = PlantState.IDLE;

    public BonkChoy3() {
        maxHp = TYPE.hp;
        hp    = maxHp;

        kRight = loadResized(PlantAssets.BONKCHOY_KO,     15);
        idle   = loadResized(PlantAssets.BONKCHOY_IDLE,   23);
        pRight = loadResized(PlantAssets.BONKCHOY_ATTACK, 10);

        if (idle != null && idle.length > 0) setImage(idle[0]);
    }

    private GreenfootImage[] loadResized(String prefix, int count) {
        GreenfootImage[] imgs = importSprites(prefix, count);
        for (GreenfootImage img : imgs) {
            if (img != null) {
                img.scale(
                    Math.max(1, (int)(img.getWidth()  * 0.45)),
                    Math.max(1, (int)(img.getHeight() * 0.45))
                );
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
        handleCombat();
    }

    private void handleCombat() {
        List<Zombie> targets = getIntersectingObjects(Zombie.class);

        if (!targets.isEmpty()) {
            state = (punchCount >= 3) ? PlantState.BONK_KO_PUNCH : PlantState.BONK_PUNCHING;
            boolean beingEaten = hp < maxHp;

            int dmg;
            if (state == PlantState.BONK_KO_PUNCH) {
                dmg = TYPE.damage * 2;
            } else {
                dmg = beingEaten ? TYPE.damage * 2 - 10 : TYPE.damage;
            }

            playLoop(state == PlantState.BONK_KO_PUNCH ? kRight : pRight, 20);
            applyDmg(targets, (int) TYPE.shootDelay, dmg);
        } else {
            state = PlantState.IDLE;
            playLoop(idle, 40);
            if (System.currentTimeMillis() - lastAttackTime > 1000) punchCount = 0;
        }
    }

    private void applyDmg(List<Zombie> targets, int delay, int dmg) {
        if (System.currentTimeMillis() - lastAttackTime <= delay) return;

        for (Zombie z : targets) {
            if (z.getWorld() != null) z.hit(dmg);
        }

        if (state == PlantState.BONK_KO_PUNCH) {
            punchCount = 0;
            hp = Math.min(maxHp, hp + (int)(maxHp * 0.35));
        } else {
            punchCount++;
        }

        AudioManager.playSound(80, false, PlantAssets.SOUND_BONK);
        lastAttackTime = System.currentTimeMillis();
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