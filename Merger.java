import greenfoot.*;

class Merger {

    private Actor  mover;
    private Actor  target;
    private double speed = 20.0;

    public Merger(Actor mover, Actor target) {
        this.mover  = mover;
        this.target = target;
    }

    public boolean update() {
        if (mover == null || mover.getWorld() == null ||
            target == null || target.getWorld() == null) return true;

        int    dx       = target.getX() - mover.getX();
        int    dy       = target.getY() - mover.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance < speed) {
            mover.setLocation(target.getX(), target.getY());
            return true;
        }

        double angle = Math.atan2(dy, dx);
        mover.setLocation(
            mover.getX() + (int)(Math.cos(angle) * speed),
            mover.getY() + (int)(Math.sin(angle) * speed)
        );
        return false;
    }
}