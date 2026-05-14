import greenfoot.*;
import java.util.*;

public class PlantCombineHandler {
    public static void checkAndCombine(PlayScene scene, Plant target) {
        if (target == null || scene == null || target.getWorld() == null) return;

        // 1. Lấy danh sách tất cả các con cùng loại đang IDLE
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

        // 2. Nếu không đủ 3 con thì nghỉ
        if (matches.size() < 3) return;

        // 3. Kiểm tra điều kiện vị trí và thời gian
        boolean allInReserve = true; 
        for (Plant p : matches) {
            if (p.getYPos() < 5) { // Có ít nhất 1 con nằm trên sân chiến đấu
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
                target.setState(PlantState.MERGING);
                for (Plant s : sources) {
                    s.setMergingTarget(target);
                }
            }
        }
    }
}