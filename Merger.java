import greenfoot.*;

public class Merger {
    private Plant source;
    private Plant target;
    private double speed = 15.0;
    private boolean arrived = false;

    public Merger(Plant source, Plant target) {
        this.source = source;
        this.target = target;
    }

    public boolean update() {
        if (arrived) return true;
        if (target == null || target.getWorld() == null || !target.isLiving()) return true;

        if (source == null || source.getWorld() == null) {
            handleArrival();
            return true;
        }

        double dx = target.getExactX() - source.getExactX();
        double dy = target.getExactY() - source.getExactY();
        double dist = Math.sqrt(dx * dx + dy * dy);

        if (dist <= speed) {
            handleArrival();
            return true;
        }

        source.setLocation(source.getExactX() + (dx / dist) * speed, 
                           source.getExactY() + (dy / dist) * speed);
        return false;
    }

    private void handleArrival() {
        if (arrived) return;
        arrived = true;
        if (source != null && source.getWorld() != null) {
            source.getWorld().removeObject(source);
        }
        if (target != null) {
            target.notifyMergerArrived();
        }
    }
}