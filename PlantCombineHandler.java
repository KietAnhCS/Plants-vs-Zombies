import greenfoot.*;
import java.util.List;
import java.util.ArrayList;

public class PlantCombineHandler {

    public static void checkAndCombine(PlayScene scene, Plant target) {
        if (target == null || scene == null || target.getWorld() == null) return;
        if (target.getState() == PlantState.MERGING || target.getState() == PlantState.DYING) return;
        if (!target.isLiving() || target.isDragging) return;
        
        if (UpgradeManager.getUpgradeResult(target) == null) return;

        List<Plant> allPlants = scene.getObjects(Plant.class);
        List<Plant> matches = new ArrayList<>();

        for (Plant p : allPlants) {
            if (p != null && p.getWorld() != null 
                && p.getClass() == target.getClass() 
                && p.getState() == PlantState.IDLE 
                && p.isLiving() && !p.isDragging) {
                matches.add(p);
            }
        }

        if (matches.size() >= 3) {
            List<Plant> sources = new ArrayList<>();
            for (Plant p : matches) {
                if (p != target && sources.size() < 2) {
                    sources.add(p);
                }
            }
            
            if (sources.size() == 2) {
                executeImmediateMerge(scene, target, sources);
            }
        }
    }

    private static void executeImmediateMerge(PlayScene scene, Plant target, List<Plant> sources) {
        // 1. Lưu tọa độ chuẩn TRƯỚC khi xóa bất cứ thứ gì
        int gx = target.getXPos();
        int gy = target.getYPos();
        int px = target.getX();
        int py = target.getY();

        // 2. Lấy cây nâng cấp mới
        Plant upgraded = UpgradeManager.getUpgradeResult(target);
        if (upgraded == null) return;

        // 3. Giải phóng GridManager cho cả 3 ô cũ
        if (scene.GridManager != null) {
            scene.GridManager.removePlant(gx, gy);
            for (Plant s : sources) {
                scene.GridManager.removePlant(s.getXPos(), s.getYPos());
            }
        }

        // 4. Xóa 3 cây cũ khỏi World
        for (Plant s : sources) {
            scene.removeObject(s);
        }
        scene.removeObject(target);

        // 5. Thêm cây mới vào đúng vị trí px, py
        scene.addObject(upgraded, px, py);
        
        // 6. Gán EventBus và đồng bộ Grid cho cây mới
        if (scene.getUpgradeManager() != null) {
            // Đây là dòng bạn nhắc: Gán EventBus từ Manager vào cây mới
            upgraded.setEventBus(scene.getUpgradeManager().getEventBus());
        }
        
        upgraded.setGridPosition(gx, gy);
        upgraded.setState(PlantState.IDLE);
        
        if (scene.GridManager != null) {
            scene.GridManager.placePlant(gx, gy, upgraded);
        }

        // 7. Hiệu ứng phụ (Dirt)
        scene.addObject(new Dirt(), px, py + 30);
    }
}