import greenfoot.*;  
public class Shovel extends PhysicsBody
{
    public boolean selected = false;
    
    public void addedToWorld(World world) {
        setImage("shovel1.png");
        selected = false;
    }
    public void act()
    {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse != null) {
            if (Greenfoot.mouseClicked(null)) {
                PlayScene PlayScene = (PlayScene)getWorld();
                PlayScene.moveHitbox();
                if (intersects(PlayScene.hitbox)) {
                    if (!selected) {
                        selected = true;
                        setImage("shovel2.png");
                        AudioPlayer.play(80, "shovel.mp3");
                        PlayScene.addObject(new clickShovel(), mouse.getX(), mouse.getY());
                    }
                }
            }
        }
        
    }
    public void setSelected(boolean bool) {
        if (!bool) {
            selected = bool;
            setImage("shovel1.png");
        } 
    }
    
}
