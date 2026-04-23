import greenfoot.*;
import java.util.*;

public class MegaGatlingPea extends Plant
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

    public MegaGatlingPea() {
        maxHp = 60;
        hp = maxHp;
        shoot = scaleImages(importSprites("MegaGatlingPea_shoot", 31), 0.75);
        idle = scaleImages(importSprites("MegaGatlingPea_idle", 31), 0.75);
        setImage(idle[0]);
    }

    @Override
    public void addedToWorld(World world) {
        super.addedToWorld(world); 
        setLocation(getX() + 15, getY() - 15);
    }

    private GreenfootImage[] scaleImages(GreenfootImage[] images, double factor) {
        for (GreenfootImage img : images) {
            int newWidth = (int)(img.getWidth() * factor);
            int newHeight = (int)(img.getHeight() * factor);
            img.scale(newWidth, newHeight);
        }
        return images;
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
            if (shootCount >= 7) {
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
        
        int animSpeed = isPoweredUp ? 5 : 10; 
        animate(shoot, animSpeed, false);

        if (frame >= 12) {
            int myRow = getYPos();
            if (getWorld() != null && myRow != -1) {
                AudioPlayer.play(80, "throw.mp3", "throw2.mp3");
                PlayScene.addObject(new FirePea(myRow), getX() + 20, getY());
                
                setFrame(1); 
                shootCount++;
                
                if (shootCount >= 7 && !isPoweredUp) {
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