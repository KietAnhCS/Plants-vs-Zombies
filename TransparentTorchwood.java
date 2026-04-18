import greenfoot.*; 
public class TransparentTorchwood extends TransparentObject
{
    public TransparentTorchwood(boolean bool) {
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
