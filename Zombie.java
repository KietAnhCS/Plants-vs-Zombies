import greenfoot.*;
import java.util.*;

public abstract class Zombie extends SpriteAnimator implements IDamageable, IGridObject {
    public ZombieConfig config;
    public PlayScene playScene;

    protected int hp;
    protected int maxHp;
    protected double walkSpeed;
    protected boolean isAlive = true;
    protected IZombieState currentState;
    protected ZombieEventBus eventBus;

    public Plant target;
    public boolean eating = false;

    protected boolean resetAnim  = false;
    protected boolean spawnHead  = false;
    protected boolean finalDeath = false;
    protected boolean fixAnim    = false;
    protected boolean eatOnce    = false;

    public GreenfootImage[] headless, headlesseating, fall;

    public Zombie(ZombieConfig config) {
        this.config    = config;
        this.maxHp     = config.maxHp;
        this.hp        = config.maxHp;
        this.walkSpeed = config.walkSpeed;
        this.eventBus  = new ZombieEventBus();

        headless       = importSprites(ZombieAssets.SHARED_HEADLESS.path,      ZombieAssets.SHARED_HEADLESS.count);
        headlesseating = importSprites(ZombieAssets.SHARED_HEADLESS_EAT.path, ZombieAssets.SHARED_HEADLESS_EAT.count);
        fall           = importSprites(ZombieAssets.SHARED_FALL.path,          ZombieAssets.SHARED_FALL.count);
    }

    @Override
    public void addedToWorld(World world) {
        if (world instanceof PlayScene) {
            this.playScene = (PlayScene) world;
            world.addObject(new HealthBar(this, 60), getX(), getY() - 50);
        }
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

    @Override
    public void hit(int dmg) {
        if (!isAlive) return;
        this.hp -= dmg;
        eventBus.publishHit(this, dmg);
        if (this.hp <= 0) {
            this.hp     = 0;
            this.isAlive = false;
        }
    }

    @Override
    public boolean isLiving() {
        return hp > 0 && isAlive && getWorld() != null;
    }

    @Override
    public int getHp() { return hp; }

    @Override
    public int getYPos() {
        if (playScene == null || playScene.GridManager == null || getWorld() == null) return -1;
        return playScene.GridManager.getGridY(getX(), getY());
    }

    public void walk() {
        if (getWorld() != null) move(-walkSpeed);
    }

    public void setState(IZombieState newState) {
        if (this.currentState != null) this.currentState.exit();
        this.currentState = newState;
        this.currentState.enter();
        eventBus.publishStateChanged(this, newState);
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
            if (p == null || p.getWorld() == null || p.getHp() <= 0) continue;
            
            if (currentX - p.getX() < 40 && currentX - p.getX() > 0) {
                target = p;
                return true;
            }
        }
        target = null;
        return false;
    }

    public void playEating() {
        if (target == null || target.getWorld() == null || target.getHp() <= 0) {
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

    protected void deathAnim() {
        if (getWorld() == null) return;

        if (!resetAnim) {
            frame = 0;
            resetAnim = true;
            removeFromRow();
            eventBus.publishDeath(this);
            target = null;
        }

        if (finalDeath) {
            if (!fixAnim) {
                fixAnim = true;
                AudioManager.playSound(80, false, "zombie_falling_1.mp3", "zombie_falling_2.mp3");
                getWorld().addObject(new fallingZombie(fall), getX(), getY());
                getWorld().removeObject(this);
            }
        } else {
            if (!spawnHead) {
                spawnHead = true;
                AudioManager.getInstance().playSound(80, false, "limbs_pop.mp3");
                getWorld().addObject(new Head(), getX() + 10, getY() - 20);
            }

            boolean isAnimFinished;
            if (checkEating()) {
                isAnimFinished = animate(headlesseating, 350, false);
                playEating();
            } else {
                isAnimFinished = animate(headless, 350, false);
                walk();
            }

            if (isAnimFinished || frame >= headless.length - 1) {
                finalDeath = true;
            }
        }
    }

    protected void updateLogic() {
        if (currentState == null) return;
        currentState.update();
        animate(currentState.getAnimation(), 200, true);
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
    
    public GreenfootImage[] getDeadSprites() {
        return this.fall;
    }
    
    protected abstract void handleThresholds();
    public abstract GreenfootImage[] getCurrentAnimation(boolean isEating);
}