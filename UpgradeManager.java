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

        world.removeObject(mover);

        java.util.List<Plant> plants = world.getObjects(Plant.class);
        boolean stillWaiting = false;
        for (Plant p : plants) {
            if (p.isMerging && p.targetPlant == target) {
                stillWaiting = true;
                break;
            }
        }

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

        scene.removeObject(target);
        scene.GridManager.removePlant(gx, gy);

        if (upgraded != null) {
            scene.addObject(upgraded, tx, ty);
            scene.GridManager.Board[gy][gx] = upgraded;
            upgraded.setGridPosition(gx, gy);
            
            PlantCombineHandler.checkAndCombine(scene, upgraded);
        }
    }
}