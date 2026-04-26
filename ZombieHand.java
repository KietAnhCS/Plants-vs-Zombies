import greenfoot.*;

public class ZombieHand extends Actor {
    private GreenfootImage[] frames = new GreenfootImage[6];
    private int timer = 0;
    private int currentFrame = 0;
    private int animationSpeed = 6; 

    public ZombieHand() {
        
        for (int i = 0; i < 6; i++) {
            frames[i] = new GreenfootImage("hand" + (i + 1) + ".png");
            
        }
        setImage(frames[0]);
    }

    public void act() {
        animate();
    }

    private void animate() {
        timer++;
        if (timer % animationSpeed == 0) {
            if (currentFrame < frames.length - 1) {
                currentFrame++;
                setImage(frames[currentFrame]);
            } else {
                onAnimationFinished();
            }
        }
    }

    private void onAnimationFinished() {
        
    }
}