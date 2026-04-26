import greenfoot.*;

public class TransparentBonkchoy extends TransparentObject
{
    public TransparentBonkchoy(boolean bool) {
        GreenfootImage img = new GreenfootImage("bonkchoyidle_three1.png");
        img.scale(img.getWidth() / 2, img.getHeight() / 2);
        setImage(img);
        
        if (bool) {
            getImage().setTransparency(125);
        } else {
            getImage().setTransparency(255);
        }
    }

    public void act() {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse != null) {
            setLocation(mouse.getX(), mouse.getY() - 15);
        }
    }
}