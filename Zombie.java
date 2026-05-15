import greenfoot.*;
import java.util.List;

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

    protected boolean resetAnim = false;
    protected boolean spawnHead = false;
    protected boolean finalDeath = false;
    protected boolean fixAnim = false;
    protected boolean eatOnce = false;

    protected int targetY;
    protected boolean movingY = false;

    public GreenfootImage[] headless, headlesseating, fall;

    public Zombie(ZombieConfig config) {
        this.config = config;
        this.maxHp = config.maxHp;
        this.hp = config.maxHp;
        this.walkSpeed = config.walkSpeed;
        this.eventBus = new ZombieEventBus();
        fall = importSprites(ZombieAssets.SHARED_FALL.path, ZombieAssets.SHARED_FALL.count);
    }

    @Override
    public void addedToWorld(World world) {
        if (world instanceof PlayScene) {
            this.playScene = (PlayScene) world;
            world.addObject(new HealthBar(this, 60), getX(), getY() - 50);
        }
    }

    @Override
    public void update() {
        if (getWorld() == null) return;
        if (!getWorld().getObjects(Overlay.class).isEmpty()) return;
        if (isLiving()) {
            handleSliding();
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
            this.hp = 0;
            this.isAlive = false;
        }
    }

    @Override
    public boolean isLiving() {
        return hp > 0 && isAlive && getWorld() != null;
    }

    @Override
    public int getHp() {
        return hp;
    }

    @Override
    public int getYPos() {
        if (playScene == null || playScene.GridManager == null || getWorld() == null) return -1;
        return playScene.GridManager.getGridY(getX(), getY());
    }

    public void walk() {
        if (getWorld() != null && !movingY) {
            move(-walkSpeed);
        }
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
            rowList.remove(this);
        }
    }

    public void startLaneChange(int newY) {
        this.targetY = newY;
        this.movingY = true;
    }

    public boolean isChangingLane() {
        return movingY;
    }

    public void forceChangeLane(int newY) {
        removeFromRow();
        setLocation(getX(), newY);
        if (playScene != null && playScene.level != null) {
            int newRow = playScene.GridManager.getGridY(getX(), newY);
            if (newRow >= 0 && newRow < playScene.level.zombieRow.size()) {
                playScene.level.zombieRow.get(newRow).add(this);
            }
        }
    }

    protected void handleSliding() {
        if (movingY) {
            if (Math.abs(getY() - targetY) > 2) {
                int step = (targetY > getY()) ? 2 : -2;
                setLocation(getX(), getY() + step);
            } else {
                forceChangeLane(targetY);
                movingY = false;
            }
        }
    }

    public GreenfootImage[] getDeadSprites() {
        return this.fall;
    }

    protected abstract void deathAnim();
    protected abstract void handleThresholds();
    public abstract GreenfootImage[] getCurrentAnimation(boolean isEating);
}