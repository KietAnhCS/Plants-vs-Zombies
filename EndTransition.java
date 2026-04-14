import greenfoot.*; 

public class EndTransition extends Actor
{
    
    public int alpha = 255;
    public EndTransition() {
        getImage().setTransparency(255);
    }
    public void act()
    {
        alpha-=10;
        if (alpha >= 0) { 
            getImage().setTransparency(alpha);
        } else {
            getImage().setTransparency(0);
            getWorld().removeObject(this);
        }
        
    }
}
