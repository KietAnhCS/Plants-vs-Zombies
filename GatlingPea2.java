import greenfoot.*;
import java.util.List;

public class GatlingPea2 extends Plant {
    private static final PlantType TYPE = PlantType.GATLING_PEA_2;
    private GreenfootImage[] idle, shoot;
    private boolean adjusted = false;
    private long lastAttackTime = System.currentTimeMillis();
    private PlayScene cachedPlayScene;
    
    private int burstCount = 0;
    private long lastBurstTime = 0;
    private static final int BURST_INTERVAL = 80; // Tốc độ ra đạn trong loạt (nhanh hơn lv1)

    public GatlingPea2() {
        setMaxHp(TYPE.hp);
        setHp(TYPE.hp);
        setDamage(TYPE.damage);
        setCost(TYPE.cost);
        
        // Sử dụng ảnh gốc, không scale bằng code để giữ độ nét
        idle = importSprites(PlantAssets.GATLING_PEA, 19);
        shoot = importSprites(PlantAssets.GATLING_PEA, 19);
        
        if (idle != null && idle.length > 0) setImage(idle[0]);
    }

    @Override
    public void addedToWorld(World world) {
        super.addedToWorld(world);
        if (world instanceof PlayScene) cachedPlayScene = (PlayScene) world;
        world.addObject(new HealthBar(this, 50), getX(), getY());
    }

    @Override
    public void hit(int dmg) {
        if (getWorld() == null || !isLiving()) return;
        
        // Fix lỗi hitFlash truyền String như yêu cầu lớp cha
        hitFlash(PlantAssets.GATLING_PEA);
        
        setHp(getHp() - dmg);
        if (getHp() <= 0) onDeath();
    }

    @Override
    public void update() {
        if (getWorld() == null) return;
        if (!adjusted) {
            setLocation(getX(), getY() - 15);
            adjusted = true;
        }
        handleCombat();
    }

    private void handleCombat() {
        if (getState() == PlantState.MERGING) return;

        boolean hasTarget = checkZombieInRow();
        
        // Vẫn tiếp tục bắn nốt loạt đạn nếu zombie chết giữa chừng
        if (hasTarget || burstCount > 0) {
            setState(PlantState.SHOOTING);
            animate(shoot, 60, false);
            executeShoot();
        } else {
            setState(PlantState.IDLE);
            animate(idle, 60, true);
        }
    }

    private boolean checkZombieInRow() {
        int myRow = getYPos();
        if (myRow == -1 || cachedPlayScene == null || cachedPlayScene.level == null) return false;
        List<Zombie> rowZombies = cachedPlayScene.level.zombieRow.get(myRow);
        return rowZombies.stream().anyMatch(z ->
            z.getWorld() != null &&
            z.getX() > getX() &&
            z.getX() <= cachedPlayScene.getWidth() + 10
        );
    }

    private void executeShoot() {
        long currentTime = System.currentTimeMillis();

        // Kiểm tra xem đã đến lúc bắt đầu loạt bắn mới chưa
        if (burstCount == 0) {
            if (currentTime - lastAttackTime >= TYPE.shootDelay) {
                burstCount = 4; // Loạt 4 viên
                lastAttackTime = currentTime;
            }
        }

        // Thực hiện bắn từng viên FirePea trong loạt
        if (burstCount > 0 && currentTime - lastBurstTime >= BURST_INTERVAL) {
            AudioManager.getInstance().playSound(80, false, PlantAssets.SOUND_THROW);
            // Level 2 bắn FirePea (Đạn lửa)
            getWorld().addObject(new FirePea(getYPos()), getX() + 25, getY() - 17);
            
            burstCount--;
            lastBurstTime = currentTime;
        }
    }
}