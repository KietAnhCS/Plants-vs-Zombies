import greenfoot.*;

public class MainMenu extends World {
    public Hitbox hitbox = new Hitbox();
    private String menuMusicFile = "menutheme.mp3";

    public MainMenu() {    
        super(1111, 705, 1, false); 
        setupWorld();
    }

    private void setupWorld() {
        Greenfoot.setSpeed(50);
        
        setPaintOrder(MuteButton.class, Start.class, More.class, Hitbox.class, ZombieHand.class, MoonGround.class);        
        addObject(new MoonGround(), 500, 780);
        
        addObject(hitbox, 0, 0);
        addObject(new Start(), 800, 198); 
        addObject(new More(), 780, 300);  
        
        addObject(new MuteButton(), 1050, 50);
    }

    public void act() {
        moveHitbox();
    }

    @Override
    public void started() {
        AudioManager.playBGM(menuMusicFile);
    }
    
    @Override
    public void stopped() { 
        AudioManager.stopBGM();
    }

    public void moveHitbox() {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse != null) {
            hitbox.setLocation(mouse.getX(), mouse.getY());
        }
    }
}
