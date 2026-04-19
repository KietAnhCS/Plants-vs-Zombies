import greenfoot.*;

public class Overlay extends Actor {
    public Overlay(int width, int height) {
        GreenfootImage img = new GreenfootImage(width, height);
        img.setColor(Color.BLACK);
        img.fill();
        img.setTransparency(180); 
        setImage(img);
    }

    public void act() {
        if (Greenfoot.mouseClicked(this)) {
            
        }
    }

    @Override
    protected void addedToWorld(World world) {
        setLocation(world.getWidth() / 2, world.getHeight() / 2);
    }
}