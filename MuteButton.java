import greenfoot.*;

public class MuteButton extends Actor {
    private boolean lastMuteState;

    public MuteButton() {
        this.lastMuteState = AudioManager.isMuted();
        updateImage();
    }

    public void act() {
        if (Greenfoot.mouseClicked(this)) {
            AudioManager.toggleMute();
            if (!AudioManager.isMuted()) {
                AudioManager.playSound("gravebutton.mp3"); 
            }
            updateImage();
        }
        
        if (AudioManager.isMuted() != lastMuteState) {
            updateImage();
        }
    }

    private void updateImage() {
        lastMuteState = AudioManager.isMuted();
        GreenfootImage img = new GreenfootImage(51, 51);
        
        img.clear();
        if (lastMuteState) {
            img.setColor(Color.RED);
            img.fillOval(0, 0, 50, 50);
            img.setColor(Color.WHITE);
            img.drawString("MUTE", 10, 32);
        } else {
            img.setColor(new Color(128, 0, 128));
            img.fillOval(0, 0, 50, 50);
            img.setColor(Color.WHITE);
            img.drawString("ON", 18, 32);
        }
        setImage(img);
    }
}