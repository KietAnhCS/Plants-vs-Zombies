import greenfoot.*;
import java.util.*;
import java.util.function.Supplier;
import java.util.Random;

public class AugmentCard extends Actor {
    private WaveManager manager;
    private String type;
    private boolean hovered = false;
    private static final int W_NORMAL = 250, H_NORMAL = 330;
    private static final int W_HOVER  = 270, H_HOVER  = 350;

    public AugmentCard(WaveManager manager, String type, Object dummy) {
        this.manager = manager;
        this.type = type;
        updateImage(W_NORMAL, H_NORMAL);
    }

    public void act() {
        handleHover();
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse != null && mouse.getButton() == 1 && Greenfoot.mouseClicked(this)) {
            AudioManager.getInstance().playSound(80, false, "gravebutton.mp3");
            applyAugmentEffect();
            clearUI();
            manager.nextWave();
        }
    }

    private void handleHover() {
        if (Greenfoot.mouseMoved(this)) {
            if (!hovered) {
                hovered = true;
                updateImage(W_HOVER, H_HOVER);
            }
        } else if (Greenfoot.mouseMoved(null)) {
            if (hovered) {
                hovered = false;
                updateImage(W_NORMAL, H_NORMAL);
            }
        }
    }

    private void updateImage(int w, int h) {
        GreenfootImage img = new GreenfootImage(type + ".png");
        img.scale(w, h);
        setImage(img);
    }

    private void applyAugmentEffect() {
        PlayScene world = (PlayScene) getWorld();
        if (world == null) return;

        if (type.equals("rerollcard")) {
            List<RollButton> buttons = world.getObjects(RollButton.class);
            if (!buttons.isEmpty()) {
                RollButton rb = buttons.get(0);
                rb.addFreeRolls(6); 
            }
        }
        else if (type.equals("TD")) {
            world.increasePlantSlots(3);
        } else if (type.equals("HM")) {
            GridManager gm = world.GridManager;
            List<Integer> emptyCols = new ArrayList<>();
        
            for (int c = 0; c < 9; c++) {
                if (gm.Board[5][c] == null) emptyCols.add(c);
            }
        
            if (!emptyCols.isEmpty()) {
                Random rand = new Random();
                int randomColIndex = rand.nextInt(emptyCols.size());
                int col = emptyCols.get(randomColIndex);

                Plant p;
                int r = rand.nextInt(2);
                switch (r) {
                    case 0: p = new Cactus2(); break;
                    case 1: p = new BonkChoy2(); break;
                    case 2: p = new BonkChoy3(); break;
                    case 3: p = new GatlingPea(); break;
                    default: p = new Peashooter(); break;
                }
        
                gm.Board[5][col] = p;
                world.addObject(p, gm.getXCoord(col, 5), gm.getYCoord(col, 5));
                
                AudioManager.getInstance().playSound(80, false, "achievement.mp3");
            }
        }
    }

    private void clearUI() {
        World world = getWorld();
        if (world == null) return;
        world.removeObjects(world.getObjects(AugmentCard.class));
        world.removeObjects(world.getObjects(Overlay.class));
    }
}