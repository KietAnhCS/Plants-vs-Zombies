import greenfoot.*;  
import java.util.*;


public class clickShovel extends SmoothMover
{
    private MyWorld myWorld;
    private Plant lastPlant = null;
    
    public void addedToWorld(World world) {
        myWorld = (MyWorld)getWorld();
    }

    public void act()
    {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse != null) {
            setLocation(mouse.getX(), mouse.getY());
            
            // Kiểm tra an toàn để tránh lỗi chia cho 0
            if (Board.xSpacing == 0 || Board.ySpacing == 0) return;

            // 1. Tính toán vị trí ô (Grid) dựa trên thông số động của Board
            int x = (mouse.getX() - Board.xOffset) / Board.xSpacing;
            int y = (mouse.getY() - Board.yOffset) / Board.ySpacing;
            
            // Lấy số hàng hiện tại từ board để giới hạn vùng đào cây
            int currentRowCount = (myWorld.board != null) ? (myWorld.board.Board.length) : 5; 
            // Tuy nhiên, mảng Board của mình luôn là [6][9], 
            // ta chỉ cần check x, y nằm trong phạm vi map hiện tại.
            
            boolean isInsideGrid = (x >= 0 && x < 9 && y >= 0 && y < (myWorld.board.Board.length));

            if (isInsideGrid) {
                // 2. Ưu tiên lấy cây nằm trên, nếu không có mới lấy Lilypad
                Plant current = myWorld.board.Board[y][x]; 
                if (current == null) {
                    current = myWorld.board.WaterBoard[y][x];
                }
                
                // Logic làm mờ cây để xem trước (Highlight)
                handleHighlight(current);

                // 3. Xử lý Click đào cây
                if (Greenfoot.mouseClicked(null)) {
                    if (current != null) {
                        myWorld.board.removePlant(x, y); 
                    } else {
                        // Click vào ô trống thì phát tiếng kêu nhẹ
                        AudioPlayer.play(80, "tap.mp3");
                    }
                    exitShovel();
                }
            } else {
                // Nếu chuột nằm ngoài bàn cờ
                resetOpaque();
                if (Greenfoot.mouseClicked(null)) {
                    exitShovel();
                }
            }
        }
    }

    /**
     * Hiệu ứng làm mờ cây khi xẻng đi ngang qua
     */
    private void handleHighlight(Plant current) {
        if (current != null) {
            if (lastPlant != null && lastPlant != current) {
                lastPlant.opaque = false; // Reset cây cũ
            }
            lastPlant = current;
            lastPlant.opaque = true; // Làm mờ cây hiện tại
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
        myWorld.shovel.setSelected(false);
        myWorld.removeObject(this);
    }
}