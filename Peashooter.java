import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Lớp Peashooter - Đã được fix lỗi "Actor removed from world"
 */
public class Peashooter extends Plant
{
    private GreenfootImage[] idle;
    private GreenfootImage[] shoot;
    private boolean shootOnce = false;
    private boolean shooting = false;
    private long shootDelay = 17L; 
    private long lastFrame2 = System.nanoTime();
    private long deltaTime2;
    
    public Peashooter() {
        maxHp = 60;
        hp = maxHp;
        // Giả sử importSprites nằm ở lớp cha animatedObject
        shoot = importSprites("peashootershoot", 3);
        idle = importSprites("peashooter", 9);
        setImage(idle[0]);
    }
   
    @Override
    public void hit(int dmg) {
        // Luôn check isLiving (từ Plant) và getWorld để tránh crash khi đang bị ăn/xóa
        if (getWorld() != null && isLiving()) {
            if (!shootOnce) {
                hitFlash(idle, "peashooter");
            } else {
                hitFlash(shoot, "peashootershoot");  
            }
            hp = hp - dmg;
        }
    }

    @Override
    public void update() {
        // CHỐT CHẶN 1: Nếu máy cắt cỏ đã cán qua, dừng ngay lập tức
        if (getWorld() == null) return;
        
        MyWorld = (MyWorld)getWorld();
        currentFrame = System.nanoTime();

        // Xử lý logic bắn và animation
        handleAnimationAndShooting();

        // CHỐT CHẶN 2: Sau khi bắn hoặc animate, check lại world lần nữa trước khi lấy tọa độ check Zombie
        if (getWorld() == null) return;

        checkZombieInRow();
    }

    private void handleAnimationAndShooting() {
        if (!shooting) {
            animate(idle, 10, true);
            lastFrame2 = System.nanoTime();
        } else {
            deltaTime2 = (currentFrame - lastFrame2) / 1000000;
            if (deltaTime2 < shootDelay) {
                animate(idle, 1, true);
                shootOnce = false;
            } else {
                if (!shootOnce) {
                    shootOnce = true;
                    frame = 0;
                }
                
                // Khi frame đạt đến ngưỡng bắn (frame 3)
                if (frame >= 3) {
                    // CHỐT CHẶN 3: Check world trước khi spawn đạn và lấy getX/getY
                    if (getWorld() != null) {
                        AudioPlayer.play(80, "throw.mp3", "throw2.mp3");
                        MyWorld.addObject(new Pea(getYPos()), getX() + 25, getY() - 17);
                        lastFrame2 = currentFrame;
                    }
                }
                animate(shoot, 10, false);
            }
        }
    }

    private void checkZombieInRow() {
        int myRow = getYPos(); // Lấy hàng hiện tại của cây
        
        // Check xem hàng này có zombie không
        if (MyWorld.level.zombieRow.get(myRow).isEmpty()) {
            shooting = false;
        } else {
            boolean found = false;
            for (Zombie i : MyWorld.level.zombieRow.get(myRow)) {
                // Kiểm tra xem Zombie có ở trước mặt cây và trong tầm nhìn không
                // Phải check i.getWorld() vì zombie cũng có thể bị xóa bởi máy cắt cỏ
                if (i.getWorld() != null && i.getX() > getX() && i.getX() <= MyWorld.getWidth() + 10) {
                    found = true;
                    break;
                }
            }
            shooting = found;
        }
    }
}