import greenfoot.*;  
public class FallingObject extends SpriteAnimator
{
    public double vSpeed = 0;
    public double hSpeed = 0;
    public double acceleration = 1;
    public long fallTime;
    public long elapsedTime = 0;
    public int rotate;
    private long fallLastFrame = System.nanoTime();

    public FallingObject(double vSpeed, double acceleration, double hSpeed, int rotate, long time) {
        this.vSpeed = vSpeed;
        this.acceleration = acceleration;
        this.rotate = rotate;
        this.hSpeed = hSpeed;
        this.fallTime = time; 
        fallLastFrame = System.nanoTime();
    }

    public void act() {
        update();
    }
    
    public void update() {
        long now = System.nanoTime();
        long dt = (now - fallLastFrame) / 1000000;
        fallLastFrame = now;
        elapsedTime += dt;


        if (elapsedTime < fallTime) {
            double x = getExactX() + hSpeed;
            double y = getExactY() + vSpeed;
            setLocation(x, y);
            turn(rotate);
            vSpeed = vSpeed + acceleration;
        } else {
            checkDeath();
        }
    }

    public void checkDeath() {
        if (getImage().getTransparency() > 0) {
            if (getImage().getTransparency()-3 <= 0) {
                getImage().setTransparency(0);
            } else {
                getImage().setTransparency(getImage().getTransparency()-3);
            }
        } else {
            getWorld().removeObject(this);
            return;
        }
    }
}