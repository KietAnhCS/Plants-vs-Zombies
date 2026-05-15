import greenfoot.*;

public abstract class Plant extends SpriteAnimator implements IDamageable, IGridObject, IEatable {
    public boolean isDragging = false;
    public boolean opaque = false;
    public boolean isMerging = false;
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

    private int arrivedMergers = 0;

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

    public void notifyMergerArrived() {
        arrivedMergers++;
        if (arrivedMergers >= 2) {
            arrivedMergers = 0;
            finalizeUpgrade();
        }
    }

    private void finalizeUpgrade() {
        if (!(getWorld() instanceof PlayScene)) return;
        PlayScene scene = (PlayScene) getWorld();

        Plant upgraded = UpgradeManager.getUpgradeResult(this);
        if (upgraded == null) {
            setState(PlantState.IDLE);
            return;
        }

        int gx = currentGridX;
        int gy = currentGridY;
        int px = getX();
        int py = getY();

        if (scene.GridManager != null) scene.GridManager.removePlant(gx, gy);
        
        upgraded.setGridPosition(gx, gy);
        upgraded.setState(PlantState.IDLE);
        
        scene.addObject(upgraded, px, py);
        if (scene.GridManager != null) scene.GridManager.placePlant(gx, gy, upgraded);
        scene.addObject(new Dirt(), px, py + 30);
        
        scene.removeObject(this);
    }

    public void setMergingTarget(Plant target) {
        if (target == null || target == this) return;
        this.targetPlant = target;
        this.isMerging = true;
        this.setState(PlantState.MERGING);

        if (getWorld() instanceof PlayScene) {
            PlayScene scene = (PlayScene) getWorld();
            if (scene.GridManager != null) {
                scene.GridManager.removePlant(currentGridX, currentGridY);
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
            if (!isMerging && state != PlantState.MERGING) {
                scene.addObject(new Dirt(), getX(), getY() + 30);
            }
            if (state == null) setState(PlantState.IDLE);
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
            setState(PlantState.IDLE);
            return;
        }
        
        double dx = targetPlant.getX() - getX();
        double dy = targetPlant.getY() - getY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        
        if (distance <= 15) {
            if (getWorld() instanceof PlayScene) {
                PlayScene scene = (PlayScene) getWorld();
                scene.addActiveMerger(new Merger(this, targetPlant));
            }
            isMerging = false;
            return;
        }
        
        double speed = 15.0;
        setLocation(getX() + (int)((dx / distance) * speed), getY() + (int)((dy / distance) * speed));
    }

    public abstract void update();
    public abstract String getPlantName();

    @Override
    public void hit(int dmg) {
        if (!isLiving() || isDragging || isMerging || state == PlantState.MERGING) return;
        this.hp -= dmg;
        onHit(dmg);
        if (this.hp <= 0) {
            this.hp = 0;
            onDeath();
        }
    }

    @Override
    public boolean canBeEatenBy(Zombie zombie) {
        return isLiving() && !isDragging && !isMerging && state != PlantState.MERGING;
    }

    protected void onHit(int dmg) {
        if (eventBus != null) eventBus.publishHit(this, dmg);
    }

    protected void onDeath() {
        setState(PlantState.DYING);
        if (eventBus != null) eventBus.publishDeath(this);
        if (getWorld() instanceof PlayScene) {
            PlayScene scene = (PlayScene) getWorld();
            if (scene.GridManager != null) scene.GridManager.removePlant(currentGridX, currentGridY);
        }
        if (combatHandler != null) combatHandler.die();
        if (getWorld() != null) getWorld().removeObject(this);
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
    
    @Override
    public int getYPos() { return currentGridY; }
    
    @Override
    public boolean isLiving() { return hp > 0 && getWorld() != null && state != PlantState.DYING; }
    
    public void setGridPosition(int gx, int gy) { this.currentGridX = gx; this.currentGridY = gy; }
}