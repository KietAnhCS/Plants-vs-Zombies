import greenfoot.*;
public class SunCounter extends Actor
{
    public static int x = 120; 
    public static int y = 50;
    
    public int sun = 100; 
    public static final int textY = 45;
    
    private PlayScene PlayScene;
    
    public SunCounter() {
        setImage("suncounter.png");
        updateText();
    }

    public void act() {}
    
    public void addedToWorld(World world) {
        PlayScene = (PlayScene)world;
        x = getX();
        y = getY();
        updateText();
    }

    public void updateText() {
        if (sun > 999999) sun = 999999;
        if (sun < 0) sun = 0;
        GreenfootImage bg = new GreenfootImage("suncounter.png");
        String sunStr = String.valueOf(sun);
        int len = sunStr.length();
        int charWidth = 12; 
        int startX = (bg.getWidth() / 2) - ((len * charWidth) / 2) + 5; 
        for (int i = 0; i < len; i++) {
            char c = sunStr.charAt(i);
            try {
                GreenfootImage numImg = new GreenfootImage("text" + c + ".png");
                bg.drawImage(numImg, startX + (i * charWidth), textY);
            } catch (Exception e) {}
        }
        setImage(bg);
    }

    public void addSun(int amount) {
        this.sun += amount;
        updateText();
    }

    public void removeSun(int amount) {
        this.sun -= amount;
        updateText();
    }
}