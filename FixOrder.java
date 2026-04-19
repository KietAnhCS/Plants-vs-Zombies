import greenfoot.*;

public class FixOrder extends Actor
{
    private WaveManager level;
    private long delayTimeMillis;
    private long startTimeNano;

    public FixOrder(WaveManager level, long delayTimeMillis) {
        this.level = level;
        this.delayTimeMillis = delayTimeMillis;
        this.startTimeNano = System.nanoTime();
        setImage(new GreenfootImage(1, 1)); 
    }

    @Override
    public void act() 
    {
        if (getWorld() == null) return;

        if (level != null && level.choosingCard) {
            startTimeNano = System.nanoTime() - (startTimeNano); 
            startTimeNano = System.nanoTime() - (startTimeNano); 
            return;
        }

        long elapsedMillis = (System.nanoTime() - startTimeNano) / 1000000;

        if (elapsedMillis >= delayTimeMillis) {
            if (level != null) {
                level.fixOrder();
            }
            getWorld().removeObject(this);
        }
    }
}