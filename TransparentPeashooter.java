import greenfoot.*;
public class TransparentPeashooter extends TransparentObject
{
    public TransparentPeashooter(boolean bool) {
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
