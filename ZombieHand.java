import greenfoot.*;

public class ZombieHand extends Actor {
    private GreenfootImage[] frames = new GreenfootImage[6];
    private int timer = 0;
    private int currentFrame = 0;
    private int animationSpeed = 6; 

    public ZombieHand() {
        // Load 6 ảnh: hand1.png -> hand6.png vào index 0 -> 5
        for (int i = 0; i < 6; i++) {
            frames[i] = new GreenfootImage("hand" + (i + 1) + ".png");
            // frames[i].scale(100, 100); // Bỏ comment nếu muốn chỉnh kích thước tay
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
        // Sau khi diễn xong, cái tay đứng yên 1 chút rồi biến mất 
        // hoặc để Transition của World xử lý.
        // getWorld().removeObject(this); 
    }
}