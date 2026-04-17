import greenfoot.*;
import java.util.List;

public class Hero extends Actor {
    private int targetX, targetY;
    private boolean isMoving = false;
    private int speed = 5; 
    private int pickupRange = 60; 

    public void act() {
        handleRightClick();
        moveToTarget();
        autoCollectSun(); 
    }

    private void handleRightClick() {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        // Mouse button 3 là chuột phải
        if (mouse != null && Greenfoot.mousePressed(null) && mouse.getButton() == 3) {
            targetX = mouse.getX();
            targetY = mouse.getY();
            isMoving = true;
        }
    }

    private void moveToTarget() {
        if (isMoving) {
            double distance = Math.hypot(targetX - getX(), targetY - getY());
            if (distance > speed) {
                turnTowards(targetX, targetY);
                move(speed);
            } else {
                setLocation(targetX, targetY);
                isMoving = false;
            }
        }
    }

    private void autoCollectSun() {
        List<Sun> suns = getObjectsInRange(pickupRange, Sun.class);
        for (Sun s : suns) {
            if (s.getWorld() != null) {
                // 1. Kích hoạt trạng thái bay về túi tiền bên class Sun
                s.collectByHero(); 
                
                // 2. Cộng tiền (lấy từ world)
                PlayScene world = (PlayScene) getWorld();
                if (world.seedbank != null && world.seedbank.sunCounter != null) {
                    world.seedbank.sunCounter.addSun(25);
                }
                
                // 3. Âm thanh
                AudioPlayer.play(80, "points.mp3");
            }
        }
    }
}