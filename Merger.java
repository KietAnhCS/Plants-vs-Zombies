import greenfoot.*;

public class Merger {
    private Plant source;
    private Plant target;
    private double speed = 15.0;

    public Merger(Plant source, Plant target, UpgradeManager upgradeManager) {
        this.source = source;
        this.target = target;
    }

    public boolean update() {
        if (source == null || source.getWorld() == null || target == null || target.getWorld() == null) {
            return true;
        }

        double tx = target.getX();
        double ty = target.getY();
        double sx = source.getX();
        double sy = source.getY();

        double dx = tx - sx;
        double dy = ty - sy;
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance <= speed) {
            source.setLocation((int)tx, (int)ty);
            finalizeEffect();
            return true;
        }

        double vx = (dx / distance) * speed;
        double vy = (dy / distance) * speed;
        
        source.setLocation((int)(sx + vx), (int)(sy + vy));
        
        return false;
    }

    private void finalizeEffect() {
        if (source != null && source.getWorld() != null) {
            source.getWorld().removeObject(source);
        }
    }

    public void startMerge() {
        if (source != null) source.setState(PlantState.MERGING);
        if (target != null) target.setState(PlantState.MERGING);
    }

    public void cancel() {
        if (source != null && source.getWorld() != null) {
            source.setState(PlantState.IDLE);
            source.syncGridPosition();
            source.getImage().setTransparency(255);
        }
        if (target != null && target.getWorld() != null) {
            target.setState(PlantState.IDLE);
        }
    }
}