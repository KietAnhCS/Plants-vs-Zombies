import greenfoot.*;

public class Peashooter extends Plant
{
    private GreenfootImage[] idle;
    private GreenfootImage[] shoot;
    private boolean shootOnce = false;
    private boolean shooting = false;
    
    // Logic cho Plant Food
    private boolean isPoweredUp = false;
    private long powerUpStartTime;
    private final long POWER_UP_DURATION = 3000L; // 3 giây
    private long baseShootDelay = 1500L; 
    private long shootDelay = 1500L; 

    private long lastFrame2 = System.nanoTime();
    private long deltaTime2;
    
    public Peashooter() {
        maxHp = 60;
        hp = maxHp;
        shoot = importSprites("peashootershoot", 3);
        idle = importSprites("peashooter", 9);
        setImage(idle[0]);
    }
   
    @Override
    public void hit(int dmg) {
        if (getWorld() != null && isLiving()) {
            if (!shootOnce) hitFlash(idle, "peashooter");
            else hitFlash(shoot, "peashootershoot");  
            hp -= dmg;
        }
    }

    // Hàm để clickPlantFood gọi vào
    public void activatePlantFood() {
        this.isPoweredUp = true;
        this.powerUpStartTime = System.currentTimeMillis();
        this.shootDelay = 60L; // Bắn siêu nhanh (0.15s một viên)
        this.hp = maxHp; // Hồi máu như bản gốc
    }

    @Override
    public void update() {
        if (getWorld() == null) return;
        
        // Kiểm tra thời gian hết buff
        if (isPoweredUp) {
            if (System.currentTimeMillis() - powerUpStartTime > POWER_UP_DURATION) {
                isPoweredUp = false;
                shootDelay = baseShootDelay;
            }
        }

        PlayScene = (PlayScene)getWorld();
        currentFrame = System.nanoTime();

        handleAnimationAndShooting();
        
        if (getWorld() == null) return;
        checkZombieInRow();
    }

    private void handleAnimationAndShooting() {
        // Khi có lá, ép buộc trạng thái shooting để xả đạn liên tục
        boolean activeShooting = shooting || isPoweredUp;

        if (!activeShooting) {
            animate(idle, 150, true);
            lastFrame2 = System.nanoTime();
        } else {
            deltaTime2 = (currentFrame - lastFrame2) / 1000000;
            if (deltaTime2 < shootDelay) {
                // Nếu đang chờ nạp đạn
                if (!isPoweredUp) {
                    animate(idle, 1, true); 
                } else {
                    // Trong trạng thái Plant Food, cho animation mượt hơn không đứng yên
                    animate(shoot, 1, false); 
                }
                shootOnce = false;
            } else {
                if (!shootOnce) {
                    shootOnce = true;
                    frame = 0; 
                }
                
                // Bắn đạn
                if (frame >= 1 && shootOnce) {
                    int myRow = getYPos();
                    if (getWorld() != null && myRow != -1) {
                        AudioPlayer.play(80, "throw.mp3", "throw2.mp3");
                        PlayScene.addObject(new Pea(myRow), getX() + 25, getY() - 17);
                        lastFrame2 = currentFrame;
                        shootOnce = false; 
                    }
                }
                
                // Tăng tốc độ animation khi có lá (delay thấp hơn = nhanh hơn)
                int animDelay = isPoweredUp ? 2 : 10;
                animate(shoot, animDelay, false);
            }
        }
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