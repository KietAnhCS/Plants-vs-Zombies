import greenfoot.*;

public class WalkingState implements IZombieState {
    private Zombie zombie;

    public WalkingState(Zombie zombie) {
        this.zombie = zombie;
    }

    @Override
    public void enter() {
        // Có thể thêm log hoặc hiệu ứng khi zombie bắt đầu đi
    }

    @Override
    public void update() {
        // 1. Kiểm tra va chạm trước khi đi
        if (zombie.checkEating()) {
            // Chuyển sang trạng thái ăn (Đảm bảo bạn đã tạo lớp EatingState)
            zombie.setState(new EatingState(zombie));
            return; 
        }

        // 2. Nếu không có gì cản trở thì mới đi tiếp
        zombie.walk();
    }

    @Override
    public void exit() {
        // Dọn dẹp nếu cần
    }

    @Override
    public GreenfootImage[] getAnimation() {
        /* 
           LƯU Ý: Thay vì dùng zombie.getWalkingSprites(), 
           ta nên ép kiểu hoặc dùng một phương thức chung để lấy đúng bộ ảnh 
           theo tình trạng máu (còn tay hay mất tay).
        */
        if (zombie instanceof BasicZombie) {
            BasicZombie bz = (BasicZombie) zombie;
            // Trả về bộ ảnh Armless nếu máu thấp, ngược lại trả về Normal
            return (bz.getHp() <= ZombieRegistry.BASIC_ARMLESS) ? bz.wArmless : bz.wNormal;
        }
        
        // Mặc định trả về một bộ ảnh nào đó nếu không phải BasicZombie
        return null; 
    }
}