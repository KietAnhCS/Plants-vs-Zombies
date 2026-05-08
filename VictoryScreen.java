import greenfoot.*;
import java.util.List;

public class VictoryScreen extends World {
    private GifImage victoryGif;
    private boolean gifFinished = false;

    public VictoryScreen() {    
        super(1111, 698, 1); 
        this.victoryGif = new GifImage("victorygif.gif");
        
        AudioManager.getInstance().playSound(80, false, "victory.mp3");
        
        GreenfootImage bg = new GreenfootImage(1111, 698);
        bg.setColor(Color.BLACK);
        bg.fill();
        setBackground(bg);
        
        prepare();
    }

    private void prepare() {
        addObject(new VictoryGifActor(), getWidth() / 2, getHeight() / 2);
    }

    private class VictoryGifActor extends Actor {
        public void act() {
            if (!gifFinished) {
                GreenfootImage current = victoryGif.getCurrentImage();
                setImage(current);
                
                List<GreenfootImage> images = victoryGif.getImages();
                if (images != null && !images.isEmpty()) {
                    if (current == images.get(images.size() - 1)) {
                        gifFinished = true;
                    }
                }
            }
        }
    }

    public void act() {
    }
}