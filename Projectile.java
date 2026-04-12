import greenfoot.*;

public class Projectile extends animatedObject {
    private int speed;
    private boolean hit = false;
    private boolean foundTarget = false;
    private Zombie hitZombie;
    private int frameCount;
    private int yPos;
    private int damage;
    private GreenfootImage[] image;

    public Projectile(String name, int frameCount, int yPos, int dmg, int speed) {
        this.frameCount = frameCount;
        this.image = importSprites(name, frameCount);
        this.yPos = yPos;
        this.damage = dmg;
        this.speed = speed;
        setImage(image[0]);
    }

    public void act() {
        // CHỐT CHẶN: Luôn kiểm tra đầu tiên
        if (getWorld() == null) return;

        MyWorld world = (MyWorld) getWorld();

        // 1. Nếu đang animate hit xong → xóa đạn
        if (hit) {
            if (frame >= frameCount) {
                world.removeObject(this);
                return;
            }
            animate(image, 150, false);
            return; // Khi đang hit, không di chuyển hay check va chạm nữa
        }

        // 2. Di chuyển bình thường
        move(speed);

        // 3. Kiểm tra ra ngoài biên
        if (isAtEdge() || getWorld() == null) {
            if (getWorld() != null) world.removeObject(this);
            return;
        }

        // 4. Kiểm tra va chạm với Zombie trong hàng tương ứng
        if (world.level == null || world.level.zombieRow == null) return;
        
        java.util.List<Zombie> row = world.level.zombieRow.get(yPos);
        if (row == null) return;

        // Dùng bản sao để tránh ConcurrentModificationException
        for (Zombie z : new java.util.ArrayList<>(row)) {
            if (getWorld() == null) return; // Kiểm tra lại sau mỗi bước quan trọng

            // Bỏ qua zombie đã chết / bị xóa
            if (z == null || z.getWorld() == null) continue;

            if (Math.abs(z.getX() - getX()) < 30) {
                if (!foundTarget) {
                    hitZombie = z;
                    foundTarget = true;
                }
                
                // Chỉ gây sát thương 1 lần
                if (!hit) {
                    hitZombie.hit(damage);
                    hit = true;
                }
                break; // Đã tìm thấy mục tiêu, không cần duyệt tiếp
            }
        }
    }
}