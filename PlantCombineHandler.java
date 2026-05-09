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
                if (p != target && sources.size() < 2) sources.add(p);
            }
            if (sources.size() == 2) {
                for (Plant s : sources) s.setMergingTarget(target);
                target.setState(PlantState.MERGING);
            }
        }
    }
}