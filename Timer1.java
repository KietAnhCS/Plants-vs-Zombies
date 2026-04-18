import java.util.Timer;
import java.util.TimerTask;
import greenfoot.*;

class Timer1 extends TimerTask
{
    public GreenfootImage[] image;
    public String spriteSheet;
    public int frame;
    public int frame2;
    
    
    Timer1(GreenfootImage[] image, String filename, int frame, int frame2) {
        this.image = image;
        this.spriteSheet = filename;
        this.frame = frame;
        this.frame2 = frame2;
    }
    public void run() {
        GreenfootImage first = new GreenfootImage(spriteSheet+(frame+1)+".png");
        GreenfootImage second = new GreenfootImage(spriteSheet+(frame2+1)+".png");
        
        image[frame] = first;
        image[frame2] = second;
        
        
    }
}
