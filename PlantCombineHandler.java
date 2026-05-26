import greenfoot.*;
import java.util.*;

public class PlantCombineHandler {
    public static void checkAndCombine(PlayScene scene, Plant target) {
        if (target == null || scene == null || target.getWorld() == null) return;

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

        if (matches.size() < 3) return;

        boolean allInReserve = true;
        for (Plant p : matches) {
            if (p.getYPos() < 5) {
                allInReserve = false;
                break;
            }
        }

        WaveManager wm = scene.getWaveManager();
        boolean isWaitingPhase = false;
        if (wm != null) {
            BattlePhase phase = wm.getBattlePhase();
            isWaitingPhase = (phase == BattlePhase.PREP || phase == BattlePhase.COUNTDOWN);
        }

        if (allInReserve || isWaitingPhase) {
            List<Plant> sources = new ArrayList<>();
            for (Plant p : matches) {
                if (p != target && sources.size() < 2) sources.add(p);
            }

            if (sources.size() == 2) {
                Plant s1 = sources.get(0);
                Plant s2 = sources.get(1);

                target.setState(PlantState.MERGING);
                s1.setMergingTarget(target, s2);
                s2.setMergingTarget(target, s1);

                scene.addMergeAnimation(s1, s2, target);
            }
        }
    }
}