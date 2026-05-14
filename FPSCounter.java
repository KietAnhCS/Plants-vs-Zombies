import greenfoot.*;

public class FPSCounter extends Actor {
    private long lastTime = 0;
    private int frameCount = 0;
    private double fps = 0;

    public void act() {
        calculateFPS();
        updateImage();
    }

    private void calculateFPS() {
        long currentTime = System.currentTimeMillis();
        frameCount++;
        
        if (currentTime - lastTime >= 1000) {
            fps = frameCount;
            frameCount = 0;
            lastTime = currentTime;
        }
    }

    private void updateImage() {
        String text = "FPS: " + (int)fps;
        GreenfootImage img = new GreenfootImage(text, 24, Color.WHITE, new Color(0, 0, 0, 150));
        setImage(img);
    }
}