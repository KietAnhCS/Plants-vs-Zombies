import greenfoot.*;  

public class Button extends SpriteAnimator
{
    public GreenfootImage idle;
    public GreenfootImage hover;
    public Button(String idle, String hover) {
        this.idle = new GreenfootImage(idle);
        this.hover = new GreenfootImage(hover);
        setImage(idle);
    }
    public void act()
    {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        ResultScreen world = (ResultScreen)getWorld();
        if (mouse != null) {
            world.moveHitbox();
            if (this.intersects(world.hitbox)) {
                setImage(hover);
            } else {
                setImage(idle);
            }
            if (Greenfoot.mouseClicked(this)) {
                AudioPlayer.play(100, "gravebutton.mp3");
                update(); 
            }
        }
        
      
    }
    public void update() {
        
    }
}
