import greenfoot.*;

class LossSequenceTransition extends Actor {
    private int fadeSpeed;
    private int alpha = 0; 
    private World restartWorld;
    
    private boolean isFadingIn = true;   
    private boolean isFadingOut = false; 
    
    private int displayCounter = 0; 
    private static final int DISPLAY_DURATION = 150; 

    public LossSequenceTransition(String imgName, int speed, World restartWorld) {
        this.fadeSpeed = speed;
        this.restartWorld = restartWorld;
        
        GreenfootImage img = new GreenfootImage(imgName);
        
        double scaleFactor = 1.3; 
        
        int newWidth = (int)(img.getWidth() * scaleFactor);
        int newHeight = (int)(img.getHeight() * scaleFactor);
        
        img.scale(newWidth, newHeight);
        
        setImage(img);
        
        getImage().setTransparency(0);
    }

    public void act() {
        if (getWorld() == null) return;

        if (isFadingIn) {
            if (alpha + fadeSpeed <= 255) {
                alpha += fadeSpeed;
                getImage().setTransparency(alpha);
            } else {
                getImage().setTransparency(255);
                isFadingIn = false; 
            }
        } 
        else if (!isFadingOut) {
            displayCounter++;
            if (displayCounter >= DISPLAY_DURATION) {
                isFadingOut = true; 
            }
        } 
        else {
            if (alpha - fadeSpeed >= 0) {
                alpha -= fadeSpeed;
                getImage().setTransparency(alpha);
            } else {
                getImage().setTransparency(0);
                
                int centerX = getWorld().getWidth() / 2;
                int centerY = getWorld().getHeight() / 2;
                
                getWorld().addObject(new GameOverPanel(), centerX, centerY);
                
                getWorld().addObject(new Retry(restartWorld), centerX, centerY + 58);
                
                getWorld().removeObject(this);
            }
        }
    }
}