import greenfoot.*;  
public class TransparentBonkchoy extends TransparentObject
{
    public TransparentBonkchoy(boolean bool) {
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
