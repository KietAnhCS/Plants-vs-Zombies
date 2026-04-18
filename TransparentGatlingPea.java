import greenfoot.*;  
public class TransparentGatlingPea extends TransparentObject
{
    public TransparentGatlingPea(boolean bool) {
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
