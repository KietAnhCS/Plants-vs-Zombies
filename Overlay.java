import greenfoot.*;

public class Overlay extends Actor {
    public Overlay(int width, int height) {
        GreenfootImage img = new GreenfootImage(width, height);
        img.setColor(Color.BLACK);
        img.fill();
        img.setTransparency(150);
        setImage(img);
    }
}