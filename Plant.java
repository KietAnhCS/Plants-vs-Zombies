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
        if (mover == null || mover.getWorld() == null || target == null || target.getWorld() == null) {
            return true;
        }

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
    public String name = "";
    public boolean isAlive = true, opaque = false;
    public PlayScene playScene;

    protected int startGridX, startGridY;
    protected int currentGridX, currentGridY;

    private Merger merger;
    public Plant targetPlant;
    protected boolean isDragging = false; 
    protected boolean isMerging = false;
    protected boolean isTarget = false;

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
            if (merger != null && merger.update()) {
                onMergeReached();
            }
            return;
        }

        if (!playScene.getObjects(Overlay.class).isEmpty()) return;

        if (hp <= 0) {
            handleDeath();
            return;
        }

        handleMouse();

        if (getWorld() != null && isLiving()) {
            if (!isDragging) {
                String className = getClass().getSimpleName().toUpperCase();
                boolean isPotato = className.contains("POTATOMINE");
                boolean isOnBench = (currentGridY == 5);

                if (!(isPotato && isOnBench)) {
                    update();
                }
            }
            if (getImage() != null) {
                getImage().setTransparency((isDragging || opaque) ? 125 : 255);
            }
        }
    }

    public void setMergingTarget(Plant target) {
        if (target == null || target == this || getWorld() == null) return;
        if (playScene != null && playScene.GridManager != null) {
            playScene.GridManager.Board[this.currentGridY][this.currentGridX] = null;
        }
        this.targetPlant = target;
        this.merger = new Merger(this, target);
        this.isMerging = true;
    }

    private void onMergeReached() {
        World world = getWorld();
        if (world == null) return;
        
        Plant target = this.targetPlant;
        world.removeObject(this);

        if (target == null || target.getWorld() == null) return;

        List<Plant> allPlants = world.getObjects(Plant.class);
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
            
            if (target.getWorld() != null) {
                world.removeObject(target);
            }
            
            if (playScene != null && playScene.GridManager != null) {
                playScene.GridManager.removePlant(gx, gy);
                if (upgraded != null) {
                    world.addObject(upgraded, tx, ty);
                    playScene.GridManager.Board[gy][gx] = upgraded;
                    playScene.checkAndCombine(upgraded);
                }
            }
        }
    }

    private void handleDeath() {
        World world = getWorld();
        if (world == null) return;
        
        AudioManager.playSound(80, false, "gulp.mp3");
        if (playScene != null && playScene.GridManager != null) {
            playScene.GridManager.removePlant(currentGridX, currentGridY);
        }
        world.removeObject(this);
    }

    private void handleMouse() {
        if (getWorld() == null) return;
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
                if (playScene != null && playScene.GridManager != null) {
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