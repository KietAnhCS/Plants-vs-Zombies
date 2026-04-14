import greenfoot.*;
import java.util.ArrayList;
import java.util.List;

public class Projectile extends animatedObject {
    private int speed;
    private boolean hit = false;
    private boolean foundTarget = false;
    private Zombie hitZombie;
    private int frameCount;
    private int yPos; // Đây là CHỈ SỐ HÀNG (0, 1, 2, 3, 4)
    private int damage;
    private GreenfootImage[] image;

    public Projectile(String name, int frameCount, int yPos, int dmg, int speed) {
        this.frameCount = frameCount;
        this.image = importSprites(name, frameCount);
        this.yPos = yPos;
        this.damage = dmg;
        this.speed = speed;
        if (image != null && image.length > 0) {
            setImage(image[0]);
        }
    }

    public void act() {
        // 1. Kiểm tra an toàn thế giới
        MyWorld world = (MyWorld) getWorld();
        if (world == null) return;

        // 2. Xử lý khi đạn đã trúng (hiệu ứng nổ)
        if (hit) {
            handleHitAnimation(world);
            return;
        }

        // 3. Di chuyển
        move(speed);

        // 4. Kiểm tra biên hoặc biến mất
        if (isAtEdge()) {
            world.removeObject(this);
            return;
        }

        // 5. Kiểm tra va chạm với Zombie
        checkCollision(world);
    }

    private void handleHitAnimation(MyWorld world) {
        // Nếu frame chạy hết mảng ảnh thì xóa đạn
        if (frame >= frameCount - 1) {
            world.removeObject(this);
        } else {
            animate(image, 150, false);
        }
    }

    private void checkCollision(MyWorld world) {
        // Kiểm tra tính hợp lệ của hệ thống hàng lối
        if (world.level == null || world.level.zombieRow == null) return;

        // CHỐT CHẶN: Nếu yPos > 10 (chắc chắn là tọa độ Y chứ không phải hàng) 
        // thì ta không check va chạm để tránh crash IndexOutOfBounds
        if (yPos < 0 || yPos >= world.level.zombieRow.size()) {
            return; 
        }

        List<Zombie> row = world.level.zombieRow.get(yPos);
        if (row == null || row.isEmpty()) return;

        // Duyệt danh sách zombie (dùng bản sao để tránh lỗi ConcurrentModification)
        for (Zombie z : new ArrayList<>(row)) {
            if (getWorld() == null) return;
            if (z == null || z.getWorld() == null) continue;

            // Kiểm tra va chạm theo khoảng cách X
            if (Math.abs(z.getX() - getX()) < 30) {
                if (!hit) {
                    z.hit(damage);
                    this.hit = true;
                    this.frame = 0; // Bắt đầu chạy animation nổ
                }
                break;
            }
        }
    }

    // Getter cực kỳ quan trọng để Torchwood gọi
    public int getYPos() {
        return this.yPos;
    }
}