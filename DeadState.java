import greenfoot.*;

public class DeadState implements IZombieState {
    private Zombie zombie;
    private boolean hasFinishedDeathAnim = false;

    public DeadState(Zombie zombie) {
        this.zombie = zombie;
    }

    @Override
    public void enter() {
        // Reset frame để bắt đầu diễn hoạt cảnh ngã xuống từ đầu
        zombie.setFrame(0);
        
        // Dọn dẹp các mục tiêu đang ăn để không làm lỗi Plant
        zombie.target = null;
        zombie.eating = false;
        
        // Gửi sự kiện zombie chết (nếu cần bus sự kiện)
        // zombie.removeFromRow(); // Thường lớp Zombie cha sẽ lo việc này
    }

    @Override
    public void update() {
        if (hasFinishedDeathAnim || zombie.getWorld() == null) return;

        // Chạy animation ngã (không lặp lại - loop = false)
        // getAnimation() sẽ trả về mảng 'fall' của zombie
        if (zombie.animate(getAnimation(), 250, false)) {
            hasFinishedDeathAnim = true;
            cleanup();
        }
    }

    private void cleanup() {
        World world = zombie.getWorld();
        if (world != null) {
            // Thêm hiệu ứng âm thanh ngã cuối cùng nếu muốn
            // AudioManager.playSound(80, false, "zombie_falling_1.mp3");
            world.removeObject(zombie);
        }
    }

    @Override
    public void exit() {}

    @Override
    public GreenfootImage[] getAnimation() {
        return zombie.getDeadSprites(); // Trả về mảng ảnh 'fall'
    }
}