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
        addObject(hitbox, 0, 0);
        addObject(new Start(), 555, 198);
        addObject(new More(), 532, 316);
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