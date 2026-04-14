import greenfoot.*;

public class Plant extends animatedObject {
    public int maxHp;
    public boolean isAlive = true;
    public int hp;
    public int damage;
    public boolean opaque = false;
    public MyWorld MyWorld;

    public Plant() {}

    public void act() {
        if (getWorld() == null) return;

        // Kiểm tra Overlay (Pause game)
        if (!getWorld().getObjects(Overlay.class).isEmpty()) return; 
        
        if (isLiving()) {
            update(); 
            if (getWorld() == null) return;

            // Xử lý độ trong suốt (đặt cây/xẻng)
            if (!opaque) {
                getImage().setTransparency(255);
            } else {
                getImage().setTransparency(125);
            }
        } else {
            die();
        }
    }

        public void die() {
        // Chốt chặn quan trọng nhất: Kiểm tra xem cây còn ở trong World không
        World world = getWorld(); 
        if (world != null) {
            MyWorld = (MyWorld)world;
            AudioPlayer.play(80, "gulp.mp3");
            
            // Lưu tọa độ ô lưới trước khi thực hiện các lệnh khác
            int xPos = getXPos();
            int yPos = getYPos();
            
            // Chỉ xóa trong mảng quản lý của Board nếu Board tồn tại
            if (MyWorld.board != null) {
                MyWorld.board.removePlant(xPos, yPos);
            }
            
            // Xóa chính nó khỏi World (chỉ khi world != null)
            world.removeObject(this);
        }
    }

    public void update() {}

    public int getXPos() {
        if (getWorld() == null) return -1; 
        return ((getX() - Board.xOffset) / Board.xSpacing);
    }   

    public int getYPos() {
        if (getWorld() == null) return -1;
        return ((getY() - Board.yOffset) / Board.ySpacing);
    }

    @Override
    public void addedToWorld(World world) {
        MyWorld = (MyWorld)world;
        // Thêm hiệu ứng đất khi trồng
        world.addObject(new Dirt(), getX(), getY() + 30);
    }

    public boolean isLiving() {
        if (hp <= 0) isAlive = false;
        return isAlive && getWorld() != null;
    }

    public void hit(int dmg) {
        if (getWorld() == null) return;
        if (!getWorld().getObjects(Overlay.class).isEmpty()) return;
        hp -= dmg;
    }
}