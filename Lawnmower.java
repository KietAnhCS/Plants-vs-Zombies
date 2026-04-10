import greenfoot.*;
import java.util.List;

/**
 * Lớp Lawnmower - Tối ưu hóa va chạm và xử lý an toàn bộ nhớ.
 */
public class Lawnmower extends Actor
{
    private int speed = 0;
    private boolean isMoving = false;

    public Lawnmower()
    {
        setImage("lawn_mower1.png"); 
    }

    public void act()
    {
        // CHỐT CHẶN 1: Nếu máy không còn trong World, không chạy bất cứ logic nào
        if (getWorld() == null) return;

        if (isMoving) {
            handleMovement();
        } else {
            checkActivation();
        }

        // CHỐT CHẶN 2: Kiểm tra biên và tự xóa chính nó
        // Lưu ý: Phải để ở cuối act()
        handleBoundaries();
    }

    /**
     * Di chuyển máy và tiêu diệt tất cả Zombie chạm phải
     */
    private void handleMovement()
    {
        if (getWorld() == null) return; // Kiểm tra máy cắt cỏ còn trong World không
    
        move(speed);
    
        // Lấy danh sách Zombie va chạm
        List<Zombie> targets = getIntersectingObjects(Zombie.class);
            for (Zombie z : targets) {
                
            if (getWorld() != null && z != null && z.getWorld() != null) {
                getWorld().removeObject(z);
            }
        }
    }

    /**
     * Kiểm tra xem có Zombie nào chạm vào máy để kích hoạt không
     */
    private void checkActivation()
    {
        if (isTouching(Zombie.class))
        {
            // Có thể thêm âm thanh khởi động ở đây
            // Greenfoot.playSound("lawnmower.mp3");
            
            setImage("lawn_mower2.png"); 
            speed = 8; 
            isMoving = true;
        }
    }

    /**
     * Xử lý khi máy chạy ra khỏi màn hình
     */
    private void handleBoundaries()
    {
        // Nếu máy đã chạy quá sát mép phải hoặc ra ngoài
        if (getWorld() != null && getX() >= getWorld().getWidth() - 5) {
            getWorld().removeObject(this);
        }
    }
}