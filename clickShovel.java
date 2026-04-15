import greenfoot.*;  
import java.util.*;


public class clickShovel extends SmoothMover
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
            
            if (Board.xSpacing == 0 || Board.ySpacing == 0) return;

            int x = (mouse.getX() - Board.xOffset) / Board.xSpacing;
            int y = (mouse.getY() - Board.yOffset) / Board.ySpacing;
            
            int currentRowCount = (PlayScene.board != null) ? (PlayScene.board.Board.length) : 5; 
            
            boolean isInsideGrid = (x >= 0 && x < 9 && y >= 0 && y < (PlayScene.board.Board.length));

            if (isInsideGrid) {
                
                Plant current = PlayScene.board.Board[y][x]; 
                if (current == null) {
                    current = PlayScene.board.WaterBoard[y][x];
                }
                
                
                handleHighlight(current);

                if (Greenfoot.mouseClicked(null)) {
                    if (current != null) {
                        PlayScene.board.removePlant(x, y); 
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
        PlayScene.shovel.setSelected(false);
        PlayScene.removeObject(this);
    }
}