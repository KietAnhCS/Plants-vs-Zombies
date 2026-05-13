import greenfoot.*;

public class ResultScreen extends World {
    public Hitbox hitbox = new Hitbox();

    public ResultScreen(World restartWorld) {
        super(1111, 602, 1, false);
        Greenfoot.setSpeed(50);
        addObject(hitbox, 0, 0);
        addObject(new Retry(restartWorld), 365, 395);
    }

    public void act() {
        moveHitbox();
    }

    private void moveHitbox() {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse != null) hitbox.setLocation(mouse.getX(), mouse.getY());
    }
}