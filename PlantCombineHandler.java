import greenfoot.*;
import java.util.List;
import java.util.ArrayList;

public class PlantCombineHandler {
    public static void checkAndCombine(PlayScene scene, Plant target) {
        if (target == null || scene == null || target.getWorld() == null) return;
        
        // Chỉ chặn nếu cây đang trong quá trình gộp khác hoặc đang biến mất hoàn toàn
        if (target.getState() == PlantState.MERGING || target.getState() == PlantState.DYING) return;
        
        // Vẫn giữ kiểm tra sống và không bị kéo
        if (!target.isLiving() || target.isDragging) return;
        if (UpgradeManager.getUpgradeResult(target) == null) return;

        List<Plant> allPlants = scene.getObjects(Plant.class);
        List<Plant> matches = new ArrayList<>();
        
        for (Plant p : allPlants) {
            if (p != null && p.getWorld() != null
                && p.getClass() == target.getClass()
                // THAY ĐỔI Ở ĐÂY: 
                // Loại bỏ p.getState() == PlantState.IDLE
                // Chỉ cần cây không đang gộp, không đang chết và không bị kéo là được
                && p.getState() != PlantState.MERGING 
                && p.getState() != PlantState.DYING
                && p.isLiving() && !p.isDragging) {
                
                matches.add(p);
            }
        }

        if (matches.size() >= 3) {
            List<Plant> sources = new ArrayList<>();
            for (Plant p : matches) {
                if (p != target && sources.size() < 2) sources.add(p);
            }
            if (sources.size() == 2) {
                for (Plant s : sources) {
                    // Chuyển trạng thái sang MERGING ngay để dừng các hành động bắn/bị ăn
                    s.setState(PlantState.MERGING); 
                    s.setMergingTarget(target);
                }
                target.setState(PlantState.MERGING);
            }
        }
    }
}