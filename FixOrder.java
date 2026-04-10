import greenfoot.*;

/**
 * DelayedOrderFixer - Một lớp tiện ích giúp thực hiện việc sắp xếp lại 
 * thứ tự các đối tượng sau một khoảng thời gian chờ nhất định.
 */
public class FixOrder extends Actor
{
    private final WaveManager level;
    private final long delayTimeMillis;
    private final long startTimeNano;

    /**
     * @param level Đối tượng quản lý màn chơi cần gọi hàm fixOrder.
     * @param delayTimeMillis Thời gian chờ tính bằng mili giây (ms).
     */
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