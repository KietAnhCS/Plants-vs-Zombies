import greenfoot.*;
public class TransparentTwinSunflower extends TransparentObject
{  
    public TransparentTwinSunflower(boolean bool) {
        if (bool) {
            getImage().setTransparency(125);
        } else {
            getImage().setTransparency(255);
        }
    }
    public void act()
    {
    }
}
