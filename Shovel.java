import greenfoot.*;

public class Shovel extends PhysicsBody {
    private static final String IMG_NORMAL   = "shovel1.png";
    private static final String IMG_SELECTED = "shovel2.png";

    private boolean selected = false;

    @Override
    public void addedToWorld(World world) {
        setImage(IMG_NORMAL);
        selected = false;
    }

    @Override
    public void act() {
        if (!Greenfoot.mousePressed(this)) return;
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse == null || selected) return;

        PlayScene scene = (PlayScene) getWorld();
        selected = true;
        setImage(IMG_SELECTED);
        AudioManager.playSound(80, false, "shovel.mp3");
        scene.addObject(new SellShovel(this, scene, getX(), getY()), mouse.getX(), mouse.getY());
    }

    public void setSelected(boolean value) {
        selected = value;
        setImage(value ? IMG_SELECTED : IMG_NORMAL);
    }
}