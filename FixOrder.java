import greenfoot.*;

public class FixOrder extends Actor
{
    private final WaveManager level;
    private final long delayTimeMillis;
    private final long startTimeNano;

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

        
        long elapsedMillis = (System.nanoTime() - startTimeNano) / 1_000_000;

        if (elapsedMillis >= delayTimeMillis) {
            executeTask();
        }
    }

    private void executeTask() {
     
        if (level != null) {
            level.fixOrder();
        }
       
        getWorld().removeObject(this);
    }
}