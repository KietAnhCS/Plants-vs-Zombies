import greenfoot.*;

public class Transition extends Actor
{
    public World world;
    public boolean fadeOut = false;
    public int fadeSpeed;
    public int alpha = 0;
    public int counter = 0;
    public int waitDuration = 0;
    
    // Constructor 1
    public Transition(boolean fadeOut, World world, int speed) {
        this(fadeOut, world, speed, 0);
    }

    // Constructor 2
    public Transition(boolean fadeOut, World world, int speed, int waitDuration) {
        this.fadeSpeed = speed;
        this.world = world;
        this.fadeOut = fadeOut;
        this.waitDuration = waitDuration;
        int w = world.getWidth();
        int h = world.getHeight();
        
        // FIX NHANH: Tạo một tấm ảnh đen xì che màn hình nếu chưa có ảnh
        if (getImage() == null) {
            setImage(new GreenfootImage(w, h)); 
            getImage().setColor(Color.BLACK);
            getImage().fill();
        }
        getImage().setTransparency(0);
    }

    // Constructor 3
    public Transition(boolean fadeOut, World world, String image, int speed) {
        this.fadeSpeed = speed;
        setImage(new GreenfootImage(image));
        this.world = world;
        this.fadeOut = fadeOut;
        getImage().setTransparency(0);
    }

    public void act()
    {
        // Kiểm tra an toàn
        if (getImage() == null) return;

        if (alpha + fadeSpeed <= 255) {
            alpha += fadeSpeed;
            getImage().setTransparency(alpha);
        } else {
            getImage().setTransparency(255);
            counter++;
            if (counter > waitDuration) {
                executeTransition();
            }
        }
    }

    private void executeTransition() {
        if (world != null) {
            if (fadeOut) {
                // Thêm hiệu ứng kết thúc vào World mới (tùy chọn tọa độ)
                world.addObject(new EndTransition(), world.getWidth()/2, world.getHeight()/2);
            }
            Greenfoot.setWorld(world);
        }
        if (getWorld() != null) {
            getWorld().removeObject(this);
        }
    }
}