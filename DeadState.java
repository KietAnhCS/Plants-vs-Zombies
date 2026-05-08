import greenfoot.*;

public class DeadState implements IZombieState 
{
    private Zombie zombie;
    private boolean hasFinishedDeathAnim = false;

    public DeadState(Zombie zombie) {
        this.zombie = zombie;
    }

    @Override
    public void enter() {
        // Reset frame để bắt đầu hoạt ảnh chết từ đầu
        zombie.setFrame(0);
        
        // Dừng các hiệu ứng âm thanh liên quan đến zombie này
        // Ví dụ: AudioManager.getInstance().stopSound(zombie.getEatingSound());
        
        // Cập nhật trạng thái sống trong logic game
        if (zombie.getWorld() instanceof PlayScene) {
            PlayScene scene = (PlayScene) zombie.getWorld();
            // Xóa zombie khỏi danh sách hàng để cây không bắn vào xác chết
            if (scene.level != null && scene.level.zombieRow != null) {
                scene.level.zombieRow.get(zombie.getYPos()).remove(zombie);
            }
        }
    }

    @Override
    public void update() {
        if (hasFinishedDeathAnim) return;

        // animate() trả về true khi đã chạy hết một vòng hoạt ảnh (loop = false)
        // Duration 200ms mỗi frame hoặc tùy chỉnh theo config của zombie
        if (zombie.animate(getAnimation(), 200, false)) {
            hasFinishedDeathAnim = true;
            cleanup();
        }
    }

    private void cleanup() {
        World world = zombie.getWorld();
        if (world != null) {
            world.removeObject(zombie);
        }
    }

    @Override
    public void exit() {
        // Trạng thái cuối cùng, không cần xử lý exit
    }

    @Override
    public GreenfootImage[] getAnimation() {
        // Lấy bộ ảnh chết từ phương thức đã có trong class Zombie của ông
        return zombie.getDeadSprites();
    }
}