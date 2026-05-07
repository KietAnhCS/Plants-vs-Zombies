import greenfoot.*;

public class PopCap extends World {
    private int     counter          = 0;
    private boolean hasTransitioned  = false;

    public PopCap() {
        super(1111, 705, 1, false);
        setPaintOrder(EndTransition.class, Transition.class);
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