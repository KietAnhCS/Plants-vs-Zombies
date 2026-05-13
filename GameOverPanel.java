import greenfoot.*;

public class GameOverPanel extends Actor {
    public GameOverPanel() {
        GreenfootImage img = new GreenfootImage("gameoverpanel.png");
        double scaleFactor = 0.27; 

        int newWidth = (int)(img.getWidth() * scaleFactor);
        int newHeight = (int)(img.getHeight() * scaleFactor);

        img.scale(newWidth, newHeight);

        setImage(img);
    }
}