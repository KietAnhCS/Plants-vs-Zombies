import greenfoot.*;

public class Setting extends Actor {
    public Setting() {
        // Tự động load ảnh setting.png của Kiệt
        setImage(new GreenfootImage("setting.png"));
    }

    public void act() {
        // Nếu đang mở menu mà bấm ESC lần nữa thì đóng menu
        if ("escape".equals(Greenfoot.getKey())) {
            getWorld().removeObject(this);
        }
    }
}