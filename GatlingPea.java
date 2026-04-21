import greenfoot.*;

public class GatlingPea extends Plant
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
    private long baseShootDelay = 1100L; 
    private long shootDelay = 1100;
    
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

        PlayScene = (PlayScene)getWorld();
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
                if (shootCount >= 5 && !isPoweredUp) {
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
        
        if (frame >= 7) {
            int myRow = getYPos();
            if (getWorld() != null && myRow != -1) {
                AudioPlayer.play(80, "throw.mp3", "throw2.mp3");
                PlayScene.addObject(new Pea(myRow), getX() + 25, getY() - 17);
                setFrame(1);
                shootCount++;
            }
        }
        
        int animSpeed = isPoweredUp ? 30 : 70;
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