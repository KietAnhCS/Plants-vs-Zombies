import greenfoot.*;
import java.util.List;

public class PotatoMine extends Plant {
    private GreenfootImage[] idle;
    private GreenfootImage[] arm;
    
    private boolean playOnce = false;
    public boolean armed = false;
    private boolean playSFX = false;
    
    private long startTime;
    private final long ARMING_TIME = 10000L; // 10 giây để nạp mìn

    public PotatoMine() {
        // Sprite "potato" nên là trạng thái đã trồi lên, "arm" là lúc đang trồi lên
        idle = importSprites("potato", 5);
        arm = importSprites("potatomine", 3);
        maxHp = 100;
        hp = maxHp;
        startTime = System.currentTimeMillis();
        
        // Ban đầu ở dưới đất (có thể dùng một sprite mầm cây nhỏ nếu có)
        setImage(arm[0]); 
    }

    @Override
    public void update() {
        if (getWorld() == null) return;
        playScene = (PlayScene) getWorld();

        long elapsed = System.currentTimeMillis() - startTime;

        if (elapsed < ARMING_TIME) {
            // Giai đoạn đang nạp: Có thể nhấp nhô nhẹ dưới đất
            return; 
        }

        // Giai đoạn bắt đầu trồi lên
        if (!armed) {
            if (!playSFX) {
                AudioManager.playSound(80, false, "dirt_rise.mp3");
                playSFX = true;
            }
            
            animate(arm, 200, false);
            
            // Nếu chạy hết animation trồi lên thì coi như đã sẵn sàng (armed)
            if (frame >= arm.length - 1) {
                armed = true;
            }
        } else {
            // Đã sẵn sàng nổ
            animate(idle, 200, true);
            checkExplosion();
        }
    }

    @Override
    public void hit(int dmg) {
        // Potato Mine chỉ bị cắn chết khi CHƯA nổ (chưa armed)
        if (isLiving() && !armed) {
            hp -= dmg;
            hitFlash(arm, "potatomine");
        }
    }

    public void checkExplosion() {
        if (playScene == null || playScene.level == null) return;

        List<Zombie> zombies = playScene.level.zombieRow.get(getYPos());
        if (zombies == null || zombies.isEmpty()) return;

        for (Zombie i : zombies) {
            if (i != null && i.getWorld() != null) {
                // Khoảng cách nổ (thường là rất gần)
                if (Math.abs(i.getX() - getX()) < 30) {
                    explode();
                    return;
                }
            }
        }
    }

    private void explode() {
        // Tạo hiệu ứng nổ (đảm bảo class Explosion xử lý việc trừ máu zombie)
        getWorld().addObject(new Explosion(playScene.level.zombieRow.get(getYPos())), getX(), getY() - 25);
        AudioManager.playSound(90, false, "potato_mine.mp3");
        
        // Giải phóng ô lưới
        if (playScene.GridManager != null) {
            playScene.GridManager.removePlant(getXPos(), getYPos());
        }
        
        // Xóa chính mình khỏi thế giới
        getWorld().removeObject(this);
    }
}