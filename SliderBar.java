import greenfoot.*;
public class SliderBar extends Actor {
    private int width = 200;
    private int height = 10;
    private SliderKnob knob;

    public SliderBar() {
        updateImage();
    }

    private void updateImage() {
        GreenfootImage img = new GreenfootImage(width + 2, height + 2);
        if (AudioManager.isMuted()) {
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
        int initialX = getX() - (width / 2) + (AudioManager.getVolume() * width / 100);
        knob = new SliderKnob(this, width);
        world.addObject(knob, initialX, getY());
    }

    public void act() {
        updateImage();
        if (knob != null) knob.syncMuteState();

        if (!AudioManager.isMuted() && Greenfoot.mousePressed(this)) {
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
                AudioManager.setMasterVolume((int)(percent * 100));
            }
        }
    }

    public int getBarWidth() { return width; }
}