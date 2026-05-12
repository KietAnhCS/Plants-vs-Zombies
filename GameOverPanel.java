import greenfoot.*;

public class GameOverPanel extends Actor {
    public GameOverPanel() {
        // Tải ảnh trong suốt đã xóa nền (gameoverpanel.png Hoàng làm ở bước trước)
        GreenfootImage img = new GreenfootImage("gameoverpanel.png");

        // --- CHỈNH SỬA KÍCH THƯỚC TẠI ĐÂY ---
        // Dùng tỷ lệ (%) so với ảnh gốc để nhỏ lại mà không bị méo ảnh
        // 1.0 = 100%, 0.5 = 50%, 0.7 = 70%
        // Nhìn hình thì mình nghĩ mức 0.5 (một nửa) hoặc 0.6 là vừa đẹp
        double scaleFactor = 0.27; 

        // Tính toán kích thước mới dựa trên tỷ lệ
        int newWidth = (int)(img.getWidth() * scaleFactor);
        int newHeight = (int)(img.getHeight() * scaleFactor);

        // Ép kích thước mới cho ảnh (vẫn giữ nguyên tỷ lệ chuẩn)
        img.scale(newWidth, newHeight);

        setImage(img);
    }
}