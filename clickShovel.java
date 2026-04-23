import greenfoot.*;  
import java.util.*;

public class clickShovel extends PhysicsBody
{
    private PlayScene PlayScene;
    private Plant lastPlant = null;
    
    public void addedToWorld(World world) {
        PlayScene = (PlayScene)getWorld();
    }

    public void act()
    {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse != null) {
            setLocation(mouse.getX(), mouse.getY());
            
            if (PlayScene.board == null) return;

            int gx = -1, gy = -1;
            double minDist = 60;

            for(int r = 0; r < 6; r++) {
                for(int c = 0; c < 9; c++) {
                    double d = Math.hypot(mouse.getX() - PlayScene.board.getXCoord(c, r), mouse.getY() - PlayScene.board.getYCoord(c, r));
                    if (d < minDist) {
                        minDist = d; gx = c; gy = r;
                    }
                }
            }

            if (gx != -1) {
                Plant current = PlayScene.board.Board[gy][gx]; 
                handleHighlight(current);

                if (Greenfoot.mouseClicked(null)) {
                    if (current != null) {
                        PlayScene.board.removePlant(gx, gy); 
                        if (PlayScene.seedbank != null) {
                            PlayScene.seedbank.addSun(30);
                        }
                        AudioPlayer.play(80, "plant.mp3"); 
                    } else {
                        AudioPlayer.play(80, "tap.mp3");
                    }
                    exitShovel();
                }
            } else {
                resetOpaque();
                if (Greenfoot.mouseClicked(null)) {
                    exitShovel();
                }
            }
        }
    }

    private void handleHighlight(Plant current) {
        if (current != null) {
            if (lastPlant != null && lastPlant != current) {
                lastPlant.opaque = false; 
            }
            lastPlant = current;
            lastPlant.opaque = true; 
        } else {
            resetOpaque();
        }
    }

    private void resetOpaque() {
        if (lastPlant != null) {
            lastPlant.opaque = false;
            lastPlant = null;
        }
    }

    private void exitShovel() {
        resetOpaque();
        if (PlayScene.shovel != null) {
            PlayScene.shovel.setSelected(false);
        }
        getWorld().removeObject(this); 
    }
}