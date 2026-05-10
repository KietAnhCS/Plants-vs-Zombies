import greenfoot.*;

public class SliderBar extends Actor {
    private int width = 200;
    private int height = 10;
    private SliderKnob knob;
    private String type; 

    public SliderBar(String type) {
        this.type = type;
        updateImage();
    }

    private void updateImage() {
        GreenfootImage img = new GreenfootImage(width + 2, height + 2);
        if (AudioManager.getInstance().isMuted()) {
            img.setColor(Color.DARK_GRAY);
            img.fillRect(0, 0, width, height);
            img.setColor(Color.BLACK);
            img.drawRect(0, 0, width - 1, height - 1);
        } else {
            img.setColor(Color.GRAY);
            img.fillRect(0, 0, width, height);
            img.setColor(Color.BLACK);
            img.drawRect(0, 0, width - 1, height - 1);
        }
        setImage(img);
    }

    protected void addedToWorld(World world) {
        int currentVol = type.equals("BGM") ? AudioManager.getInstance().getBGMVolume() : AudioManager.getInstance().getSFXVolume();
        
        int initialX = getX() - (width / 2) + (currentVol * width / 100);
        
        knob = new SliderKnob(this, width, type);
        world.addObject(knob, initialX, getY());
    }

    public void act() {
        updateImage();
        if (knob != null) knob.syncMuteState();

        if (!AudioManager.getInstance().isMuted() && Greenfoot.mousePressed(this)) {
            MouseInfo mouse = Greenfoot.getMouseInfo();
            if (mouse != null) {
                int mouseX = mouse.getX();
                int leftLimit = getX() - (width / 2);
                int rightLimit = getX() + (width / 2);

                if (mouseX < leftLimit) mouseX = leftLimit;
                if (mouseX > rightLimit) mouseX = rightLimit;
                knob.setLocation(mouseX, getY());
                knob.setDragging(true);
                
                double percent = (double)(mouseX - leftLimit) / width;
                int newVolume = (int)(percent * 100);
                
                if (type.equals("BGM")) AudioManager.getInstance().setBGMVolume(newVolume);
                else AudioManager.getInstance().setSFXVolume(newVolume);
            }
        }
    }

    public int getBarWidth() { return width; }
}