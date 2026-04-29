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
        if (target == null || target.getWorld() == null || mover == null || mover.getWorld() == null) return true;

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
    public PlayScene playScene;

    private boolean isDragging = false;
    private int startGridX, startGridY;
    private int currentGridX, currentGridY;

    private Merger merger;
    public Plant targetPlant;
    public boolean isMerging = false;
    public boolean isTarget = false;

    @Override
    public void addedToWorld(World world) {
        playScene = (PlayScene) world;
        currentGridX = playScene.GridManager.getGridX(getX(), getY());
        currentGridY = playScene.GridManager.getGridY(getX(), getY());
        playScene.addObject(new Dirt(), getX(), getY() + 30);
    }

    @Override
    public void act() {
        if (getWorld() == null) return;
        playScene = (PlayScene) getWorld();

        if (isMerging) {
            if (merger != null && merger.update()) onMergeReached();
            return;
        }

        if (!playScene.getObjects(Overlay.class).isEmpty()) return;

        if (hp <= 0) {
            handleDeath();
            return;
        }

        handleMouse();

        if (getWorld() != null && isLiving()) {
            if (!isDragging && currentGridY < 5) update();
            if (getImage() != null) {
                getImage().setTransparency((isDragging || opaque) ? 125 : 255);
            }
        }
    }

    public void setMergingTarget(Plant target) {
        if (target == null || target == this) return;
        if (playScene != null && playScene.GridManager != null) {
            playScene.GridManager.Board[this.currentGridY][this.currentGridX] = null;
        }
        this.targetPlant = target;
        this.merger = new Merger(this, target);
        this.isMerging = true;
    }

    private void onMergeReached() {
        if (getWorld() == null) return;
        Plant target = this.targetPlant;
        playScene.removeObject(this);

        if (target == null || target.getWorld() == null) return;

        List<Plant> allPlants = playScene.getObjects(Plant.class);
        boolean stillWaiting = false;
        for (Plant p : allPlants) {
            if (p != this && p.isMerging && p.targetPlant == target) {
                stillWaiting = true;
                break;
            }
        }

        if (!stillWaiting) {
            int gx = target.getXPos();
            int gy = target.getYPos();
            int tx = target.getX();
            int ty = target.getY();

            Plant upgraded = UpgradeManager.getUpgradeResult(target);
            playScene.removeObject(target);
            playScene.GridManager.removePlant(gx, gy);

            if (upgraded != null) {
                playScene.addObject(upgraded, tx, ty);
                playScene.GridManager.Board[gy][gx] = upgraded;
                playScene.checkAndCombine(upgraded);
            }
        }
    }

    private void handleDeath() {
        if (getWorld() == null) return;
        AudioManager.playSound(80, false, "gulp.mp3");
        if (playScene != null && playScene.GridManager != null) {
            playScene.GridManager.removePlant(currentGridX, currentGridY);
        }
        getWorld().removeObject(this);
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

            if (Greenfoot.mouseClicked(null)) {
                isDragging = false;
                int nx = playScene.GridManager.getGridX(getX(), getY());
                int ny = playScene.GridManager.getGridY(getX(), getY());

                if (nx >= 0 && ny >= 0 && playScene.GridManager.placePlant(nx, ny, this)) {
                    currentGridX = nx;
                    currentGridY = ny;
                    playScene.checkAndCombine(this);
                } else {
                    returnToOldPosition();
                }
            }
        }
    }

    private void returnToOldPosition() {
        if (playScene != null && playScene.GridManager != null) {
            setLocation(
                playScene.GridManager.getXCoord(startGridX, startGridY),
                playScene.GridManager.getYCoord(startGridX, startGridY)
            );
            currentGridX = startGridX;
            currentGridY = startGridY;
        }
    }

    public void hit(int dmg) {
        if (isDragging || isMerging || !isLiving()) return;
        hp -= dmg;
    }

    public abstract void update();

    public int getXPos() { return currentGridX; }
    public int getYPos() { return currentGridY; }
    public boolean isLiving() { return hp > 0; }
}