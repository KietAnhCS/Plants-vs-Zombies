import greenfoot.*;
import java.util.*;

public class Board extends Actor {
    public Plant[][] Board = new Plant[6][9];
    public Lilypad[][] WaterBoard = new Lilypad[6][9];
    
    // Giữ nguyên tên biến, điều chỉnh giá trị để khớp với hàng Zombie
    public int xOffset = 334;
    public int yOffset = 175;  // Khớp với hàng 1 của Zombie
    public int xSpacing = 78;  // Khoảng cách cột
    public int ySpacing = 65;  // Khoảng cách trung bình giữa các hàng Zombie
    
    // Điều chỉnh Delta để tạo độ nghiêng khớp với tọa độ Zombie cung cấp
    public final int colDeltaY = 0;   // Để cây nằm thẳng hàng ngang cho đều
    public final int rowDeltaX = -12; // Độ thụt lề mỗi hàng để khớp với (250, 238, 226...)
    
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
        currentRowCount = 6;
    }

    // Hàm lấy tọa độ X chuẩn (nghiêng)
    public int getXCoord(int x, int y) {
        return xOffset + (x * xSpacing) + (y * rowDeltaX);
    }

    // Hàm lấy tọa độ Y chuẩn (nghiêng)
    public int getYCoord(int x, int y) {
        return yOffset + (x * colDeltaY) + (y * ySpacing);
    }
    
    public boolean isWaterRow(int y) {
        return isWaterMap && (y == 2 || y == 3);
    }
    
    public boolean canPlace(int x, int y, Plant plant) {
        if (y < 0 || y >= currentRowCount || x < 0 || x >= 9) return false;
        if (UpgradeManager.canUpgrade(plant, Board[y][x])) return true;
        
        boolean isWater = isWaterRow(y);
        if (plant instanceof Lilypad) {
            return isWater && WaterBoard[y][x] == null && Board[y][x] == null;
        }
        if (isWater) return WaterBoard[y][x] != null && Board[y][x] == null;
        
        return Board[y][x] == null;
    }
    
    public boolean placePlant(int x, int y, Plant plant) {
        if (!canPlace(x, y, plant)) return false;
        
        int posX = getXCoord(x, y);
        int posY = getYCoord(x, y);

        if (UpgradeManager.canUpgrade(plant, Board[y][x])) {
            Plant upgradedPlant = UpgradeManager.getUpgradeResult(plant, Board[y][x]);
            getWorld().removeObject(Board[y][x]);
            Board[y][x] = upgradedPlant;
            getWorld().addObject(upgradedPlant, posX, posY);
            return true;
        }
        
        if (plant instanceof Lilypad) {
            WaterBoard[y][x] = (Lilypad) plant;
            getWorld().addObject(plant, posX, posY + 10);
        } else {
            Board[y][x] = plant;
            getWorld().addObject(plant, posX, posY);
        }
        return true;
    }
    
    public void removePlant(int x, int y) {
        if (y < 0 || y >= 6 || x < 0 || x >= 9) return;
        if (Board[y][x] != null) {
            getWorld().removeObject(Board[y][x]);
            Board[y][x] = null;
        } else if (WaterBoard[y][x] != null) {
            getWorld().removeObject(WaterBoard[y][x]);
            WaterBoard[y][x] = null;
        }
    }

    public boolean movePlant(int oldX, int oldY, int newX, int newY, Plant plant) {
        // 1. Kiểm tra giới hạn mảng
        if (newX < 0 || newX >= 9 || newY < 0 || newY >= currentRowCount) return false;
        
        // 2. Nếu thả ngay tại vị trí cũ thì coi như thành công
        if (oldX == newX && oldY == newY) return true;
    
        // 3. Kiểm tra logic nâng cấp (Nếu kéo hướng dương đè lên hướng dương)
        if (UpgradeManager.canUpgrade(plant, Board[newY][newX])) {
            // Thực hiện nâng cấp
            Plant upgradedPlant = UpgradeManager.getUpgradeResult(plant, Board[newY][newX]);
            
            // Xóa cây cũ ở ô đích và cây đang kéo ở ô cũ
            getWorld().removeObject(Board[newY][newX]);
            Board[oldY][oldX] = null; 
            
            // Cập nhật cây mới vào mảng và thế giới
            Board[newY][newX] = upgradedPlant;
            int posX = getXCoord(newX, newY);
            int posY = getYCoord(newX, newY);
            getWorld().addObject(upgradedPlant, posX, posY);
            
            // Xóa chính cái cây đang được kéo (vì nó đã "nhập" vào cây kia)
            getWorld().removeObject(plant); 
            return true; 
        }
    
        // 4. Nếu không nâng cấp, kiểm tra xem ô mới có trống không để di chuyển bình thường
        if (canPlace(newX, newY, plant)) {
            Board[oldY][oldX] = null;
            Board[newY][newX] = plant;
            return true;
        }
    
        return false;
    }
}