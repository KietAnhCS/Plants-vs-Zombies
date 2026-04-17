import greenfoot.*; 

public class TransparentLilypad extends TransparentObject
{
    public TransparentLilypad(boolean bool) {
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
