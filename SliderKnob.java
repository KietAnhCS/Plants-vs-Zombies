import greenfoot.*;

public class SliderKnob extends Actor {
    private boolean dragging = false;
    private SliderBar bar;
    private int barWidth;
    private String type;

    public SliderKnob(SliderBar bar, int barWidth, String type) {
        this.bar = bar;
        this.barWidth = barWidth;
        this.type = type;
        updateImage();
    }

    public void syncMuteState() { updateImage(); }

    private void updateImage() {
        GreenfootImage img = new GreenfootImage(25, 25);
        if (AudioManager.getInstance().isMuted()) {
            img.setColor(Color.DARK_GRAY);
        } else {
            img.setColor(new Color(128, 0, 128)); 
        }
        img.fillOval(0, 0, 24, 24);
        setImage(img);
    }

    public void setDragging(boolean state) { this.dragging = state; }

    public void act() {
        if (AudioManager.getInstance().isMuted()) {
            dragging = false;
            return;
        }

        if (Greenfoot.mousePressed(this)) dragging = true;
        if (Greenfoot.mouseClicked(null) || Greenfoot.mouseDragEnded(null)) dragging = false;

        if (dragging) {
            MouseInfo mouse = Greenfoot.getMouseInfo();
            if (mouse != null) {
                int mouseX = mouse.getX();
                int leftLimit = bar.getX() - (barWidth / 2);
                int rightLimit = bar.getX() + (barWidth / 2);

                if (mouseX < leftLimit) mouseX = leftLimit;
                if (mouseX > rightLimit) mouseX = rightLimit;
                setLocation(mouseX, bar.getY());

                double percent = (double)(mouseX - leftLimit) / barWidth;
                int newVolume = (int)(percent * 100);
                
                if (type.equals("BGM")) AudioManager.getInstance().setBGMVolume(newVolume);
                else AudioManager.getInstance().setSFXVolume(newVolume);
            }
        }
    }
}