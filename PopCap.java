import greenfoot.*; 

public class PopCap extends World
{
    public int counter = 0;
    public boolean hasTransitioned = false; 

    public PopCap()
    {    
        super(1111, 705, 1, false); 
        setPaintOrder(EndTransition.class, Transition.class);
    }

    public void act() {
        if (!AudioManager.isSoundPlaying("menutheme.mp3")) {
            AudioManager.playBGM("menutheme.mp3");
        }

        counter++;

        if (counter > 100 && !hasTransitioned) {
            addObject(new Transition(true, new MainMenu(), 6), getWidth()/2, getHeight()/2);
            hasTransitioned = true; 
        }
    }
    
    @Override
    public void stopped() {
        AudioManager.stopBGM();
    }
}