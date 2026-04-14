import greenfoot.*;

public class FallingSun extends FallingObject
{
    private MyWorld myWorld;
    private GreenfootImage[] sunSprites;
    private boolean beenClicked = false;
    
    // lifetimeStart dùng để tính thời gian mặt trời nằm dưới đất trước khi biến mất
    private long lifetimeStart; 

    public FallingSun() {
        // vSpeed = 0.6, không gia tốc (rơi đều), hSpeed = 0, rơi thẳng đứng
        // fallTime ngẫu nhiên từ 2-10 giây để mặt trời dừng lại ở các độ cao khác nhau
        super(0.6, 0, 0, 0, (long)Random.Int(2000, 5000)); 
        sunSprites = importSprites("sun", 2);
    }

    public void update() {
        animate(sunSprites, 200, true);

        if (!beenClicked) {
            if (checkClick()) {
                collectSun();
            } else {
                handleFallingAndWaiting();
            }
        } else {
            flyToCounter();
        }

        checkRemoval();
    }

    private void handleFallingAndWaiting() {
        currentFrame = System.nanoTime();
        deltaTime = (currentFrame - lastFrame) / 1000000;

        // Nếu đang trong thời gian rơi
        if (deltaTime < fallTime) {
            double y = getExactY() + vSpeed;
            setLocation(getExactX(), y);
            // Cập nhật mốc thời gian bắt đầu "chờ" liên tục khi đang rơi
            lifetimeStart = System.currentTimeMillis(); 
        } 
        // Nếu đã rơi xong và đang nằm chờ
        else {
            // Nếu nằm chờ quá 8 giây thì mờ dần và biến mất
            if (System.currentTimeMillis() - lifetimeStart > 8000) {
                fadeOut(10);
            }
        }
    }

    private void collectSun() {
        beenClicked = true;
        AudioPlayer.play(90, "points.mp3");
        if (myWorld != null && myWorld.seedbank != null) {
            myWorld.seedbank.sunCounter.addSun(25);
        }
    }

    private void flyToCounter() {
        turnTowards(SunCounter.x, SunCounter.y);
        move(20);
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
        // Kiểm tra xem đã chạm vào SunCounter chưa
        boolean reachedCounter = Math.abs(getX() - SunCounter.x) < 20 && Math.abs(getY() - SunCounter.y) < 20;

        if (getImage().getTransparency() == 0 || (beenClicked && reachedCounter)) {
            if (getWorld() != null) {
                getWorld().removeObject(this);
            }
        }
    }

    public boolean checkClick() {
        if (Greenfoot.mouseClicked(this)) return true;
        
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse != null && Greenfoot.mouseClicked(null)) {
            myWorld.moveHitbox();
            return intersects(myWorld.hitbox);
        }
        return false;
    }

    @Override
    public void addedToWorld(World world) {
        myWorld = (MyWorld)world;
        lastFrame = System.nanoTime(); // Bắt đầu tính thời gian rơi
        lifetimeStart = System.currentTimeMillis(); 
    }
}