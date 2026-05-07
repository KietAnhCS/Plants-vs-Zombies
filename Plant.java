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
        if (!(world instanceof PlayScene)) return;
        
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
            if (combatHandler != null) combatHandler.die();
            return;
        }

        if (inputHandler != null) inputHandler.handleMouse();

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
        GreenfootImage img = getImage();
        if (img != null) {
            img.setTransparency((isDragging || opaque) ? 125 : 255);
        }
    }
    
    public void setMergingTarget(Plant target) {
        if (target == null || target == this || playScene == null) return;
        
        this.targetPlant = target;
        this.isMerging = true;
        this.merger = new Merger(this, target);
        
        if (playScene.GridManager != null) {
            int gx = getXPos();
            int gy = getYPos();
            if (gy >= 0 && gy < playScene.GridManager.Board.length && 
                gx >= 0 && gx < playScene.GridManager.Board[0].length) {
                playScene.GridManager.Board[gy][gx] = null;
            }
        }
    }

    public void hit(int dmg) {
        if (isLiving() && !isDragging && !isMerging) {
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
    public boolean isLiving() { return hp > 0 && getWorld() != null; }
}