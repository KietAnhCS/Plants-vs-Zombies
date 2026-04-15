import greenfoot.*;
import java.util.List;

public class BonkChoy extends Plant
{
    private GreenfootImage[] idle;
    private GreenfootImage[] punch;
    
    private int yOffset = -50; 
    private int xOffset = 40; 
    private boolean adjusted = false;

    // Chỉnh lại delay đấm cho khớp với animation (khoảng 400-500ms là vừa đẹp)
    private long lastAttackTime = System.currentTimeMillis();
    private final long attackDelay = 400L; 
    private final int damage = 10; // Tăng damage lên vì delay đã giảm xuống
    
    private boolean isCounterAttacking = false;
    private long counterAttackEndTime = 0;

    public BonkChoy() {
        maxHp = 300; 
        hp = maxHp;
        idle = importSprites("bonkchoy", 6);
        punch = importSprites("bonkchoyfight", 16);
        setImage(idle[0]);
    }

    @Override
    public void update() {
        if (getWorld() == null) return;

        if (!adjusted) {
            setLocation(getX() + xOffset, getY() + yOffset);
            adjusted = true;
        }

        // Kiểm tra nếu vẫn đang trong thời gian phản đòn
        if (isCounterAttacking) {
            if (System.currentTimeMillis() < counterAttackEndTime) {
                handlePunchLogic();
            } else {
                isCounterAttacking = false;
            }
        } else {
            animate(idle, 150, true);
        }
    }

    private void handlePunchLogic() {
        // LUÔN LUÔN chạy animation khi đang phản công
        animate(punch, 3, true);
        
        // CHỈ gây sát thương khi đến nhịp (attackDelay)
        // 400ms là tốc độ đấm thực tế của Bonk Choy trong game gốc
        if (System.currentTimeMillis() - lastAttackTime > attackDelay) {
            AudioPlayer.play(80, "bonk.mp3");
            
            // Quét và gây sát thương ngay lập tức
            List<Zombie> targets = getObjectsInRange(100, Zombie.class);
            for (Zombie z : targets) {
                if (z.getWorld() != null) {
                    z.hit(damage); // Zombie sẽ nháy flash ở đây
                }
            }
            lastAttackTime = System.currentTimeMillis();
        }
    }

    @Override
    public void hit(int dmg) {
        if (getWorld() != null && isLiving()) {
            hp -= dmg;
            
            // Khi bị đánh, bắt đầu đấm trả
            this.isCounterAttacking = true;
            // Mỗi lần bị hit sẽ gia hạn thêm 1.2 giây đấm trả 
            // (để đảm bảo đấm được ít nhất 2-3 phát)
            this.counterAttackEndTime = System.currentTimeMillis() + 1200; 

            if (hp <= 0) {
                getWorld().removeObject(this);
            }
        }
    }
}