import greenfoot.*;

public abstract class Plant extends SpriteAnimator {
    public int cost, maxHp, hp, damage;
    public String name = "";
    public boolean opaque = false;
    public PlayScene playScene;

    protected int currentGridX, currentGridY;
    public boolean isDragging = false, isMerging = false, isTarget = false;

    public Merger merger;
    public Plant targetPlant;
    
    private PlantInputHandler inputHandler;
    private PlantStateManager stateManager;
    private PlantCombatHandler combatHandler;

    @Override
    public void addedToWorld(World world) {
        playScene = (PlayScene) world;
        inputHandler = new PlantInputHandler(this);
        stateManager = new PlantStateManager(this);
        combatHandler = new PlantCombatHandler(this);
        
        syncGridPosition();
        playScene.addObject(new Dirt(), getX(), getY() + 30);
    }

    @Override
    public void act() {
        if (getWorld() == null) return;
        
        stateManager.update();

        if (isMerging) {
            UpgradeManager.handleMergeLogic(this, merger);
            return;
        }

        if (hp <= 0) {
            combatHandler.die();
            return;
        }

        inputHandler.handleMouse();

        if (stateManager.canAct()) {
            update();
        }
        
        updateTransparency();
    }

    public void syncGridPosition() {
        if (playScene != null && playScene.GridManager != null) {
            this.currentGridX = playScene.GridManager.getGridX(getX(), getY());
            this.currentGridY = playScene.GridManager.getGridY(getX(), getY());
        }
    }

    private void updateTransparency() {
        if (getImage() != null) {
            getImage().setTransparency((isDragging || opaque) ? 125 : 255);
        }
    }
    
    public void setMergingTarget(Plant target) {
        if (target == null || target == this) return;
        this.targetPlant = target;
        this.isMerging = true;
        this.merger = new Merger(this, target);
        
        if (playScene != null && playScene.GridManager != null) {
            playScene.GridManager.Board[this.currentGridY][this.currentGridX] = null;
        }
    }

    public void hit(int dmg) {
        if (!isDragging && !isMerging && isLiving()) {
            hp -= dmg;
        }
    }

    public abstract void update();

    public void setGridPosition(int x, int y) { 
        this.currentGridX = x; 
        this.currentGridY = y; 
    }

    public int getXPos() { return currentGridX; }
    public int getYPos() { return currentGridY; }
    public boolean isLiving() { return hp > 0; }
}