import greenfoot.*;

public class SellShovel extends SpriteAnimator {
    private final int startX, startY;
    private final Shovel parent;
    private final PlayScene playScene;
    private Plant lastHighlighted = null;

    public SellShovel(Shovel parent, PlayScene playScene, int x, int y) {
        this.parent = parent;
        this.playScene = playScene;
        this.startX = x;
        this.startY = y;
    }

    @Override
    public void act() {
        if (getWorld() == null) return;
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
        
        if (gx != -1 && gy != -1 && playScene.GridManager.Board[gy][gx] != null) {
            dig(gx, gy);
        } else {
            AudioManager.getInstance().playSound(80, false, "tap.mp3");
        }
        exit();
    }

    private void dig(int gx, int gy) {
        Plant p = playScene.GridManager.Board[gy][gx];
        if (p != null) {
            int px = p.getX();
            int py = p.getY();
            
            String rawName = p.getClass().getSimpleName(); 
            String formattedName = rawName.replaceAll("([a-z0-9])([A-Z])", "$1_$2")
                                         .replaceAll("([A-Z])([A-Z][a-z])", "$1_$2")
                                         .replace(" ", "_")
                                         .toUpperCase();

            if (formattedName.contains("BONKCHOY")) formattedName = formattedName.replace("BONKCHOY", "BONK_CHOY");
            if (formattedName.contains("GATLINGPEA")) formattedName = formattedName.replace("GATLINGPEA", "GATLING_PEA");
            if (formattedName.contains("POTATOMINE")) formattedName = formattedName.replace("POTATOMINE", "POTATO_MINE");
            if (formattedName.matches(".*\\d$")) formattedName = formattedName.replaceAll("(\\d)$", "_$1");

            int refundValue = 0;
            try {
                refundValue = PlantType.valueOf(formattedName).cost;
            } catch (Exception e) {
                refundValue = p.getCost() > 0 ? p.getCost() : 25;
            }

            playScene.GridManager.Board[gy][gx] = null;
            playScene.removeObject(p);
            
            Sun s = new Sun(refundValue, true);
            playScene.addObject(s, px, py);
            
            AudioManager.getInstance().playSound(80, false, "plant.mp3");
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