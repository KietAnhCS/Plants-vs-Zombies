import greenfoot.*;

public class MegaGatlingPea extends Plant
{
    private GreenfootImage[] idle;
    private GreenfootImage[] shoot;
    private boolean shootOnce = false;
    private int shootCount = 0;
    private boolean resetFrame = false;
    private boolean shooting = false;
    
    private boolean isPoweredUp = false;
    private long powerUpStartTime;
    private final long POWER_UP_DURATION = 3000L;
    private long baseShootDelay = 600L; 
    private long shootDelay = 600L;
    
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
        this.shootDelay = 60L; 
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

        currentFrame = System.nanoTime();

        handleAction();
        checkZombieInRow();
    }

    private void handleAction() {
        boolean activeShooting = shooting || isPoweredUp;

        if (!activeShooting) {
            animate(idle, 225, true);
            lastFrame2 = System.nanoTime();
            shootCount = 0;
            resetFrame = false;
        } else {
            deltaTime2 = (currentFrame - lastFrame2) / 1000000;
            
            if (deltaTime2 < shootDelay) {
                if (!isPoweredUp) {
                    animate(idle, 225, true);
                    shootCount = 0;
                    resetFrame = false;
                } else {
                    executeShootingLogic();
                }
            } else {
                if (shootCount >= 7 && !isPoweredUp) {
                    lastFrame2 = currentFrame;
                }
                executeShootingLogic();
            }
        }
    }

    private void executeShootingLogic() {
        if (!resetFrame) {
            setFrame(1);
            resetFrame = true;
        }
        
        if (frame >= 12) {
            int myRow = getYPos();
            if (getWorld() != null && myRow != -1) {
                AudioPlayer.play(80, "throw.mp3", "throw2.mp3");
                PlayScene.addObject(new FirePea(myRow), getX() + 20, getY());
                setFrame(1);
                shootCount++;
            }
        }
        
        int animSpeed = isPoweredUp ? 20 : 40;
        animate(shoot, animSpeed, false);
    }

    private void checkZombieInRow() {
        int myRow = getYPos();
        if (myRow == -1 || PlayScene.level == null) return;

        if (PlayScene.level.zombieRow.get(myRow).isEmpty()) {
            shooting = false;
        } else {
            boolean found = false;
            for (Zombie i : PlayScene.level.zombieRow.get(myRow)) {
                if (i.getWorld() != null && i.getX() > getX() && i.getX() <= PlayScene.getWidth() + 10) {
                    found = true;
                    break;
                }
            }
            shooting = found;
        }
    }
}