import greenfoot.*;  
import java.awt.Desktop;
import java.net.URL;

public class More extends Button
{
    public boolean clicked = false;
    GreenfootImage[] start;
    public int counter = 0;
    public More() {
        super("more1.png", "more2.png");
        start = importSprites("more", 2);
    }
    public void act()
    {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        MainMenu world = (MainMenu)getWorld();
        
        if (mouse != null) {
            world.moveHitbox();
            if (this.intersects(world.hitbox)) {
                setImage(hover);
            } else {
                setImage(idle);
            }
            if (Greenfoot.mouseClicked(this)) {
                clicked = true;
                try{
                    Desktop.getDesktop().browse(new URL("https://github.com/KietAnhCS").toURI());
                }
            catch (Exception e)
                {}
                AudioPlayer.play(100, "gravebutton.mp3");
                
            }
        }
        
        
      
    }
    public void update() {
        
    }
}
