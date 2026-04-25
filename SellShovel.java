import greenfoot.*;
import java.util.List;

public class SellShovel extends SpriteAnimator {
    private boolean isDragging = true;
    private int startX, startY;
    private PlayScene playScene;
    private Plant lastHighlighted = null;
    private Shovel parentDe;

    public SellShovel(Shovel parent, int x, int y) {
        this.parentDe = parent;
        this.startX = x;
        this.startY = y;
        
    }

    protected void addedToWorld(World world) {
        playScene = (PlayScene) world;
    }

    public void act() {
        handleMouse();
    }

    private void handleMouse() {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse == null) return;

        if (isDragging) {
            if (Greenfoot.mouseDragged(null) || true) {
                setLocation(mouse.getX(), mouse.getY());
                checkHighlight(mouse.getX(), mouse.getY());
            }

            if (Greenfoot.mouseClicked(null)) {
                isDragging = false;
                int gx = playScene.board.getGridX(mouse.getX(), mouse.getY());
                int gy = playScene.board.getGridY(mouse.getX(), mouse.getY());

                if (gx != -1 && gy != -1) {
                    executeDig(gx, gy);
                } else {
                    setLocation(startX, startY);
                }
                exitShovel();
            }
        }
    }

    private void checkHighlight(int x, int y) {
        if (lastHighlighted != null) {
            lastHighlighted.opaque = false;
            lastHighlighted = null;
        }
        if (playScene.board == null) return;
        int gx = playScene.board.getGridX(x, y);
        int gy = playScene.board.getGridY(x, y);
        if (gx != -1 && gy != -1) {
            Plant p = playScene.board.Board[gy][gx];
            if (p != null) {
                p.opaque = true;
                lastHighlighted = p;
            }
        }
    }

    private void executeDig(int gx, int gy) {
        Plant p = playScene.board.Board[gy][gx];
        if (p != null) {
            playScene.board.removePlant(gx, gy);
            if (playScene.seedbank != null) {
                playScene.seedbank.addSun(30);
            }
            AudioPlayer.play(80, "plant.mp3");
        } else {
            AudioPlayer.play(80, "tap.mp3");
        }
    }

    private void exitShovel() {
        if (lastHighlighted != null) {
            lastHighlighted.opaque = false;
        }
        if (parentDe != null) {
            parentDe.setSelected(false);
        }
        playScene.removeObject(this);
    }
}