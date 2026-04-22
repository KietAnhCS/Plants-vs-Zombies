import greenfoot.*;  
import java.util.*;

public class clickPlantFood extends PhysicsBody
{
    private PlantFood master;
    private PlayScene playScene;
    private Plant lastPlant = null;

    public clickPlantFood(PlantFood master) {
        this.master = master;
        setImage("plantfood.png");
    }

    public void addedToWorld(World world) {
        playScene = (PlayScene)getWorld();
    }

    public void act() {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse != null) {
            setLocation(mouse.getX(), mouse.getY());

            if (playScene.board == null || playScene.board.xSpacing == 0) return;

            double calcX = (double)(mouse.getX() - playScene.board.xOffset) / playScene.board.xSpacing;
            double calcY = (double)(mouse.getY() - playScene.board.yOffset) / playScene.board.ySpacing;
            
            int x = (int)Math.round(calcX);
            int y = (int)Math.round(calcY);
            
            boolean isInsideGrid = (x >= 0 && x < 9 && y >= 0 && y < 6);

            if (isInsideGrid) {
                
                Plant current = playScene.board.Board[y][x]; 
                
                handleHighlight(current);

                if (Greenfoot.mouseClicked(null)) {
                    if (current != null) {
                        current.activatePlantFood(); 
                        master.usePlantFood();
                        exitPlantFood();
                    } else {
                        AudioPlayer.play(80, "tap.mp3");
                        exitPlantFood(); 
                    }
                }
            } else {
                resetOpaque();
                if (Greenfoot.mouseClicked(null)) {
                    exitPlantFood();
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

    private void exitPlantFood() {
        resetOpaque();
        master.setSelected(false);
        
        if (getWorld() != null) {
            getWorld().removeObject(this);
        }
    }
}