import greenfoot.*; 
public class TransparentObject extends Actor
{
    public TransparentObject() {
        
    }
    public void act()
    {
    
    }
    public void setTransparent(boolean bool) {
        if (bool) {
            getImage().setTransparency(125);
        } else {
            getImage().setTransparency(255);
        }
    }
}
