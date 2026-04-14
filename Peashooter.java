import greenfoot.*;

public class Peashooter extends Plant
{
    private GreenfootImage[] idle;
    private GreenfootImage[] shoot;
    private boolean shootOnce = false;
    private boolean shooting = false;
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
            // Hiệu ứng nháy trắng khi bị cắn
            if (!shootOnce) hitFlash(idle, "peashooter");
            else hitFlash(shoot, "peashootershoot");  
            hp -= dmg;
        }
    }

    @Override
    public void update() {
        if (getWorld() == null) return;
        MyWorld = (MyWorld)getWorld();
        currentFrame = System.nanoTime();

        handleAnimationAndShooting();
        
        if (getWorld() == null) return;
        checkZombieInRow();
    }

    private void handleAnimationAndShooting() {
        if (!shooting) {
            animate(idle, 150, true);
            lastFrame2 = System.nanoTime();
        } else {
            deltaTime2 = (currentFrame - lastFrame2) / 1000000;
            if (deltaTime2 < shootDelay) {
                animate(idle, 1, true); // Giữ frame idle khi chờ nạp đạn
                shootOnce = false;
            } else {
                if (!shootOnce) {
                    shootOnce = true;
                    frame = 0; // Reset frame để bắt đầu animation bắn
                }
                
                // Bắn đạn tại frame thứ 3 của animation bắn
                if (frame >= 3 && shootOnce) {
                    int myRow = getYPos();
                    if (getWorld() != null && myRow != -1) {
                        AudioPlayer.play(80, "throw.mp3", "throw2.mp3");
                        MyWorld.addObject(new Pea(myRow), getX() + 25, getY() - 17);
                        lastFrame2 = currentFrame;
                        shootOnce = false; // Bắn xong 1 viên thì đợi delay
                    }
                }
                animate(shoot, 10, false);
            }
        }
    }

    private void checkZombieInRow() {
        int myRow = getYPos(); 
        if (myRow == -1 || MyWorld.level == null) return;

        // Kiểm tra xem hàng hiện tại có Zombie không
        if (MyWorld.level.zombieRow.get(myRow).isEmpty()) {
            shooting = false;
        } else {
            boolean found = false;
            for (Zombie i : MyWorld.level.zombieRow.get(myRow)) {
                if (i.getWorld() != null && i.getX() > getX() && i.getX() <= MyWorld.getWidth() + 10) {
                    found = true;
                    break;
                }
            }
            shooting = found;
        }
    }
}