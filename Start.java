import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Class Start đã được scale 1.5x và tối ưu logic.
 */
public class Start extends Button
{
    public boolean clicked = false;
    GreenfootImage[] start;
    public int counter = 0;

    public Start() {
        super("start1.png", "start2.png");
        
        start = importSprites("start", 2);
        
        for (int i = 0; i < start.length; i++) {
            start[i].scale((int)(start[i].getWidth() * 1.25), (int)(start[i].getHeight() * 1.25));
        }
        
        
        if (idle != null) {
            idle.scale((int)(idle.getWidth() * 1.25), (int)(idle.getHeight() * 1.25));
        }
        if (hover != null) {
            hover.scale((int)(hover.getWidth() * 1.25), (int)(hover.getHeight() * 1.25));
        }
        
        setImage(idle);
    }

    public void act()
    {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        MainMenu world = (MainMenu)getWorld();
        
        if (clicked) {
            
            animate(start, 80, true);
            counter++;
            if (counter == 200) {
                update();
            }
        } else {
            if (mouse != null) {
                
                world.moveHitbox();
                
                
                if (this.intersects(world.hitbox)) {
                    setImage(hover);
                } else {
                    setImage(idle);
                }
                
                
                if (Greenfoot.mouseClicked(this)) {
                    clicked = true;
                    world.menutheme.stop();
                    
                    
                    AudioPlayer.play(0, "gravebutton.mp3");
                    AudioPlayer.play(0, "losemusic.mp3");
                    getWorld().addObject(new DelayAudio(new GreenfootSound("evillaugh.mp3"), 0, false, 1000L), 0, 0);
                    
                    
                    getWorld().addObject(new ZombieHand(), 300, 500);
                }
            }
        }
    }

    public void update() {
        
        getWorld().addObject(new Transition(false, new CinematicIntro(), 4), 365, 215);
    }
}