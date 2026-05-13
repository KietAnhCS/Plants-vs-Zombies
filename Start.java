import greenfoot.*;

public class Start extends Button {
    private boolean clicked = false;
    private int counter = 0;
    private GreenfootImage[] startAnim;

    public Start() {
        super("start1.png", "start2.png");
        startAnim = importSprites("start", 2);
        
        double scaleFactor = 0.9;
        
        scaleAll(startAnim, scaleFactor);
        scaleImage(idle, scaleFactor);
        scaleImage(hover, scaleFactor);
        setImage(idle);
        setRotation(2);
    }

    public void act() {
        if (clicked) {
            animate(startAnim, 80, true);
            counter++;
            if (counter >= 200) onClick();
        } else {
            super.act(); 
        }
    }

    @Override
    protected void onClick() {
        if (!clicked) {
            clicked = true;
            AudioManager.stopBGM(); 
            AudioManager.getInstance().playSound(80, false, "gravebutton.mp3");
            AudioManager.getInstance().playSound(80, false, "losemusic.mp3");
            
            getWorld().addObject(new DelayAudio("evillaugh.mp3", 80, false, 1000L), 0, 0);
            getWorld().addObject(new ZombieHand(), 250, 500);
        } else {
            Greenfoot.setWorld(new Arena());
        }
    }

    private void scaleAll(GreenfootImage[] images, double f) {
        for (GreenfootImage img : images)
            img.scale((int)(img.getWidth() * f), (int)(img.getHeight() * f));
    }

    private void scaleImage(GreenfootImage img, double f) {
        if (img != null) img.scale((int)(img.getWidth() * f), (int)(img.getHeight() * f));
    }
}