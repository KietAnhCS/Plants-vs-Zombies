import greenfoot.*;
import java.util.List;

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
        }

        double angle = Math.atan2(dy, dx);
        mover.setLocation(
            mover.getX() + (int)(Math.cos(angle) * speed),
            mover.getY() + (int)(Math.sin(angle) * speed)
        );
        return false;
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

    @Override
    public void addedToWorld(World world) {
        PlayScene = (PlayScene) world;
        currentGridX = PlayScene.GridManager.getGridX(getX(), getY());
        currentGridY = PlayScene.GridManager.getGridY(getX(), getY());
        PlayScene.addObject(new Dirt(), getX(), getY() + 30);
    }

    @Override
    public void act() {
        World worldRef = getWorld();
        if (worldRef == null) return;
        PlayScene = (PlayScene) worldRef;

        if (isMerging) {
            if (merger.update()) onMergeReached();
            return;
        }

        if (!PlayScene.getObjects(Overlay.class).isEmpty()) return;

        handleMouse();

        if (getWorld() == null) return;

        if (isLiving()) {
            if (!isDragging && currentGridY < 5) update();
            if (getImage() != null) {
                getImage().setTransparency((isDragging || opaque) ? 125 : 255);
            }
        } else {
            handleDeath();
        }
    }

    public void setMergingTarget(Plant target) {
        if (target == null) return;
        if (PlayScene != null && PlayScene.GridManager != null) {
            PlayScene.GridManager.Board[this.currentGridY][this.currentGridX] = null;
        }
        this.targetPlant = target;
        target.isTarget = true;
        this.merger = new Merger(this, target);
        this.isMerging = true;
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
            world.GridManager.removePlant(gx, gy);
            if (upgraded != null) {
                world.addObject(upgraded, tx, ty);
                world.GridManager.Board[gy][gx] = upgraded;
                world.checkAndCombine(upgraded);
            }
        }
    }

    private void handleDeath() {
        World worldRef = getWorld();
        if (worldRef == null) return;
        AudioManager.playSound(80,false, "gulp.mp3");
        if (PlayScene.GridManager != null) {
            PlayScene.GridManager.removePlant(currentGridX, currentGridY);
        }
        worldRef.removeObject(this);
    }

    private void handleMouse() {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse == null) return;

        if (!isDragging && Greenfoot.mousePressed(this) && mouse.getButton() == 1) {
            if (isTarget || isMerging) return;
            isDragging = true;
            startGridX = currentGridX;
            startGridY = currentGridY;
        }

        if (isDragging) {
            if (Greenfoot.mouseDragged(null)) {
                setLocation(mouse.getX(), mouse.getY());
            }

            if (Greenfoot.mouseClicked(null) || Greenfoot.mouseClicked(this)) {
                isDragging = false;
                if (getWorld() == null) return;

                int nx = PlayScene.GridManager.getGridX(mouse.getX(), mouse.getY());
                int ny = PlayScene.GridManager.getGridY(mouse.getX(), mouse.getY());

                if (nx < 0 || ny < 0) {
                    returnToOldPosition();
                } else if (PlayScene.GridManager.placePlant(nx, ny, this)) {
                    currentGridX = nx;
                    currentGridY = ny;
                    PlayScene.checkAndCombine(this);
                } else {
                    returnToOldPosition();
                }
            }
        }
    }

    private void returnToOldPosition() {
        setLocation(
            PlayScene.GridManager.getXCoord(startGridX, startGridY),
            PlayScene.GridManager.getYCoord(startGridX, startGridY)
        );
        currentGridX = startGridX;
        currentGridY = startGridY;
    }

    public void hit(int dmg) {
        if (isDragging || isMerging) return;
        hp -= dmg;
    }

    public abstract void update();

    public int getXPos() { return currentGridX; }
    public int getYPos() { return currentGridY; }
    public boolean isLiving() { return hp > 0; }
}