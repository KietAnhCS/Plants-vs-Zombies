import greenfoot.*;

public class PopCap extends World {
    private int     counter          = 0;
    private boolean hasTransitioned  = false;

    public PopCap() {
        super(1111, 705, 1, false);
        setPaintOrder(EndTransition.class, Transition.class);
        
        GreenfootImage bg = new GreenfootImage(1111, 705);
        bg.setColor(Color.BLACK);
        bg.fill();
        
        GreenfootImage logo = new GreenfootImage("ea.png");
        int centerX = (1111 - logo.getWidth()) / 2;
        int centerY = (705 - logo.getHeight()) / 2;
        bg.drawImage(logo, centerX, centerY);
        
        setBackground(bg);
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
        counter++;
        if (counter > 100 && !hasTransitioned) {
            hasTransitioned = true;
            addObject(new Transition(true, new MainMenu(), 6),
                getWidth() / 2, getHeight() / 2);
        }
    }
}