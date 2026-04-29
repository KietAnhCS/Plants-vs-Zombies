import greenfoot.*;
import java.util.*;

public abstract class Zombie extends SpriteAnimator {
    protected ZombieAppearance appearance;
    protected ZombieState currentState;
    
    public double walkSpeed;
    public int hp, maxHp;
    public boolean isAlive = true;
    public boolean eating = false;
    public Plant target;
    public PlayScene playScene;
    
    public boolean fallen = false;
    public boolean spawnHead = false;
    public boolean resetAnim = false;
    public boolean finalDeath = false;
    public boolean fixAnim = false;
    private boolean eatOnce = false;
    
    protected int damage = 20;
    public GreenfootImage[] headless, headlesseating, fall;

    public Zombie() {
        headless = importSprites("zombieheadless", 7);
        fall = importSprites("zombiefall", 6);
        headlesseating = importSprites("headlesszombieeating", 7);
    }

    @Override
    public void addedToWorld(World world) {
        if (world instanceof PlayScene) this.playScene = (PlayScene) world;
        world.addObject(new HealthBar(this, 60), getX(), getY() - 50);
    }

    public void hit(int dmg) {
        if (!isAlive) return;
        this.hp -= dmg;
        if (this.hp <= 0) {
            this.hp = 0;
            this.isAlive = false;
        }
    }

    public void takeDmg(int dmg) {
        hit(dmg);
    }

    public void setState(ZombieState newState) {
        this.currentState = newState;
    }

    @Override
    public void act() {
        if (getWorld() == null) return;
        if (!getWorld().getObjects(Overlay.class).isEmpty()) return;
        if (isLiving()) {
            updateLogic();
            handleThresholds();
        } else {
            deathAnim();
        }
    }

    private void updateLogic() {
        if (currentState != null) {
            currentState.update();
            animate(currentState.getAnimation(), 200, true);
        }
    }

    public boolean isLiving() {
        return hp > 0 && isAlive;
    }

    public boolean isEating() {
        if (playScene == null || playScene.GridManager == null) return false;
        int yIdx = getYPos();
        Plant[][] boardGrid = playScene.GridManager.Board;
        if (yIdx < 0 || yIdx >= boardGrid.length) return false;
        Plant[] rowCells = boardGrid[yIdx];
        for (Plant p : rowCells) {
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

    public void playEating() {
        if (target == null || target.getWorld() == null) {
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

    public void deathAnim() {
        if (!resetAnim) { frame = 0; resetAnim = true; }
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
                if (getWorld() != null) getWorld().addObject(new Head(), getX(), getY() - 10);
            }
            if (eating) animate(headlesseating, 350, false);
            else { animate(headless, 350, false); move(-walkSpeed); }
            if (frame >= 6) { finalDeath = true; removeFromRow(); }
        }
    }

    protected void removeFromRow() {
        if (playScene == null || playScene.level == null) return;
        int row = getYPos();
        if (row >= 0 && row < playScene.level.zombieRow.size()) {
            playScene.level.zombieRow.get(row).remove(this);
        }
    }

    public int getYPos() {
        if (playScene == null || playScene.GridManager == null) return 0;
        return playScene.GridManager.getGridY(getX(), getY());
    }

    protected abstract void handleThresholds();
}