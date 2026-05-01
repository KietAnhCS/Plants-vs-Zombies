import greenfoot.*;

public class SellShovel extends SpriteAnimator {
    private final int startX, startY;
    private final Shovel parent;
    private final PlayScene playScene;
    private Plant lastHighlighted = null;

    public SellShovel(Shovel parent, PlayScene playScene, int x, int y) {
        this.parent    = parent;
        this.playScene = playScene;
        this.startX    = x;
        this.startY    = y;
    }

    @Override
    public void act() {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse == null) return;

        setLocation(mouse.getX(), mouse.getY());
        updateHighlight(mouse.getX(), mouse.getY());

        if (Greenfoot.mouseClicked(null)) {
            handleDrop(mouse.getX(), mouse.getY());
        }
    }

    private void updateHighlight(int x, int y) {
        if (lastHighlighted != null) {
            lastHighlighted.opaque = false;
            lastHighlighted = null;
        }
        Plant p = getPlantAt(x, y);
        if (p != null) {
            p.opaque = true;
            lastHighlighted = p;
        }
    }

    private void handleDrop(int x, int y) {
        int gx = playScene.GridManager.getGridX(x, y);
        int gy = playScene.GridManager.getGridY(x, y);
        if (gx != -1 && gy != -1) {
            dig(gx, gy);
        } else {
            AudioManager.playSound(80, false, "tap.mp3");
        }
        exit();
    }

    private void dig(int gx, int gy) {
        Plant p = playScene.GridManager.Board[gy][gx];
        if (p != null) {
            playScene.GridManager.removePlant(gx, gy);
            playScene.getSunManager().add(30);
            AudioManager.playSound(80, false, "plant.mp3");
        } else {
            AudioManager.playSound(80, false, "tap.mp3");
        }
    }

    private Plant getPlantAt(int x, int y) {
        int gx = playScene.GridManager.getGridX(x, y);
        int gy = playScene.GridManager.getGridY(x, y);
        if (gx == -1 || gy == -1) return null;
        return playScene.GridManager.Board[gy][gx];
    }

    private void exit() {
        if (lastHighlighted != null) lastHighlighted.opaque = false;
        if (parent != null) parent.setSelected(false);
        playScene.removeObject(this);
    }
}