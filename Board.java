import greenfoot.*;
import java.util.*;

public class Board extends Actor {
    // Chỉ giữ lại mảng Board chính cho thực vật
    public Plant[][] Board = new Plant[6][9];
    
    public int xOffset = 334;
    public int yOffset = 175;  
    public int xSpacing = 78;  
    public int ySpacing = 65;  
    
    public final int colDeltaY = 0;  
    public final int rowDeltaX = -12; 
    
    public int currentRowCount = 6;

    public Board() {
        getImage().setTransparency(0);
    }
    
    /**
     * Khởi tạo lại bố cục cho bản đồ khô 6x9
     */
    public void setupLayout() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 9; j++) {
                Board[i][j] = null;
            }
        }
        currentRowCount = 6;
    }

    public int getXCoord(int x, int y) {
        return xOffset + (x * xSpacing) + (y * rowDeltaX);
    }

    public int getYCoord(int x, int y) {
        return yOffset + (x * colDeltaY) + (y * ySpacing);
    }
    
    /**
     * Kiểm tra xem có thể đặt cây tại vị trí (x, y) hay không
     */
    public boolean canPlace(int x, int y, Plant plant) {
        // Kiểm tra giới hạn mảng
        if (y < 0 || y >= 6 || x < 0 || x >= 9) return false;
        
        // Kiểm tra khả năng nâng cấp cây (ví dụ: Twin Sunflower đè lên Sunflower)
        if (UpgradeManager.canUpgrade(plant, Board[y][x])) return true;
        
        // Chỉ cho phép đặt nếu ô trống
        return Board[y][x] == null;
    }
    
    /**
     * Đặt cây vào vị trí chỉ định
     */
    public boolean placePlant(int x, int y, Plant plant) {
        if (!canPlace(x, y, plant)) return false;
        
        int posX = getXCoord(x, y);
        int posY = getYCoord(x, y);

        // Xử lý nâng cấp cây
        if (UpgradeManager.canUpgrade(plant, Board[y][x])) {
            Plant upgradedPlant = UpgradeManager.getUpgradeResult(plant, Board[y][x]);
            getWorld().removeObject(Board[y][x]);
            Board[y][x] = upgradedPlant;
            getWorld().addObject(upgradedPlant, posX, posY);
            return true;
        }
        
        Board[y][x] = plant;
        getWorld().addObject(plant, posX, posY);
        return true;
    }
    
    public void removePlant(int x, int y) {
        if (y < 0 || y >= 6 || x < 0 || x >= 9) return;
        if (Board[y][x] != null) {
            getWorld().removeObject(Board[y][x]);
            Board[y][x] = null;
        }
    }

    public boolean movePlant(int oldX, int oldY, int newX, int newY, Plant plant) {
        if (newX < 0 || newX >= 9 || newY < 0 || newY >= 6) return false;
        if (oldX == newX && oldY == newY) return true;
    
        if (UpgradeManager.canUpgrade(plant, Board[newY][newX])) {
            Plant upgradedPlant = UpgradeManager.getUpgradeResult(plant, Board[newY][newX]);
            
            getWorld().removeObject(Board[newY][newX]);
            Board[oldY][oldX] = null; 
            
            Board[newY][newX] = upgradedPlant;
            int posX = getXCoord(newX, newY);
            int posY = getYCoord(newX, newY);
            getWorld().addObject(upgradedPlant, posX, posY);
            
            getWorld().removeObject(plant); 
            return true; 
        }
    
        if (canPlace(newX, newY, plant)) {
            Board[oldY][oldX] = null;
            Board[newY][newX] = plant;
            return true;
        }
    
        return false;
    }
}