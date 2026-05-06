import greenfoot.*;

public class Merger {

    private Actor mover;
    private Actor target;
    private double speed = 20.0;

    public Merger(Actor mover, Actor target) {
        this.mover = mover;
        this.target = target;
    }

    public boolean update() {
        if (mover == null || mover.getWorld() == null || target == null || target.getWorld() == null) {
            return true;
        }

        int targetX = target.getX();
        int targetY = target.getY();
        int moverX = mover.getX();
        int moverY = mover.getY();

        int dx = targetX - moverX;
        int dy = targetY - moverY;
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance < speed) {
            mover.setLocation(targetX, targetY);
            return true;
        }

        double angle = Math.atan2(dy, dx);
        int nextX = moverX + (int) (Math.cos(angle) * speed);
        int nextY = moverY + (int) (Math.sin(angle) * speed);

        mover.setLocation(nextX, nextY);
        return false;
    }
}