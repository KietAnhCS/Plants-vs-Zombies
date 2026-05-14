import greenfoot.*;

public class VictoryScreen extends World {
    public VictoryScreen() {  
        super(1111, 698, 1); 
        
        GreenfootImage bg = new GreenfootImage(1111, 698);
        bg.setColor(Color.BLACK);
        bg.fill();
        setBackground(bg);
        
        try {
            AudioManager.getInstance().playSound(80, false, "victory.mp3");
        } catch (Exception e) {
            System.out.println("Error: not found victory.mp3");
        }
        
        prepare();
    }

    private void prepare() {
        addObject(new VictoryGifActor(), getWidth() / 2, getHeight() / 2);
    }

    private class VictoryGifActor extends Actor {
        private GifImage gif = new GifImage("victorygif.gif");

        public void act() {
            setImage(gif.getCurrentImage());
        }
    }
}