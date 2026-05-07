import greenfoot.*;
import java.awt.Desktop;
import java.net.URL;

public class More extends Button {
    public More() {
        super("more1.png", "more2.png");
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