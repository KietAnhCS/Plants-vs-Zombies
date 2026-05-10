import greenfoot.*;

public class Merger {
    private Plant source;
    private Plant target;
    private double speed = 15.0;
    private boolean arrived = false;
    private boolean finalized = false;

    private static int arrivedCount = 0;
    private static Plant lastTarget = null;

    public Merger(Plant source, Plant target, UpgradeManager upgradeManager) {
        this.source = source;
        this.target = target;
        if (target != lastTarget) {
            lastTarget = target;
            arrivedCount = 0;
        }
    }

    public boolean update() {
        if (arrived) return finalized;
        if (target == null || target.getWorld() == null) return true;

        if (source == null || source.getWorld() == null) {
            markArrived();
            return finalized;
        }

        double tx = target.getExactX();
        double ty = target.getExactY();
        double sx = source.getExactX();
        double sy = source.getExactY();
        double dx = tx - sx;
        double dy = ty - sy;
        double dist = Math.sqrt(dx * dx + dy * dy);

        if (dist <= speed) {
            if (source.getWorld() != null) source.getWorld().removeObject(source);
            markArrived();
            return finalized;
        }

        source.setLocation(sx + (dx / dist) * speed, sy + (dy / dist) * speed);
        return false;
    }

    private void markArrived() {
        if (arrived) return;
        arrived = true;
        arrivedCount++;
        if (arrivedCount >= 2) {
            finalized = true;
            arrivedCount = 0;
            lastTarget = null;
            if (target != null && target.getWorld() != null) {
                finalizeEffect((PlayScene) target.getWorld());
            }
        }
    }

    private void finalizeEffect(PlayScene scene) {
        Plant upgraded = UpgradeManager.getUpgradeResult(target);
        if (upgraded == null) return;

        int gx = target.getXPos();
        int gy = target.getYPos();
        int px = target.getX();
        int py = target.getY();

        if (scene.GridManager != null) scene.GridManager.removePlant(gx, gy);
        scene.removeObject(target);
        scene.addObject(upgraded, px, py);

        if (scene.getUpgradeManager() != null) {
            upgraded.setEventBus(scene.getUpgradeManager().getEventBus());
        }

        upgraded.setGridPosition(gx, gy);
        upgraded.setState(PlantState.IDLE);
        if (scene.GridManager != null) scene.GridManager.placePlant(gx, gy, upgraded);
        scene.addObject(new Dirt(), px, py + 30);
    }

    public void cancel() {
        if (source != null && source.getWorld() != null) {
            source.setState(PlantState.IDLE);
            source.syncGridPosition();
        }
        if (target != null && target.getWorld() != null) {
            target.setState(PlantState.IDLE);
        }
    }
}