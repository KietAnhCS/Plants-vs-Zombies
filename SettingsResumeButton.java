import greenfoot.*;

public class SettingsResumeButton extends Button {
    
    public SettingsResumeButton() {
        // Gọi constructor của lớp cha Button
        super("resume1.png", "resume2.png"); 
        
        double scaleFactor = 0.18; 
        
        // Sử dụng biến idleImage và hoverImage từ lớp cha
        if (this.idleImage != null) {
            this.idleImage.scale((int)(this.idleImage.getWidth() * scaleFactor), (int)(this.idleImage.getHeight() * scaleFactor));
        }
        if (this.hoverImage != null) {
            this.hoverImage.scale((int)(this.hoverImage.getWidth() * scaleFactor), (int)(this.hoverImage.getHeight() * scaleFactor));
        }
        
        setImage(this.idleImage);
    }

    @Override
    public void addedToWorld(World world) {
        // Thêm text vào ảnh ngay khi object được add vào world
        addTextToImage(this.idleImage);
        addTextToImage(this.hoverImage);
        
        setImage(this.idleImage);
    }
    
    private void addTextToImage(GreenfootImage img) {
        if (img != null) {
            img.setColor(Color.WHITE);
            img.setFont(new Font("Arial", true, false, 24)); 
            
            int textX = (img.getWidth() / 2) - 60; 
            int textY = (img.getHeight() / 2) + 10;
            
            // Bạn có thể thêm lệnh drawString ở đây nếu muốn hiển thị chữ
            // img.drawString("RESUME", textX, textY);
        }
    }

    @Override
    public void update() {
        // Gọi logic hover và click từ lớp cha
        super.update();
    }
    
    @Override
    protected void onClick() {
        World currentWorld = getWorld();
        
        // Kiểm tra instance của World để gọi phương thức đóng menu tương ứng
        if (currentWorld instanceof Arena) {
            ((Arena)currentWorld).closeSettingsMenu();
        } else if (currentWorld instanceof PlayScene) {
            ((PlayScene)currentWorld).closeSettingsMenu();
        }
    }
}