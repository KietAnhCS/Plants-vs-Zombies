import greenfoot.*;

/**
 * PlantCombatHandler tách biệt logic xử lý sát thương và cái chết 
 * giúp lớp Plant gọn gàng hơn.
 */
public class PlantCombatHandler {
    private Plant plant;

    public PlantCombatHandler(Plant plant) {
        this.plant = plant;
    }

    /**
     * Xử lý nhận sát thương cho cây.
     */
    public void takeDamage(int dmg) {
        if (plant == null || plant.getWorld() == null) return;
        
        // Cây đang kéo hoặc đang gộp thì thường không nhận dame (tùy logic game của bạn)
        if (plant.isDragging || plant.isMerging) return;

        plant.hp -= dmg;
        if (plant.hp <= 0) {
            die();
        }
    }

    /**
     * Xử lý logic khi cây chết: Phát âm thanh, xóa khỏi Grid và xóa khỏi World.
     */
    public void die() {
        World world = plant.getWorld();
        if (world == null) return;

        // 1. Hiệu ứng âm thanh
        AudioManager.playSound(80, false, "gulp.mp3");

        // 2. Cập nhật GridManager để ô đó trống cho cây khác
        if (plant.playScene != null && plant.playScene.GridManager != null) {
            plant.playScene.GridManager.removePlant(plant.getXPos(), plant.getYPos());
        }

        // 3. Xóa đối tượng khỏi thế giới Greenfoot
        world.removeObject(plant);
    }
}