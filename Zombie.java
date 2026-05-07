import greenfoot.*;
import java.util.*;

public abstract class Zombie extends SpriteAnimator {

    protected ZombieState currentState;
    public final ZombieConfig config;

    public double walkSpeed;
    public int hp, maxHp;
    public boolean isAlive  = true;
    public boolean eating   = false;
    public Plant   target;
    public PlayScene playScene;

    public boolean fallen      = false;
    public boolean spawnHead   = false;
    public boolean resetAnim   = false;
    public boolean finalDeath  = false;
    public boolean fixAnim     = false;
    private boolean eatOnce    = false;

    public GreenfootImage[] headless, headlesseating, fall;

    public Zombie(ZombieConfig config) {
        this.config = config;
        this.maxHp  = config.maxHp;
        this.hp     = config.maxHp;
        headless       = importSprites(SpriteKey.SHARED_HEADLESS.path,   7);
        headlesseating = importSprites(SpriteKey.SHARED_HEADLESS_EAT.path, 7);
        fall           = importSprites(SpriteKey.SHARED_FALL.path,           6);
    }

    @Override
    public void addedToWorld(World world) {
        if (world instanceof PlayScene) {
            this.playScene = (PlayScene) world;
            world.addObject(new HealthBar(this, 60), getX(), getY() - 50);
        }
    }

    public void hit(int dmg) {
        if (!isAlive) return;
        this.hp -= dmg;
        if (this.hp <= 0) {
            this.hp      = 0;
            this.isAlive = false;
            this.target = null;
            this.eating = false;
        }
    }

    public void takeDmg(int dmg) {
        hit(dmg);
    }

    public void walk() {
        if (getWorld() != null) move(-walkSpeed);
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
        if (currentState == null) return;
        currentState.update();
        animate(currentState.getAnimation(), 200, true);
    }

    public boolean isLiving() {
        return hp > 0 && isAlive && getWorld() != null;
    }

    public boolean checkEating() {
        this.eating = computeEating();
        return this.eating;
    }

    private boolean computeEating() {
        if (playScene == null || playScene.GridManager == null || getWorld() == null) return false;
        
        int yIdx = getYPos();
        if (yIdx < 0 || yIdx >= playScene.GridManager.Board.length) return false;

        Plant[] myRow = playScene.GridManager.Board[yIdx];
        if (myRow == null) return false;

        int currentX = getX();
        for (Plant p : myRow) {
            if (p == null || p.getWorld() == null) continue;
            if (Math.abs(p.getX() - currentX) < 40) {
                if (p instanceof PotatoMine && ((PotatoMine) p).getState() == PlantState.POTATO_ARMED) {
                    target = null;
                    return false;
                }
                target = p;
                return true;
            }
        }
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
                target.hit(config.damage);
            }
        } else {
            eatOnce = false;
        }
    }

    public void deathAnim() {
        if (getWorld() == null) return;

        if (!resetAnim) { 
            frame = 0; 
            resetAnim = true; 
            removeFromRow();
        }

        if (finalDeath) {
            if (!fixAnim) {
                fixAnim = true;
                AudioManager.playSound(80, false, "zombie_falling_1.mp3", "zombie_falling_2.mp3");
                
                int lastX = getX();
                int lastY = getY();
                World world = getWorld();
                
                world.addObject(new fallingZombie(fall), lastX - 12, lastY + 20);
                world.removeObject(this);
            }
        } else {
            if (!spawnHead) {
                spawnHead = true;
                AudioManager.playSound(80, false, "limbs_pop.mp3");
                getWorld().addObject(new Head(), getX(), getY() - 10);
            }

            if (eating) {
                animate(headlesseating, 350, false);
            } else { 
                animate(headless, 350, false); 
                walk(); 
            }

            if (frame >= 6) { 
                finalDeath = true; 
            }
        }
    }

    protected void removeFromRow() {
        if (playScene == null || playScene.level == null) return;
        int row = getYPos();
        if (row >= 0 && row < playScene.level.zombieRow.size()) {
            List<Zombie> rowList = playScene.level.zombieRow.get(row);
            if (rowList.contains(this)) {
                rowList.remove(this);
            }
        }
    }

    public int getYPos() {
        if (playScene == null || playScene.GridManager == null || getWorld() == null) return -1;
        return playScene.GridManager.getGridY(getX(), getY());
    }

    protected abstract void handleThresholds();
}