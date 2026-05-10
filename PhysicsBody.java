import greenfoot.*;

public abstract class PhysicsBody extends Actor {
    private double exactX;
    private double exactY;

    @Override
    public void addedToWorld(World world) {
        exactX = getX();
        exactY = getY();
    }

    @Override
    public void move(int distance) {
        move((double) distance);
    }

    public void move(double distance) {
        double radians = Math.toRadians(getRotation());
        setLocation(exactX + Math.cos(radians) * distance,
                    exactY + Math.sin(radians) * distance);
    }

    @Override
    public void setLocation(int x, int y) {
        exactX = x;
        exactY = y;
        super.setLocation(x, y);
    }

    public void setLocation(double x, double y) {
        exactX = x;
        exactY = y;
        super.setLocation((int) Math.round(x), (int) Math.round(y));
    }

    public double getExactX() { return exactX; }
    public double getExactY() { return exactY; }
}