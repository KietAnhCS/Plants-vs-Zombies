import greenfoot.*;

public class FallingSun extends FallingObject
{
    private PlayScene PlayScene;
    private GreenfootImage[] sunSprites;
    private boolean beenClicked = false;
    private long lifetimeStart; 

    public FallingSun() {
        super(0.6, 0, 0, 0, (long)Random.Int(2000, 5000)); 
        sunSprites = importSprites("sun", 2);
    }

    public void update() {
        if (getWorld() == null) return;
        animate(sunSprites, 200, true);

        // CHẶN ĐỨNG: Chỉ khi chạm Hero mới cho collect
        if (!beenClicked) {
            if (isTouching(Hero.class)) {
                collectSun();
            } else {
                handleFallingAndWaiting();
            }
        } else {
            flyToCounter();
        }

        checkRemoval();
    }

    // Hàm này để Hero gọi khi đi ngang qua (hoặc dùng isTouching ở trên)
    public void collectSun() {
        if (beenClicked) return;
        beenClicked = true;
        AudioPlayer.play(90, "points.mp3");
        if (PlayScene != null && PlayScene.seedbank != null) {
            PlayScene.seedbank.sunCounter.addSun(25);
        }
    }

    
    public boolean checkClick() {
        return false;
    }

    private void handleFallingAndWaiting() {
        currentFrame = System.nanoTime();
        deltaTime = (currentFrame - lastFrame) / 1000000;

        if (deltaTime < fallTime) {
            double y = getExactY() + vSpeed;
            setLocation(getExactX(), y);
            lifetimeStart = System.currentTimeMillis(); 
        } else {
            if (System.currentTimeMillis() - lifetimeStart > 8000) {
                fadeOut(10);
            }
        }
    }

    private void flyToCounter() {
        turnTowards(SunCounter.x, SunCounter.y);
        move(20);
    }

    private void fadeOut(int amount) {
        int trans = getImage().getTransparency();
        if (trans > amount) getImage().setTransparency(trans - amount);
        else getImage().setTransparency(0);
    }

    private void checkRemoval() {
        boolean reachedCounter = Math.abs(getX() - SunCounter.x) < 20 && Math.abs(getY() - SunCounter.y) < 20;
        if (getImage().getTransparency() == 0 || (beenClicked && reachedCounter)) {
            if (getWorld() != null) getWorld().removeObject(this);
        }
    }

    @Override
    public void addedToWorld(World world) {
        PlayScene = (PlayScene)world;
        lastFrame = System.nanoTime(); 
        lifetimeStart = System.currentTimeMillis(); 
    }
}