import greenfoot.*;
import java.util.List;
import java.util.ArrayList;

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
        if (getWorld() == null) return;

        if (isMoving) {
            handleMovement();
        } else {
            checkActivation();
        }

        // Kiểm tra biên
        handleBoundaries();
    }

    private void handleMovement()
    {
        if (getWorld() == null) return;
        
        move(speed);
        
        // Lấy danh sách Zombie va chạm
        List<Zombie> targets = getIntersectingObjects(Zombie.class);
        
        // Sử dụng một bản sao danh sách để duyệt, tránh lỗi bộ nhớ
        for (Zombie z : new ArrayList<>(targets)) {
            if (z != null && z.getWorld() != null) {
                // QUAN TRỌNG: Gọi hàm tự hủy của Zombie để nó tự xóa khỏi zombieRow
                // thay vì chỉ xóa mỗi Actor khỏi World.
                z.takeDmg(9999); // Ép Zombie chết để nó chạy logic xóa trong class Zombie
                
                // Nếu sau khi takeDmg mà Zombie vẫn còn (do đang diễn anim chết), 
                // thì máy cắt cỏ "nghiền" nát luôn:
                if (z.getWorld() != null) {
                    getWorld().removeObject(z);
                }
            }
        }
    }

    private void checkActivation()
    {
        // Kiểm tra va chạm với Zombie để kích hoạt
        if (isTouching(Zombie.class))
        {
            setImage("lawn_mower2.png"); 
            speed = 8; 
            isMoving = true;
            
            // Phát âm thanh nếu có
            // AudioPlayer.play(100, "lawnmower.mp3");
        }
    }

    private void handleBoundaries()
    {
        if (getWorld() == null) return;

        // Nếu đi quá biên phải màn hình
        if (getX() >= getWorld().getWidth() - 2) {
            getWorld().removeObject(this);
        }
    }
}