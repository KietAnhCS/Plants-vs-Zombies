import greenfoot.*;  
import java.awt.Desktop;
import java.net.URL;

public class More extends Button
{
    public More() {
        super("more1.png", "more2.png");
    }

    public void act()
    {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        MainMenu world = (MainMenu)getWorld();
        
        if (world == null) return;

        if (mouse != null) {
            world.moveHitbox();
            if (this.intersects(world.hitbox)) {
                setImage(hover);
            } else {
                setImage(idle);
            }
        }
        if (Greenfoot.mouseClicked(this)) {
            executeClickAction();
        }
    }

    private void executeClickAction() {
        AudioManager.playSound(80, false,"gravebutton.mp3");

        try {
            Desktop.getDesktop().browse(new URL("https://github.com/KietAnhCS").toURI());
        } catch (Exception e) {
            System.out.println("Không thể mở liên kết: " + e.getMessage());
        }
    }
}