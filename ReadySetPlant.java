import greenfoot.*;

public class ReadySetPlant extends SpriteAnimator {
    private final GreenfootImage[] readySprites;
    private final GreenfootImage[] setSprites;
    private final GreenfootImage plantImage;

    private enum Phase { READY, SET, PLANT }
    private Phase phase = Phase.READY;
    private int counter = 0;

    public ReadySetPlant() {
        readySprites = importSprites("Ready__", 11);
        setSprites = importSprites("Set__", 12);
        plantImage = new GreenfootImage("PLANT!.png");
    }

    @Override
    public void update() {
        if (getWorld() == null) return;
        if (((PlayScene)getWorld()).isGameOver) return;

        switch (phase) {
            case READY:
                if (animate(readySprites, 40, false)) {
                    phase = Phase.SET;
                }
                break;

            case SET:
                if (animate(setSprites, 40, false)) {
                    phase = Phase.PLANT;
                }
                break;

            case PLANT:
                setImage(plantImage);
                counter++;
                if (counter >= 60) {
                    getWorld().removeObject(this);
                }
                break;
        }
    }
}