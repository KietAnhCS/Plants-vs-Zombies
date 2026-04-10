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
        // Chốt chặn 1: Nếu vừa vào mà đã bị xóa (do Lawnmower chạy trước) thì nghỉ luôn
        if (getWorld() == null) return;

        if (isLiving()) {
            update(); // Gọi logic của Peashooter, Sunflower...
            
            // Chốt chặn 2: Sau khi update, nhỡ đâu bị xóa trong lúc update thì dừng
            if (getWorld() == null) return;

            if (!opaque) {
                getImage().setTransparency(255);
            } else {
                getImage().setTransparency(125);
            }
        } else {
            // Cây hết máu hoặc bị xóa chủ động
            die();
        }
    }

    public void die() {
        if (getWorld() != null) {
            MyWorld = (MyWorld)getWorld();
            AudioPlayer.play(80, "gulp.mp3");
            
            // Lưu tọa độ vào biến tạm TRƯỚC khi xóa đối tượng
            int xPos = getXPos();
            int yPos = getYPos();
            
            MyWorld.board.removePlant(xPos, yPos);
            MyWorld.removeObject(this);
        }
    }

    public void update() {
        // Lớp con sẽ override hàm này
    }

    public int getXPos() {
        if (getWorld() == null) return 0; 
        return ((getX() - Board.xOffset) / Board.xSpacing);
    }   

    public int getYPos() {
        if (getWorld() == null) return 0;
        return ((getY() - Board.yOffset) / Board.ySpacing);
    }

    @Override
    public void addedToWorld(World world) {
        MyWorld = (MyWorld)getWorld();
        MyWorld.addObject(new Dirt(), getX(), getY() + 30);
    }

    public boolean isLiving() {
        if (hp <= 0) {
            isAlive = false;
        } else {
            isAlive = true;
        }
        return isAlive;
    }

    public void hit(int dmg) {
        hp -= dmg;
    }
}