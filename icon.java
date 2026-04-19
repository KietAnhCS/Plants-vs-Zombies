import greenfoot.*;  


public class icon extends World
{
    public icon()
    {    
        super(1111, 602, 1); 
        Greenfoot.setWorld(new CinematicIntro());
    }
    public void started() {
        Greenfoot.setWorld(new CinematicIntro());
    }
    public void act() {
        Greenfoot.setWorld(new CinematicIntro());
    }
}
