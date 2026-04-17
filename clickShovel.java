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
            
            if (PlayScene.board == null || PlayScene.board.xSpacing == 0) return;

            double calcX = (double)(mouse.getX() - PlayScene.board.xOffset) / PlayScene.board.xSpacing;
            double calcY = (double)(mouse.getY() - PlayScene.board.yOffset) / PlayScene.board.ySpacing;
            
            int x = (int)Math.round(calcX);
            int y = (int)Math.round(calcY);
            
            boolean isInsideGrid = (x >= 0 && x < 9 && y >= 0 && y < PlayScene.board.currentRowCount);

            if (isInsideGrid) {
                Plant current = PlayScene.board.Board[y][x]; 
                if (current == null) {
                    current = PlayScene.board.WaterBoard[y][x];
                }
                
                handleHighlight(current);

                if (Greenfoot.mouseClicked(null)) {
                    if (current != null) {
                        // Xóa cây khỏi mảng logic và thế giới
                        PlayScene.board.removePlant(x, y); 
                        
                        // ĐỀN BÙ 30 MẶT TRỜI
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
        PlayScene.shovel.setSelected(false);
        PlayScene.removeObject(this);
    }
}