import greenfoot.*;
import java.util.*;

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
        if (Greenfoot.mouseClicked(this)) {
            MouseInfo mouse = Greenfoot.getMouseInfo();
            if (mouse != null && mouse.getButton() == 1) {
                AudioManager.playSound(80, false, "gravebutton.mp3");
                applyAugmentEffect();
                clearUI();
                manager.nextWave();
            }
        }
    }

    private void handleHover() {
        if (Greenfoot.mouseMoved(this)) {
            if (!hovered) {
                hovered = true;
                updateImage(W_HOVER, H_HOVER);
            }
        }
        if (Greenfoot.mouseMoved(null) && !Greenfoot.mouseMoved(this)) {
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
            world.getSunManager().add(150);
        } 
        else if (type.equals("TD")) {
            world.increasePlantSlots(5);
        } 
        else if (type.equals("HM")) {
            GridManager gm = world.GridManager;
            List<Integer> emptyCols = new ArrayList<>();
            
            for (int c = 0; c < 9; c++) {
                if (gm.Board[5][c] == null) {
                    emptyCols.add(c);
                }
            }
            
            if (emptyCols.size() >= 1) {
                Collections.shuffle(emptyCols);
                
                int col1 = emptyCols.get(0);
                Plant p1 = new Peashooter();
                gm.Board[5][col1] = p1;
                world.addObject(p1, gm.getXCoord(col1, 5), gm.getYCoord(col1, 5));
                
                if (emptyCols.size() >= 2) {
                    int col2 = emptyCols.get(1);
                    Plant p2 = new Cactus();
                    gm.Board[5][col2] = p2;
                    world.addObject(p2, gm.getXCoord(col2, 5), gm.getYCoord(col2, 5));
                }
            }
            
            AudioManager.playSound(80, false, "achievement.mp3");
        }
    }

    private void clearUI() {
        World world = getWorld();
        if (world != null) {
            world.removeObjects(world.getObjects(AugmentCard.class));
            world.removeObjects(world.getObjects(Overlay.class));
        }
    }
}