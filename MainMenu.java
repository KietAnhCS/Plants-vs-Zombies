import greenfoot.*;

public class MainMenu extends World {
    private Tombstone tombstone;
    public Hitbox hitbox = new Hitbox();

    public MainMenu() {
        super(1111, 705, 1, false);
        GreenfootImage bg = new GreenfootImage("pvz_background.png");
        bg.scale(1111, 705);
        setBackground(bg);
        setupWorld();
    }

    private void setupWorld() {
        Greenfoot.setSpeed(50);
        
        setPaintOrder(MuteButton.class, Start.class, More.class, Hitbox.class, 
                      Tombstone.class, ZombieHand.class, PvZLogo.class);

        addObject(hitbox, 0, 0);
        addObject(new MuteButton(), 1050, 50);

        int logoX = 350; 
        int logoY = 175; 
        addObject(new PvZLogo(), logoX, logoY);

        tombstone = new Tombstone();
        int tombX = 850;
        int tombY = 400;
        addObject(tombstone, tombX, tombY);

        int startButtonY = tombY - 70;
        int moreButtonY = tombY + 50;

        addObject(new Start(), tombX + 20, startButtonY);
        addObject(new More(), tombX + 5, moreButtonY);
    }

    @Override
    public void started() {
        AudioManager.playBGM("menutheme.mp3");
    }

    @Override
    public void stopped() {
        AudioManager.stopBGM();
    }

    public void act() {
        moveHitbox();
    }

    public void moveHitbox() {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse != null) hitbox.setLocation(mouse.getX(), mouse.getY());
    }
}