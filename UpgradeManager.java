import greenfoot.*;

public class UpgradeManager {

    public static boolean canUpgrade(Plant mover, Plant target) {
        if (mover == null || target == null) return false;
        return mover.getClass() == target.getClass() && mover.name.equals(target.name);
    }

    public static Plant getUpgradeResult(Plant p) {
        if (p instanceof Peashooter) return new Repeater();
        if (p instanceof Repeater) return new GatlingPea();
        if (p instanceof GatlingPea) return new GatlingPea2();
        if (p instanceof Cactus) return new Cactus2();
        if (p instanceof Cactus2) return new Cactus3();
        if (p instanceof BonkChoy) return new BonkChoy2();
        if (p instanceof BonkChoy2) return new BonkChoy3();
        return null;
    }

    public static void handleMergeLogic(Plant mover, Merger merger) {
        if (merger != null && merger.update()) {
            finalizeMerge(mover);
        }
    }

    private static void finalizeMerge(Plant mover) {
        World world = mover.getWorld();
        Plant target = mover.targetPlant;
        if (world == null || target == null) return;

        world.removeObject(mover); // Xóa con vừa bay tới nơi

        // KIỂM TRA: Còn con nào khác cũng đang bay tới target này không?
        java.util.List<Plant> plants = world.getObjects(Plant.class);
        boolean stillWaiting = false;
        for (Plant p : plants) {
            if (p.isMerging && p.targetPlant == target) {
                stillWaiting = true;
                break;
            }
        }

        // Nếu KHÔNG còn con nào đang bay tới nữa (đã đủ 3 con tụ lại)
        if (!stillWaiting) {
            executeUpgrade(target, (PlayScene) world);
        }
    }

    private static void executeUpgrade(Plant target, PlayScene scene) {
        int gx = target.getXPos();
        int gy = target.getYPos();
        int tx = target.getX();
        int ty = target.getY();

        Plant upgraded = getUpgradeResult(target);

        // Xóa con đích cuối cùng
        scene.removeObject(target);
        scene.GridManager.removePlant(gx, gy);

        if (upgraded != null) {
            scene.addObject(upgraded, tx, ty);
            scene.GridManager.Board[gy][gx] = upgraded;
            upgraded.setGridPosition(gx, gy);
            
            // Đồng bộ lại tên cấp độ cho con mới để gộp tiếp lần sau
            syncLevelName(upgraded);
            
            // Kiểm tra gộp tiếp (chuỗi combo)
            PlantCombineHandler.checkAndCombine(scene, upgraded);
        }
    }

    private static void syncLevelName(Plant p) {
        if (p instanceof Repeater || p instanceof Cactus2 || p instanceof BonkChoy2) p.name = "Level2";
        else if (p instanceof GatlingPea || p instanceof Cactus3 || p instanceof BonkChoy3) p.name = "Level3";
        else p.name = "Level1";
    }
}