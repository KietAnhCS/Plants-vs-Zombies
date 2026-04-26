import greenfoot.*;
import java.util.*;

public class Zombie extends SpriteAnimator {
    public boolean fallen = false;
    public boolean eating = false;
    public boolean eatOnce = false;
    public int hp, maxHp;
    public double walkSpeed;
    public PlayScene PlayScene;
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

        if (frame <= 6) {
            if (finalDeath) {
                if (!fixAnim) {
                    fixAnim = true;
                    AudioPlayer.play(80, "zombie_falling_1.mp3", "zombie_falling_2.mp3");
                    PlayScene.addObject(new fallingZombie(fall), getX() - 12, getY() + 20);
                    PlayScene.removeObject(this);
                    return;
                }
            } else {
                if (!spawnHead) {
                    spawnHead = true;
                    AudioPlayer.play(80, "limbs_pop.mp3");
                    getWorld().addObject(new Head(), getX(), getY() - 10);
                }
                if (!eating) {
                    animate(headless, 350, false);
                    move(-walkSpeed);
                } else {
                    animate(headlesseating, 350, false);
                }
            }
        } else if (!finalDeath) {
            resetAnim = false;
            finalDeath = true;
            removeFromRow();
        }
    }

    public void playEating() {
        if (getWorld() == null || !getWorld().getObjects(Overlay.class).isEmpty()) return;
        if (target == null || target.getWorld() == null) {
            eating = false;
            target = null;
            return;
        }

        if (frame == 5 || frame == 2) {
            if (!eatOnce) {
                eatOnce = true;
                AudioPlayer.play(70, "chomp.mp3", "chomp2.mp3", "chompsoft.mp3");
                
                target.hit(this.damage); 
            }
        } else {
            eatOnce = false;
        }
    }

    public void addedToWorld(World world) {
        if (world instanceof PlayScene) {
            this.PlayScene = (PlayScene) world;
        }
        world.addObject(new HealthBar(this, 60), getX(), getY());
    }

    public boolean isLiving() { return hp > 0; }

    public void takeDmg(int dmg) {
        if (!isAlive) return;
        hp -= dmg;
        if (hp <= 0) {
            isAlive = false;
            removeFromRow();
        }
    }

    private void removeFromRow() {
        if (PlayScene == null || PlayScene.level == null) return;
        for (ArrayList<Zombie> i : PlayScene.level.zombieRow) {
            if (i.contains(this)) {
                i.remove(this);
                break;
            }
        }
    }

    public boolean isEating() {
        if (PlayScene == null || PlayScene.GridManager == null) return false;
        int yIdx = getYPos();
        
        Plant[][] boardGrid = PlayScene.GridManager.Board;
        if (yIdx < 0 || yIdx >= boardGrid.length) return false;

        Plant[] row = boardGrid[yIdx];
        for (int i = 0; i < row.length; i++) {
            Plant p = row[i];
            if (p != null && p.getWorld() != null) {
                if (Math.abs(p.getX() - getX() + 5) < 35) {
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
        if (PlayScene == null || PlayScene.GridManager == null) return 0;
        return PlayScene.GridManager.getGridY(getX(), getY());
    }

    public int getXPos() { return getX(); }
    public void hit(int dmg) { takeDmg(dmg); }
}