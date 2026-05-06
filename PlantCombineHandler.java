import greenfoot.*;
import java.util.List;
import java.util.ArrayList;

public class PlantCombineHandler {

    public static void checkAndCombine(PlayScene scene, Plant placedPlant) {
        if (placedPlant == null || scene == null || placedPlant.getWorld() == null) return;

        List<Plant> plants = scene.getObjects(Plant.class);
        List<Plant> matches = new ArrayList<>();

        // Chỉ tìm những con cùng loại, cùng tên và ĐANG ĐỨNG YÊN (không dragging, không merging)
        for (Plant p : plants) {
            if (p.getClass() == placedPlant.getClass() && 
                p.name.equals(placedPlant.name) && 
                !p.isMerging && !p.isDragging) {
                matches.add(p);
            }
        }

        // BẮT BUỘC ĐỦ 3 CON (bao gồm cả con vừa đặt)
        if (matches.size() >= 3) {
            // Con vừa đặt (placedPlant) sẽ đứng yên làm đích
            Plant target = placedPlant;
            int count = 0;
            
            for (Plant p : matches) {
                if (p != target && count < 2) {
                    p.setMergingTarget(target); // Cho 2 con còn lại bay vào
                    count++;
                }
            }
        }
    }
}