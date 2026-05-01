import greenfoot.*;

public class ReadySetPlant extends SpriteAnimator {
    private final GreenfootImage[] ready;
    private final GreenfootImage[] set;
    private final GreenfootImage   plant;

    private enum Phase { READY, SET, PLANT }
    private Phase phase   = Phase.READY;
    private int   counter = 0;

    public ReadySetPlant() {
        ready = importSprites("Ready__", 11);
        set   = importSprites("Set__",   12);
        plant = new GreenfootImage("PLANT!.png");
    }

    @Override
    public void act() {
        switch (phase) {
            case READY:
                if (animate(ready, 40, false)) phase = Phase.SET;
                break;
            case SET:
                if (animate(set, 40, false)) phase = Phase.PLANT;
                break;
            case PLANT:
                setImage(plant);
                if (++counter >= 60) getWorld().removeObject(this);
                break;
        }
    }
}