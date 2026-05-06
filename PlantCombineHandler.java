import greenfoot.*;
import java.util.List;
import java.util.ArrayList;

public class PlantCombineHandler {

    public static void checkAndCombine(PlayScene scene, Plant placedPlant) {
        if (placedPlant == null || scene == null || placedPlant.getWorld() == null) return;

        if (UpgradeManager.getUpgradeResult(placedPlant) == null) {
            return;
        }

        List<Plant> plants = scene.getObjects(Plant.class);
        List<Plant> matches = new ArrayList<>();

        for (Plant p : plants) {
            if (p.getClass() == placedPlant.getClass() && 
                p.name.equals(placedPlant.name) && 
                !p.isMerging && !p.isDragging) {
                matches.add(p);
            }
        }

        if (matches.size() >= 3) {
            Plant target = placedPlant;
            int count = 0;
            
            for (Plant p : matches) {
                if (p != target && count < 2) {
                    p.setMergingTarget(target);
                    count++;
                }
            }
        }
    }
}