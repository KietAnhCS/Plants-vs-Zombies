import greenfoot.*;
import java.util.*;

public class clickPlantFood extends SmoothMover
{
    private PlantFood master;
    private PlayScene playScene;
    private Plant lastPlant = null;

    // Constructor nhận vào đối tượng PlantFood gốc để quản lý trạng thái selected
    public clickPlantFood(PlantFood master) {
        this.master = master;
        setImage("plantfood.png"); // Ảnh cái lá sẽ bay theo con trỏ chuột
    }

    public void addedToWorld(World world) {
        playScene = (PlayScene)getWorld();
    }

    public void act() {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse != null) {
            // Cho cái lá luôn đi theo vị trí chuột
            setLocation(mouse.getX(), mouse.getY());

            // Kiểm tra thông số board để tránh lỗi Null
            if (playScene.board == null || playScene.board.xSpacing == 0) return;

            // --- LOGIC XÁC ĐỊNH Ô LƯỚI (Y hệt Shovel) ---
            double calcX = (double)(mouse.getX() - playScene.board.xOffset) / playScene.board.xSpacing;
            double calcY = (double)(mouse.getY() - playScene.board.yOffset) / playScene.board.ySpacing;
            
            int x = (int)Math.round(calcX);
            int y = (int)Math.round(calcY);
            
            // Kiểm tra xem chuột có đang nằm trong phạm vi sân cỏ không
            boolean isInsideGrid = (x >= 0 && x < 9 && y >= 0 && y < playScene.board.currentRowCount);

            if (isInsideGrid) {
                // Tìm cây tại vị trí (x, y) trên Board chính hoặc Board nước
                Plant current = playScene.board.Board[y][x]; 
                if (current == null) {
                    current = playScene.board.WaterBoard[y][x];
                }
                
                // Xử lý hiệu ứng sáng lên khi di chuột qua cây
                handleHighlight(current);

                // Nếu người dùng click chuột
                if (Greenfoot.mouseClicked(null)) {
                    if (current != null) {
                        // KÍCH HOẠT KỸ NĂNG ĐẶC BIỆT CỦA CÂY
                        current.activatePlantFood(); 
                        // AudioPlayer.play(80, "plantfood_use.mp3"); // Âm thanh nếu có
                    } else {
                        // Click vào ô trống
                        AudioPlayer.play(80, "tap.mp3");
                    }
                    exitPlantFood(); // Thoát trạng thái dùng PlantFood
                }
            } else {
                // Nếu chuột ra ngoài lưới, tắt highlight và nếu click thì hủy chọn
                resetOpaque();
                if (Greenfoot.mouseClicked(null)) {
                    exitPlantFood();
                }
            }
        }
    }

    // Hàm xử lý highlight (làm mờ/sáng cây)
    private void handleHighlight(Plant current) {
        if (current != null) {
            if (lastPlant != null && lastPlant != current) {
                lastPlant.opaque = false; 
            }
            lastPlant = current;
            lastPlant.opaque = true; // Biến này điều khiển độ sáng trong class Plant
        } else {
            resetOpaque();
        }
    }

    // Reset lại trạng thái cây cũ khi chuột di chuyển đi chỗ khác
    private void resetOpaque() {
        if (lastPlant != null) {
            lastPlant.opaque = false;
            lastPlant = null;
        }
    }

    // Hàm kết thúc: trả trạng thái selected về false và xóa object theo chuột
    private void exitPlantFood() {
        resetOpaque();
        master.setSelected(false);
        playScene.removeObject(this);
    }
}