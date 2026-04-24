import greenfoot.*;
import java.util.List;
import java.util.ArrayList;

class Merger {
    private Actor mover;
    private Actor target;
    private double speed = 20.0;

    public Merger(Actor mover, Actor target) {
        this.mover = mover;
        this.target = target;
    }

    public boolean update() {
        if (target == null || target.getWorld() == null || mover.getWorld() == null) return true;
        
        int dx = target.getX() - mover.getX();
        int dy = target.getY() - mover.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        
        if (distance < speed) {
            mover.setLocation(target.getX(), target.getY());
            return true;
        } else {
            double angle = Math.atan2(dy, dx);
            mover.setLocation(mover.getX() + (int)(Math.cos(angle) * speed), 
                             mover.getY() + (int)(Math.sin(angle) * speed));
            return false;
        }
    }
}

public abstract class Plant extends SpriteAnimator {
    public int cost, maxHp, hp, damage;
    public boolean isAlive = true, opaque = false;
    public PlayScene PlayScene;

    private boolean isDragging = false;
    private int startGridX, startGridY;
    private int currentGridX, currentGridY;

    private Merger merger;
    public Plant targetPlant;
    public boolean isMerging = false;
    public boolean isTarget = false;

    public void setMergingTarget(Plant target) {
        if (target == null) return;

        if (PlayScene != null && PlayScene.board != null) {
            PlayScene.board.Board[this.currentGridY][this.currentGridX] = null;
        }

        this.targetPlant = target;
        target.isTarget = true;
        this.merger = new Merger(this, target);
        this.isMerging = true;
    }

    public void act() {
        World worldRef = getWorld();
        if (worldRef == null) return;
        PlayScene = (PlayScene) worldRef;

        if (isMerging) {
            if (merger.update()) {
                onMergeReached();
            }
            return;
        }

        if (!PlayScene.getObjects(Overlay.class).isEmpty()) return;

        handleMouse();
        
        if (getWorld() == null) return;

        if (isLiving()) {
            if (!isDragging) {
                update();
            }
            if (getImage() != null) {
                getImage().setTransparency((isDragging || opaque) ? 125 : 255);
            }
        } else {
            handleDeath();
        }
    }

    private void onMergeReached() {
        World worldRef = getWorld();
        if (worldRef == null) return;
        PlayScene world = (PlayScene) worldRef;

        Plant target = this.targetPlant; 
        if (target == null) return;

        int gx = target.getXPos();
        int gy = target.getYPos();
        int tx = target.getX();
        int ty = target.getY();

        world.removeObject(this);

        List<Plant> allPlants = world.getObjects(Plant.class);
        int activeMergers = 0;
        for (Plant p : allPlants) {
            if (p.getClass() == this.getClass() && p.isMerging && p.targetPlant == target) {
                activeMergers++;
            }
        }

        if (activeMergers == 0 && target.getWorld() != null) {
            Plant upgraded = UpgradeManager.getUpgradeResult(target);
            world.board.removePlant(gx, gy);

            if (upgraded != null) {
                world.addObject(upgraded, tx, ty);
                world.board.updateBoardData(gx, gy, upgraded);
                world.checkAndCombine(upgraded);
            }
        }
    }

    private void handleDeath() {
        World worldRef = getWorld();
        if (worldRef == null) return;

        AudioPlayer.play(80, "gulp.mp3");
        if (PlayScene.board != null) {
            PlayScene.board.removePlant(currentGridX, currentGridY);
        }
        worldRef.removeObject(this);
    }

    private void handleMouse() {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse == null) return;
    
        if (!isDragging && Greenfoot.mousePressed(this)) {
            if (mouse.getButton() == 1) {
                if (isTarget || isMerging) return;
                isDragging = true;
                startGridX = currentGridX;
                startGridY = currentGridY;
            }
        }
    
        if (isDragging) {
            if (Greenfoot.mouseDragged(null)) {
                setLocation(mouse.getX(), mouse.getY());
            }
            
            if (Greenfoot.mouseClicked(null) || Greenfoot.mouseClicked(this)) {
                isDragging = false;
                if (getWorld() == null) return;
            
                int nx = PlayScene.board.getGridX(mouse.getX(), mouse.getY());
                int ny = PlayScene.board.getGridY(mouse.getX(), mouse.getY());
                
                if (nx < 0 || ny < 0) {
                    
                    int ox = PlayScene.board.getXCoord(startGridX, startGridY) + 
                             (PlayScene.board.getXCoord(startGridX+1, startGridY) - PlayScene.board.getXCoord(startGridX, startGridY))/2;
                    int oy = PlayScene.board.getYCoord(startGridX, startGridY) + 
                             (PlayScene.board.getYCoord(startGridX, startGridY+1) - PlayScene.board.getYCoord(startGridX, startGridY))/2;
                    setLocation(ox, oy);
                } else if (PlayScene.board.placePlant(nx, ny, this)) {
                    currentGridX = nx;
                    currentGridY = ny;
                    PlayScene.checkAndCombine(this);
                    
                } else {
                    
                    int ox = PlayScene.board.getXCoord(startGridX, startGridY) + 
                             (PlayScene.board.getXCoord(startGridX+1, startGridY) - PlayScene.board.getXCoord(startGridX, startGridY))/2;
                    int oy = PlayScene.board.getYCoord(startGridX, startGridY) + 
                             (PlayScene.board.getYCoord(startGridX, startGridY+1) - PlayScene.board.getYCoord(startGridX, startGridY))/2;
                    setLocation(ox, oy);
                }
            }
        }
    }

    public void hit(int dmg) { 
        if (isDragging || isMerging) return;
        hp -= dmg; 
    }

    public abstract void update();

    public int getXPos() { return currentGridX; }
    public int getYPos() { return currentGridY; }

    @Override
    public void addedToWorld(World world) {
        PlayScene = (PlayScene)world;
        currentGridX = PlayScene.board.getGridX(getX(), getY());
        currentGridY = PlayScene.board.getGridY(getX(), getY());
        PlayScene.addObject(new Dirt(), getX(), getY() + 30);
    }

    public boolean isLiving() { return hp > 0; }
    
}