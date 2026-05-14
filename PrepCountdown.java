import greenfoot.*; 

public class PrepCountdown extends Actor {

    private static final int W = 220;
    private static final int H = 70;
    private final String label;
    private int seconds;

    public PrepCountdown(String label, int initialSeconds) {
        this.label = label.toUpperCase();
        this.seconds = initialSeconds;
        redraw();
    }

    public void setSeconds(int s) {
        if (s != seconds) {
            this.seconds = s;
            redraw();
        }
    }

    private void redraw() {
        GreenfootImage img = new GreenfootImage(W, H);
        
        img.setColor(new Color(0, 0, 0, 160));
        img.fillRect(4, 4, W - 4, H - 4);

        img.setColor(new Color(50, 50, 50));
        img.fillRect(0, 0, W - 4, H - 4);

        img.setColor(Color.WHITE);
        img.drawRect(0, 0, W - 4, H - 4);
        img.drawRect(1, 1, W - 6, H - 6);

        img.setFont(new Font("Courier New", true, false, 18));
        img.setColor(Color.BLACK);
        img.drawString(label, 17, 27);
        img.setColor(new Color(200, 200, 200));
        img.drawString(label, 15, 25);

        String text = String.valueOf(seconds);
        img.setFont(new Font("Courier New", true, false, 40));
        
        Color numCol = (seconds <= 3) ? new Color(255, 60, 60) : new Color(255, 215, 0);
        
        img.setColor(Color.BLACK);
        img.drawString(text, W - 62, H - 18);
        
        img.setColor(numCol);
        img.drawString(text, W - 65, H - 20);

        setImage(img);
    }

    @Override
    public void act() {
    }
}