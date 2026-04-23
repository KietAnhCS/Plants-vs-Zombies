import greenfoot.*;

public class AugmentCard extends Actor {
    private WaveManager manager;
    private String type;
    private boolean hovered = false;
    private static final int W_NORMAL = 250, H_NORMAL = 330;
    private static final int W_HOVER  = 270, H_HOVER  = 350;

    public AugmentCard(WaveManager manager, String type, Object dummy) {
        this.manager = manager;
        this.type = type;
        updateImage(W_NORMAL, H_NORMAL);
    }

    public void act() {
        handleHover();
        if (Greenfoot.mouseClicked(this)) {
            MouseInfo mouse = Greenfoot.getMouseInfo();
            if (mouse != null && mouse.getButton() == 1) {
                AudioPlayer.play(100, "gravebutton.mp3");
                applyAugmentEffect();
                manager.nextWave();
            }
        }
    }

    private void handleHover() {
        if (Greenfoot.mouseMoved(this)) {
            if (!hovered) {
                hovered = true;
                updateImage(W_HOVER, H_HOVER);
            }
        }
        if (Greenfoot.mouseMoved(null) && !Greenfoot.mouseMoved(this)) {
            if (hovered) {
                hovered = false;
                updateImage(W_NORMAL, H_NORMAL);
            }
        }
    }

    private void updateImage(int w, int h) {
        GreenfootImage img = new GreenfootImage(type + ".png");
        img.scale(w, h);
        setImage(img);
    }

    private void applyAugmentEffect() {
        PlayScene world = (PlayScene) getWorld();
        if (world != null && world.seedbank != null) {
            if (type.equals("rerollcard")) world.seedbank.addSun(150);
            else if (type.equals("TD")) world.seedbank.setTD(true);
        }
    }
}