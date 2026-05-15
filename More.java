import greenfoot.*;
import java.awt.Desktop;
import java.net.URL;

public class More extends Button {

    public More() {
        // Gọi constructor lớp cha với 2 đường dẫn ảnh
        super("more1.png", "more2.png");
        
        double scaleFactor = 0.9;
        
        // Scale ảnh idle và hover kế thừa từ lớp Button
        if (this.idleImage != null) {
            this.idleImage.scale((int)(this.idleImage.getWidth() * scaleFactor), 
                                 (int)(this.idleImage.getHeight() * scaleFactor));
        }
        if (this.hoverImage != null) {
            this.hoverImage.scale((int)(this.hoverImage.getWidth() * scaleFactor), 
                                  (int)(this.hoverImage.getHeight() * scaleFactor));
        }
        
        // Thiết lập trạng thái ban đầu
        setImage(this.idleImage);
        setRotation(0);
    }

    @Override
    public void update() {
        // Gọi update của lớp cha để xử lý logic hover và click
        super.update();
    }

    @Override
    protected void onClick() {
        // Logic mở liên kết web khi click
        try {
            Desktop.getDesktop().browse(new URL("https://github.com/KietAnhCS").toURI());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}