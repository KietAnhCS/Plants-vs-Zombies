import greenfoot.*;
import java.util.List;

public class GatlingPea2 extends Plant {
    private GreenfootImage[] idle;
    private GreenfootImage[] shoot;
    
    private int shootCount = 0;
    private boolean shooting = false;
    private boolean isPoweredUp = false;
    
    private long powerUpStartTime;
    private final long POWER_UP_DURATION = 3000L;
    
    private long lastActionTime = System.currentTimeMillis();
    private long shootDelay = 2000L; // Thời gian nghỉ giữa các loạt đạn
    private final long BURST_INTERVAL = 100L; // Thời gian giữa mỗi viên đạn trong loạt đạn 5 viên

    public GatlingPea2() {
        maxHp = 100; // Gatling Pea nên trâu hơn một chút
        hp = maxHp;
        // Sử dụng chung sprites nếu chúng giống nhau hoặc import riêng
        shoot = importSprites("GatlingPea", 19);
        idle = importSprites("GatlingPea", 19);
        setImage(idle[0]);
    }

    public void activatePlantFood() {
        this.isPoweredUp = true;
        this.powerUpStartTime = System.currentTimeMillis();
        this.hp = maxHp;
    }

    @Override
    public void hit(int dmg) {
        if (getWorld() != null && isLiving()) {
            hp -= dmg;
            hitFlash(shooting ? shoot : idle, "GatlingPea");
        }
    }

    @Override
    public void update() {
        if (getWorld() == null) return;
        
        playScene = (PlayScene) getWorld();
        updatePowerUpStatus();
        checkZombieInRow();
        handleAction();
    }

    private void updatePowerUpStatus() {
        if (isPoweredUp && (System.currentTimeMillis() - powerUpStartTime > POWER_UP_DURATION)) {
            isPoweredUp = false;
        }
    }

    private void handleAction() {
        long currentTime = System.currentTimeMillis();

        // Trạng thái Plant Food: Bắn liên tục không ngừng
        if (isPoweredUp) {
            animate(shoot, 40, false);
            if (currentTime - lastActionTime >= 50) { // Tốc độ cực nhanh
                fireFirePea();
                lastActionTime = currentTime;
            }
            return;
        }

        // Trạng thái bình thường
        if (shooting) {
            if (shootCount < 5) {
                // Đang trong loạt bắn 5 viên
                animate(shoot, 100, false);
                if (currentTime - lastActionTime >= BURST_INTERVAL) {
                    fireFirePea();
                    shootCount++;
                    lastActionTime = currentTime;
                }
            } else {
                // Đã bắn xong 5 viên, đợi hồi chiêu (shootDelay)
                animate(idle, 225, true);
                if (currentTime - lastActionTime >= shootDelay) {
                    shootCount = 0; // Reset để bắt đầu loạt bắn mới
                    lastActionTime = currentTime;
                }
            }
        } else {
            // Không có zombie: Nghỉ ngơi
            animate(idle, 225, true);
            shootCount = 0;
        }
    }

    private void fireFirePea() {
        if (getWorld() != null) {
            AudioManager.playSound(80, false, "throw.mp3", "throw2.mp3");
            getWorld().addObject(new FirePea(getYPos()), getX() + 25, getY() - 17);
        }
    }

    private void checkZombieInRow() {
        if (playScene == null || playScene.level == null) return;

        List<Zombie> rowZombies = playScene.level.zombieRow.get(getYPos());
        if (rowZombies == null || rowZombies.isEmpty()) {
            shooting = false;
            return;
        }

        shooting = rowZombies.stream().anyMatch(z -> 
            z.getWorld() != null && 
            z.getX() > getX() && 
            z.getX() <= playScene.getWidth() + 10
        );
    }
}