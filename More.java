import greenfoot.*;
import java.awt.Desktop;
import java.net.URL;

public class More extends Button {
    public More() {
        super("more1.png", "more2.png");
        
        double scaleFactor = 0.9;
        
        if (this.idle != null) {
            this.idle.scale((int)(this.idle.getWidth() * scaleFactor), (int)(this.idle.getHeight() * scaleFactor));
        }
        if (this.hover != null) {
            this.hover.scale((int)(this.hover.getWidth() * scaleFactor), (int)(this.hover.getHeight() * scaleFactor));
        }
        setImage(this.idle);
        setRotation(0);
    }

    @Override
    protected void onClick() {
        try {
            Desktop.getDesktop().browse(new URL("https://github.com/KietAnhCS").toURI());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}