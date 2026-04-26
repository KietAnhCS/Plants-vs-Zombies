import greenfoot.*; 

public class TransparentCactus extends TransparentObject
{
    public TransparentCactus(boolean bool) {
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
