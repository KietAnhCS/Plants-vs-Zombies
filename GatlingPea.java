import greenfoot.*;
import java.util.*;

public class GatlingPea extends Plant
{
    private GreenfootImage[] idle;
    private GreenfootImage[] shoot;
    private int shootCount = 0;
    private boolean resetFrame = false;
    private boolean shooting = false;
    
    private boolean isPoweredUp = false;
    private long powerUpStartTime;
    private final long POWER_UP_DURATION = 3000L;
    
    private long baseShootDelay = 2000L; 
    private long shootDelay = 2000L;      
    
    private long lastFrame2 = System.nanoTime();
    private long deltaTime2;

    public GatlingPea() {
        maxHp = 60;
        hp = maxHp;
        shoot = importSprites("GatlingPea", 19);
        idle = importSprites("GatlingPea", 19);
        setImage(idle[0]);
    }

    public void activatePlantFood() {
        this.isPoweredUp = true;
        this.powerUpStartTime = System.currentTimeMillis();
        this.shootDelay = 40L; 
        this.hp = maxHp;
    }

    @Override
    public void hit(int dmg) {
        if (getWorld() != null && isLiving()) {
            hp -= dmg;
        }
    }

    @Override
    public void update() {
        if (getWorld() == null) return;

        if (isPoweredUp) {
            if (System.currentTimeMillis() - powerUpStartTime > POWER_UP_DURATION) {
                isPoweredUp = false;
                shootDelay = baseShootDelay;
            }
        }

        PlayScene = (PlayScene)getWorld();
        currentFrame = System.nanoTime();

        handleAction();
        checkZombieInRow();
    }

    private void handleAction() {
        deltaTime2 = (currentFrame - lastFrame2) / 1000000;

        if (isPoweredUp) {
            executeShootingLogic();
        } 
        else if (shooting) {
            if (shootCount >= 5) {
                if (deltaTime2 >= shootDelay) {
                    shootCount = 0;
                } else {
                    animate(idle, 225, true);
                }
            } else {
                executeShootingLogic();
            }
        } else {
            animate(idle, 225, true);
            shootCount = 0;
            lastFrame2 = currentFrame - (shootDelay * 1000000);
        }
    }

    private void executeShootingLogic() {
        if (!resetFrame) {
            setFrame(1);
            resetFrame = true;
        }
        
        int animSpeed = isPoweredUp ? 10 : 15; 
        animate(shoot, animSpeed, false);

        if (frame >= 7) {
            int myRow = getYPos();
            if (getWorld() != null && myRow != -1) {
                AudioPlayer.play(80, "throw.mp3", "throw2.mp3");
                PlayScene.addObject(new FirePea(myRow), getX() + 25, getY() - 17);
                
                setFrame(1); 
                shootCount++;
                
                if (shootCount >= 5 && !isPoweredUp) {
                    lastFrame2 = System.nanoTime();
                    resetFrame = false;
                }
            }
        }
    }

    private void checkZombieInRow() {
        int myRow = getYPos();
        if (myRow == -1 || PlayScene.level == null || PlayScene.level.zombieRow == null) return;

        List<Zombie> zombiesInRow = PlayScene.level.zombieRow.get(myRow);
        if (zombiesInRow == null || zombiesInRow.isEmpty()) {
            shooting = false;
        } else {
            boolean found = false;
            for (Zombie i : zombiesInRow) {
                if (i != null && i.getWorld() != null && i.getX() > getX() && i.getX() <= PlayScene.getWidth() + 10) {
                    found = true;
                    break;
                }
            }
            shooting = found;
        }
    }
}