import greenfoot.*;
import java.util.*;

public abstract class Zombie extends SpriteAnimator implements IDamageable, IGridObject {
    private int hp;
    private int maxHp;
    private double walkSpeed;
    private boolean isAlive;
    private IZombieState currentState;
    public ZombieConfig config;
    private ZombieEventBus eventBus;
    private IDeathHandler deathHandler;
    private IAudioService audio;
    private IEatable target;
    private int eatTimer = 0;
    private final int EAT_COOLDOWN = 30;

    public Zombie(ZombieConfig config) {
        this.config = config;
        this.maxHp = config.maxHp;
        this.hp = config.maxHp;
        this.walkSpeed = config.walkSpeed;
        this.isAlive = true;
        this.eventBus = new ZombieEventBus();
        this.audio = AudioManager.getInstance();
        this.deathHandler = new ZombieDeathHandler(audio, eventBus);
    }

    @Override
    public void act() {
        if (getWorld() == null || !isAlive) return;
        updateLogic();
        handleThresholds();
    }

    @Override
    public void hit(int dmg) {
        if (!isAlive) return;
        this.hp -= dmg;
        eventBus.publishHit(this, dmg);
        if (this.hp <= 0) {
            this.hp = 0;
            this.isAlive = false;
            deathHandler.handleDeath(this);
        }
    }

    public void walk() {
        if (getWorld() != null) {
            setLocation(getX() - (int) walkSpeed, getY());
        }
    }

    @Override
    public boolean isLiving() {
        return isAlive && hp > 0;
    }

    public boolean checkEating() {
        List<IEatable> targets = getObjectsAtOffset(-(getImage().getWidth() / 2), 0, IEatable.class);
        if (!targets.isEmpty()) {
            this.target = targets.get(0);
            return true;
        }
        this.target = null;
        return false;
    }

    public void playEating() {
        if (target != null && target.canBeEatenBy(this)) {
            if (eatTimer <= 0) {
                audio.playSound(80, false, ZombieAssets.SHARED_HEADLESS_EAT.path);
                target.hit(config.damage);
                eventBus.publishAteTarget(this, target);
                eatTimer = EAT_COOLDOWN;
            } else {
                eatTimer--;
            }
        } else {
            eatTimer = 0;
        }
    }

    public void setState(IZombieState newState) {
        if (this.currentState != null) this.currentState.exit();
        this.currentState = newState;
        this.currentState.enter();
        eventBus.publishStateChanged(this, newState);
    }

    public GreenfootImage[] getDeadSprites() {
        return importSprites(config.deadAnimation, config.deadFrames);
    }

    @Override
    public int getYPos() {
        return (getY() - 100) / 100;
    }

    @Override
    public int getHp() {
        return this.hp;
    }

    protected void updateLogic() {
        if (currentState != null) {
            currentState.update();
            if (!(currentState instanceof DeadState)) {
                animate(currentState.getAnimation(), 200, true);
            }
        }
    }

    protected void removeFromRow() {
        if (getWorld() instanceof PlayScene) {
            PlayScene scene = (PlayScene) getWorld();
            List<Zombie> row = scene.level.zombieRow.get(getYPos());
            if (row != null) row.remove(this);
        }
    }

    protected abstract void handleThresholds();

    public void setDeathHandler(IDeathHandler handler) {
        this.deathHandler = handler;
    }
}