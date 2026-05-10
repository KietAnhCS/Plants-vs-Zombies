import greenfoot.*;

public abstract class Plant extends SpriteAnimator {
    public boolean isDragging = false;
    public boolean opaque = false;
    public boolean isMerging = false;
    public boolean isTarget = false;
    protected Plant targetPlant = null;

    private int hp;
    private int maxHp;
    private int damage;
    private int cost;
    private PlantState state;

    private PlantEventBus eventBus;
    private PlantInputHandler inputHandler;
    private PlantStateManager stateManager;
    private PlantCombatHandler combatHandler;

    protected int currentGridX;
    protected int currentGridY;

    private int lastAlpha = -1;

    public int getHp() { return hp; }
    public int getMaxHp() { return maxHp; }
    public int getDamage() { return damage; }
    public int getCost() { return cost; }

    public void setHp(int hp) { this.hp = hp; }
    public void setMaxHp(int maxHp) { this.maxHp = maxHp; }
    public void setDamage(int damage) { this.damage = damage; }
    public void setCost(int cost) { this.cost = cost; }

    public PlantState getState() { return state; }

    public void setState(PlantState state) {
        this.state = state;
        this.isDragging = (state == PlantState.DRAGGING);
        if (eventBus != null) eventBus.publishStateChanged(this, state);
    }

    public void setEventBus(PlantEventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void setMergingTarget(Plant target) {
        if (target == null || target == this) return;
        this.targetPlant = target;
        this.isMerging = true;

        if (getWorld() instanceof PlayScene) {
            PlayScene scene = (PlayScene) getWorld();
            if (scene.GridManager != null) {
                scene.GridManager.Board[currentGridY][currentGridX] = null;
            }
        }
    }

    @Override
    public void addedToWorld(World world) {
        super.addedToWorld(world);
        if (world instanceof PlayScene) {
            PlayScene scene = (PlayScene) world;

            if (this.eventBus == null && scene.getUpgradeManager() != null) {
                this.eventBus = scene.getUpgradeManager().getEventBus();
            }

            this.inputHandler = new PlantInputHandler(this);
            this.stateManager = new PlantStateManager(this);
            this.combatHandler = new PlantCombatHandler(this);

            syncGridPosition();

            if (!isMerging) {
                scene.addObject(new Dirt(), getX(), getY() + 30);
            }

            if (state == null) {
                setState(PlantState.IDLE);
            }
        }
    }

    @Override
    public void act() {
        if (getWorld() == null) return;

        updateTransparency();

        if (isMerging && targetPlant != null) {
            handleMergeMovement();
            return;
        }

        if (stateManager != null) stateManager.update();
        if (inputHandler != null) inputHandler.handleMouse();

        if (stateManager != null && stateManager.canAct() && isLiving() && !isDragging) {
            update();
        }
    }

    private void updateTransparency() {
        if (getImage() == null) return;
        int alpha = isDragging ? 125 : (opaque ? 160 : 255);
        if (alpha == lastAlpha) return;
        lastAlpha = alpha;
        GreenfootImage copy = new GreenfootImage(getImage());
        copy.setTransparency(alpha);
        setImage(copy);
    }

    protected void handleMergeMovement() {
        if (targetPlant == null || targetPlant.getWorld() == null) {
            isMerging = false;
            return;
        }
    
        double tx = targetPlant.getExactX();
        double ty = targetPlant.getExactY();
        double sx = getExactX();
        double sy = getExactY();
        double dx = tx - sx;
        double dy = ty - sy;
        double distance = Math.sqrt(dx * dx + dy * dy);
    
        if (distance <= 15) {
            if (getWorld() instanceof PlayScene) {
                PlayScene scene = (PlayScene) getWorld();
                scene.addActiveMerger(new Merger(this, targetPlant, scene.getUpgradeManager()));
            }
            getWorld().removeObject(this);
            return;
        }
    
        double speed = 15.0;
        setLocation(sx + (dx / distance) * speed, sy + (dy / distance) * speed);
    }

    public abstract void update();

    public void hit(int dmg) {
        if (!isLiving() || isDragging || isMerging) return;
        this.hp -= dmg;
        onHit(dmg);
        if (this.hp <= 0) {
            this.hp = 0;
            onDeath();
        }
    }

    protected void onHit(int dmg) {
        if (eventBus != null) eventBus.publishHit(this, dmg);
    }

    protected void onDeath() {
        setState(PlantState.DYING);
        if (eventBus != null) eventBus.publishDeath(this);

        if (getWorld() instanceof PlayScene) {
            PlayScene scene = (PlayScene) getWorld();
            if (scene.GridManager != null) {
                scene.GridManager.removePlant(currentGridX, currentGridY);
            }
        }

        if (combatHandler != null) combatHandler.die();
        if (getWorld() != null) {
            getWorld().removeObject(this);
        }
    }

    public void syncGridPosition() {
        if (getWorld() instanceof PlayScene) {
            PlayScene scene = (PlayScene) getWorld();
            if (scene.GridManager != null) {
                int[] pos = scene.GridManager.getGridPos(getX(), getY());
                this.currentGridX = pos[0];
                this.currentGridY = pos[1];
            }
        }
    }

    public int getXPos() { return currentGridX; }
    public int getYPos() { return currentGridY; }

    public boolean isLiving() {
        return hp > 0 && getWorld() != null && state != PlantState.DYING;
    }

    public void setGridPosition(int gx, int gy) {
        this.currentGridX = gx;
        this.currentGridY = gy;
    }
}