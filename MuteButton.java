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
        int size = 51;
        GreenfootImage img = new GreenfootImage(size, size);
        img.clear();

        if (lastMuteState) {
            img.setColor(new Color(180, 0, 0));
            img.fillOval(0, 0, size - 1, size - 1);
            img.setColor(new Color(220, 50, 50));
            img.fillOval(3, 3, size - 9, size - 9);
            img.setColor(new Color(255, 80, 80));
            img.drawLine(14, 14, size - 16, size - 16);
            img.drawLine(15, 14, size - 15, size - 16);
            img.setColor(Color.WHITE);
            img.setFont(new Font("Arial", true, false, 10));
            img.drawString("MUTE", 9, 33);
        } else {
            img.setColor(new Color(80, 0, 100));
            img.fillOval(0, 0, size - 1, size - 1);
            img.setColor(new Color(140, 40, 180));
            img.fillOval(3, 3, size - 9, size - 9);
            img.setColor(new Color(200, 130, 255, 120));
            img.fillOval(12, 10, 14, 8);
            img.setColor(Color.WHITE);
            img.setFont(new Font("Arial", true, false, 13));
            img.drawString("ON", 15, 32);
        }

        setImage(img);
    }
}