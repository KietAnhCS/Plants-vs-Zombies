import greenfoot.*;  

public class Start extends Button
{
    private boolean clicked = false;
    private GreenfootImage[] start;
    private int counter = 0;

    public Start() {
        super("start1.png", "start2.png");
        
        start = importSprites("start", 2);
        scaleImages(start, 1.25);
        
        if (idle != null) idle.scale((int)(idle.getWidth() * 1.25), (int)(idle.getHeight() * 1.25));
        if (hover != null) hover.scale((int)(hover.getWidth() * 1.25), (int)(hover.getHeight() * 1.25));
        
        setImage(idle);
    }

    private void scaleImages(GreenfootImage[] images, double factor) {
        for (GreenfootImage img : images) {
            img.scale((int)(img.getWidth() * factor), (int)(img.getHeight() * factor));
        }
    }

    public void act()
    {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        MainMenu world = (MainMenu)getWorld();
        
        if (clicked) {
            animate(start, 80, true);
            counter++;
            
            if (counter >= 200) {
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
                    processClick();
                }
            }
        }
    }

    private void processClick() {
        clicked = true;
        
        AudioManager.stopBGM(); 
        
        AudioManager.playSound(80, false, "gravebutton.mp3");
        AudioManager.playSound(80, false, "losemusic.mp3");
        
        getWorld().addObject(new DelayAudio("evillaugh.mp3", 80, false, 1000L), 0, 0);
        
        getWorld().addObject(new ZombieHand(), 300, 500);
    }

    public void update() {
        Greenfoot.setWorld(new Arena());
    }
}