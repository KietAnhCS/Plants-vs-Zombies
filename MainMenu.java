import greenfoot.*;  

public class MainMenu extends World
{
    Hitbox hitbox = new Hitbox();
    GreenfootSound menutheme = new GreenfootSound("menutheme.mp3");
    public MainMenu(GreenfootSound menutheme)
    {    
        
        super(1111, 705, 1, false); 
        addObject(hitbox,0,0);
        addObject(new Start(), 555, 198);
        addObject(new More(), 532, 316);
        
        
        this.menutheme = menutheme;
        Greenfoot.setSpeed(50);
    }
    public void act() {
        if (Greenfoot.isKeyDown("1")) {
            menutheme.stop();
            Greenfoot.setWorld(new CinematicIntro());
                    
                
        } 
    }
    public void started() {
        if (!menutheme.isPlaying()) {
            menutheme.setVolume(0);
            menutheme.playLoop();
        }
    
    }
    public void stopped() {
        menutheme.pause();
    }

    public void moveHitbox() {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse != null) {
            hitbox.setLocation(mouse.getX(), mouse.getY());
        }
    }
    
}
