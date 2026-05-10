import greenfoot.GreenfootImage;

/**
 * <<interface>>
 * IZombieState: Định nghĩa các hành vi chung cho mọi trạng thái của Zombie.
 * Giúp tách biệt logic xử lý (Walking, Eating, Dead) ra khỏi lớp Zombie chính.
 */
public interface IZombieState 
{
    /**
     * + enter() : void
     * Được gọi một lần duy nhất khi Zombie bắt đầu chuyển vào trạng thái này.
     * Thường dùng để thiết lập ban đầu hoặc reset hoạt ảnh.
     */
    void enter();

    /**
     * + update() : void
     * Được gọi liên tục trong mỗi vòng lặp game (phương thức act của Zombie).
     * Chứa logic chính của trạng thái (ví dụ: di chuyển, trừ máu cây).
     */
    void update();

    /**
     * + exit() : void
     * Được gọi một lần duy nhất trước khi Zombie chuyển sang một trạng thái khác.
     * Dùng để dọn dẹp hoặc dừng các hiệu ứng âm thanh/hình ảnh.
     */
    void exit();

    /**
     * + getAnimation() : GreenfootImage[]
     * Trả về mảng các khung hình hoạt ảnh tương ứng với trạng thái hiện tại.
     * Dữ liệu này sẽ được SpriteAnimator sử dụng để hiển thị lên màn hình.
     * 
     * @return Mảng các đối tượng GreenfootImage.
     */
    GreenfootImage[] getAnimation();
}