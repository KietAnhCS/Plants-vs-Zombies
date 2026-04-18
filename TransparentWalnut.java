import greenfoot.*; 
public class TransparentWalnut extends TransparentObject
{
    public TransparentWalnut(boolean bool) {
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
