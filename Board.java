import greenfoot.*;
import java.util.*;

public class Board extends Actor {
    public Plant[][] Board = new Plant[6][9];
    public Lilypad[][] WaterBoard = new Lilypad[6][9];
    
    public int xOffset = 290;
    public int yOffset = 135; 
    public int xSpacing = 82;
    public int ySpacing = 85;
    
    public int currentRowCount = 6;
    private boolean isWaterMap = false; 

    public Board() {
        getImage().setTransparency(0);
    }

    public void setupLayout(boolean isWater) {
        this.isWaterMap = isWater; 
        
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 9; j++) {
                Board[i][j] = null;
                WaterBoard[i][j] = null;
            }
        }

        if (isWater) {
            currentRowCount = 6;
            yOffset = 135;
            ySpacing = 85; 
        } else {
            currentRowCount = 5;
            yOffset = 110;
            ySpacing = 100;
        }
    }

    public boolean isWaterRow(int y) {
        return isWaterMap && (y == 2 || y == 3);
    }

    public boolean canPlace(int x, int y, Plant plant) {
        if (y < 0 || y >= currentRowCount || x < 0 || x >= 9) return false;
        
        boolean isWater = isWaterRow(y);

        if (plant instanceof Lilypad) {
            return isWater && WaterBoard[y][x] == null && Board[y][x] == null;
        } else {
            if (isWater) {
                return WaterBoard[y][x] != null && Board[y][x] == null;
            } else {
                return Board[y][x] == null;
            }
        }
    }

    public boolean placePlant(int x, int y, Plant plant) {
        if (canPlace(x, y, plant)) {
            int posX = x * xSpacing + xOffset;
            int posY = y * ySpacing + yOffset;
            
            if (plant instanceof Lilypad) {
                WaterBoard[y][x] = (Lilypad)plant;
                getWorld().addObject(plant, posX, posY + 10);
            } else {
                Board[y][x] = plant;
                getWorld().addObject(plant, posX, posY);
            }
            return true;
        }
        return false; 
    }

    public void removePlant(int x, int y) {
        if (y >= 0 && y < 6 && x >= 0 && x < 9) {
            if (Board[y][x] != null) {
                getWorld().removeObject(Board[y][x]);
                Board[y][x] = null;
            } else if (WaterBoard[y][x] != null) {
                getWorld().removeObject(WaterBoard[y][x]);
                WaterBoard[y][x] = null;
            }
        }
    }
}