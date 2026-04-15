import greenfoot.*;

public class Sun extends FallingObject
{
    private PlayScene PlayScene;
    private GreenfootImage[] sunSprites;
    private boolean beenClicked = false;
    
    private long lifetimeStart; 

    public Sun() {
       
        super(-3.5, 0.15, Random.Int(-1, 1), 1, 800L); 
        sunSprites = importSprites("sun", 2);
    }

    public void update() {
        // KIỂM TRA ĐẦU TIÊN: Nếu đối tượng không còn trong World thì thoát ngay
        if (getWorld() == null) return;

        // 1. Hiệu ứng hoạt họa
        animate(sunSprites, 200, true);

        // 2. Logic click và di chuyển
        if (!beenClicked) {
            if (checkClick()) {
                collectSun();
            } else {
                handleAutoFadeOut();
                applyFallingPhysics();
            }
        } else {
            flyToCounter();
        }

        // 3. Kiểm tra xóa đối tượng (Luôn để ở cuối hàm update)
        checkRemoval();
    }

    private void collectSun() {
        beenClicked = true;
        // AudioPlayer.play(90, "points.mp3");
        
        // Cập nhật điểm an toàn hơn
        if (PlayScene != null && PlayScene.seedbank != null) {
            PlayScene.seedbank.sunCounter.addSun(25);
        }
        setRotation(0); 
    }

    private void applyFallingPhysics() {
        currentFrame = System.nanoTime();
        // SỬA: Đảm bảo lastFrame đã được khởi tạo để tránh lỗi thời gian
        deltaTime = (currentFrame - lastFrame) / 1000000;

        if (deltaTime < fallTime) {
            double x = getExactX() + hSpeed;
            double y = getExactY() + vSpeed;
            setLocation(x, y);
            turn(rotate);
            vSpeed += acceleration;
        }
    }

    private void flyToCounter() {
        // Bay về phía SunCounter (Dùng toạ độ static)
        turnTowards(SunCounter.x, SunCounter.y);
        move(15);
    }

    private void handleAutoFadeOut() {
        // Tính thời gian tồn tại bằng Millis cho nhẹ
        if ((System.currentTimeMillis() - lifetimeStart) > 12000) {
            fadeOut(10);
        }
    }

    private void fadeOut(int amount) {
        int trans = getImage().getTransparency();
        if (trans > amount) {
            getImage().setTransparency(trans - amount);
        } else {
            getImage().setTransparency(0);
        }
    }

    private void checkRemoval() {
        if (getWorld() == null) return;

        // Kiểm tra đã chạm đích chưa
        boolean reachedCounter = Math.abs(getX() - SunCounter.x) < 20 && Math.abs(getY() - SunCounter.y) < 20;
        
        if (getImage().getTransparency() == 0 || (beenClicked && reachedCounter)) {
            getWorld().removeObject(this);
        }
    }

    public boolean checkClick() {
        // Ưu tiên click trực tiếp vào đối tượng
        if (Greenfoot.mouseClicked(this)) return true;

        // Click thông qua hitbox (nếu click hụt một tí vẫn ăn)
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse != null && Greenfoot.mouseClicked(null)) {
            if (PlayScene != null && PlayScene.hitbox != null) {
                PlayScene.moveHitbox();
                return intersects(PlayScene.hitbox);
            }
        }
        return false;
    }

    @Override
    public void addedToWorld(World world) {
        PlayScene = (PlayScene)world;
        lastFrame = System.nanoTime(); 
        lifetimeStart = System.currentTimeMillis(); 
    }
}