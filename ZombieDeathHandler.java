import greenfoot.*;

/**
 * ZombieDeathHandler: Xử lý logic chi tiết khi một Zombie chết.
 * Triển khai các hiệu ứng âm thanh, sự kiện và sinh ra các thực thể phụ.
 */
public class ZombieDeathHandler implements IDeathHandler 
{
    // - IAudioService audio
    private IAudioService audio;
    
    // - ZombieEventBus eventBus
    private ZombieEventBus eventBus;

    /**
     * Constructor khởi tạo handler với các dịch vụ cần thiết.
     */
    public ZombieDeathHandler(IAudioService audio, ZombieEventBus eventBus) {
        this.audio = audio;
        this.eventBus = eventBus;
    }

    /**
     * + handleDeath(Zombie) : void
     * Thực hiện các hành động khi Zombie chết.
     */
    @Override
    public void handleDeath(Zombie zombie) {
        // 1. Phát âm thanh khi zombie chết (ví dụ: tiếng rên hoặc tiếng ngã)
        if (audio != null) {
            audio.playSound(0, false, "zombie_die"); 
        }

        // 2. Thông báo cho EventBus để các hệ thống khác (điểm số, nhiệm vụ) cập nhật
        if (eventBus != null) {
            eventBus.publishDeath(zombie);
        }

        // 3. Logic sinh ra các thực thể phụ (spawns) theo UML:
        // Ví dụ: Sinh ra một cái đầu (Head) rơi xuống đất
        World world = zombie.getWorld();
        if (world != null) {
            // Spawns Head
            world.addObject(new Head(), zombie.getX(), zombie.getY());
            
            // Spawns FallingZombie (phần thân đang ngã) nếu cần thiết
            // world.addObject(new FallingZombie(zombie.getDeadSprites()), zombie.getX(), zombie.getY());
        }
    }
}