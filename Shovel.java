import greenfoot.*;

public class Shovel extends PhysicsBody {
    private boolean selected = false;

    public void addedToWorld(World world) {
        setImage("shovel1.png");
        selected = false;
    }

    public void act() {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse != null && Greenfoot.mousePressed(this)) {
            PlayScene world = (PlayScene) getWorld();
            if (!selected) {
                selected = true;
                setImage("shovel2.png");
                AudioPlayer.play(80, "shovel.mp3");
                world.addObject(new SellShovel(this, getX(), getY()), mouse.getX(), mouse.getY());
            }
        }
    }

    public void setSelected(boolean bool) {
        selected = bool;
        setImage(bool ? "shovel2.png" : "shovel1.png");
    }
}