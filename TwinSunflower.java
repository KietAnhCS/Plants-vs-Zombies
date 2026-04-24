import greenfoot.*;

public class TwinSunflower extends Plant
{
    private int sunProductionTime = 90000;
    private GreenfootImage[] idle;
    private boolean test = false;
    private long lastFrame2 = System.nanoTime();
    private long deltaTime2;

    public TwinSunflower() {
        idle = importSprites("twinsunflower", 10);
        maxHp = 100;
        hp = maxHp;
    }

    public void update() {
        currentFrame = System.nanoTime();
        produceSun();
        animate(idle, 150, true);
    }

    public void hit(int dmg) {
        if (isLiving()) {
            hitFlash(idle, "twinsunflower");
            hp = hp - dmg;
        }
    }

    public void produceSun() {
        deltaTime2 = (currentFrame - lastFrame2) / 1000000;
        
        if (deltaTime2 > sunProductionTime) {
            lastFrame2 = System.nanoTime();
            hitFlash(idle, "twinsunflower");
            test = true;
            
            PlayScene.addObject(new Sun(25), getX() + 20, getY() - 10);
            PlayScene.addObject(new Sun(25), getX() + 20, getY() - 10);
            PlayScene.addObject(new Sun(25), getX() + 20, getY() - 10);
        }
    }
}