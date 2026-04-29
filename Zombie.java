import greenfoot.*;
import java.util.*;

public class Zombie extends SpriteAnimator {
    public boolean fallen = false;
    public boolean eating = false;
    public boolean eatOnce = false;
    public int hp, maxHp;
    public double walkSpeed;
    public PlayScene playScene;
    public boolean spawnHead = false;
    public Plant target;

    protected int damage; 
    public boolean isAlive = true;
    public GreenfootImage[] headless, headlesseating, fall;
    public boolean resetAnim = false, finalDeath = false, fixAnim = false;

    public Zombie() {
        headless = importSprites("zombieheadless", 7);
        fall = importSprites("zombiefall", 6);
        headlesseating = importSprites("headlesszombieeating", 7);
        this.damage = 20; 
    }

    @Override
    public void act() {
        if (getWorld() == null) return;
        if (!getWorld().getObjects(Overlay.class).isEmpty()) return;

        if (isLiving()) {
            update();
        } else {
            deathAnim();
        }
    }

    public void update() {
    }

    public void deathAnim() {
        if (!resetAnim) {
            frame = 0;
            resetAnim = true;
        }

        if (frame < 7) {
            if (finalDeath) {
                if (!fixAnim) {
                    fixAnim = true;
                    AudioManager.playSound(80, false, "zombie_falling_1.mp3", "zombie_falling_2.mp3");
                    if (getWorld() != null) {
                        getWorld().addObject(new fallingZombie(fall), getX() - 12, getY() + 20);
                        getWorld().removeObject(this);
                    }
                }
            } else {
                if (!spawnHead) {
                    spawnHead = true;
                    AudioManager.playSound(80, false, "limbs_pop.mp3");
                    getWorld().addObject(new Head(), getX(), getY() - 10);
                }
                
                if (!eating) {
                    animate(headless, 350, false);
                    move(-walkSpeed);
                } else {
                    animate(headlesseating, 350, false);
                }
                
                if (frame >= 6) {
                    finalDeath = true;
                    removeFromRow();
                }
            }
        }
    }

    public void playEating() {
        if (getWorld() == null || target == null || target.getWorld() == null) {
            eating = false;
            target = null;
            return;
        }

        if (frame == 2 || frame == 5) {
            if (!eatOnce) {
                eatOnce = true;
                AudioManager.playSound(70, false, "chomp.mp3", "chomp2.mp3");
                target.hit(this.damage); 
            }
        } else {
            eatOnce = false;
        }
    }

    @Override
    public void addedToWorld(World world) {
        if (world instanceof PlayScene) {
            this.playScene = (PlayScene) world;
        }
        world.addObject(new HealthBar(this, 60), getX(), getY() - 50);
    }

    public boolean isLiving() { return hp > 0 && isAlive; }

    public void takeDmg(int dmg) {
        if (!isAlive) return;
        hp -= dmg;
        if (hp <= 0) {
            isAlive = false;
        }
    }

    protected void removeFromRow() {
        if (playScene == null || playScene.level == null) return;
        int row = getYPos();
        if (row >= 0 && row < playScene.level.zombieRow.size()) {
            playScene.level.zombieRow.get(row).remove(this);
        }
    }

    public boolean isEating() {
        if (playScene == null || playScene.GridManager == null) return false;
        
        int yIdx = getYPos();
        Plant[][] boardGrid = playScene.GridManager.Board;
        
        if (yIdx < 0 || yIdx >= boardGrid.length) return false;

        Plant[] row = boardGrid[yIdx];
        for (Plant p : row) {
            if (p != null && p.getWorld() != null) {
                if (Math.abs(p.getX() - getX()) < 40) {
                    if (p instanceof PotatoMine && ((PotatoMine) p).armed) {
                        eating = false;
                        return false;
                    }
                    eating = true;
                    target = p;
                    return true;
                }
            }
        }
        eating = false;
        target = null;
        return false;
    }

    public int getYPos() {
        if (playScene == null || playScene.GridManager == null) return 0;
        return playScene.GridManager.getGridY(getX(), getY());
    }

    public void hit(int dmg) { takeDmg(dmg); }
}