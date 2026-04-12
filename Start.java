import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Class Start đã được scale 1.5x và tối ưu logic.
 */
public class Start extends Button
{
    public boolean clicked = false;
    GreenfootImage[] start;
    public int counter = 0;

    public Start() {
        super("start1.png", "start2.png");
        
        // 1. Import mảng ảnh animation
        start = importSprites("start", 2);
        
        // 2. Phóng to mảng ảnh animation lên 1.5 lần
        for (int i = 0; i < start.length; i++) {
            start[i].scale((int)(start[i].getWidth() * 1.25), (int)(start[i].getHeight() * 1.25));
        }
        
        // 3. Phóng to ảnh Idle và Hover (từ class cha Button)
        if (idle != null) {
            idle.scale((int)(idle.getWidth() * 1.25), (int)(idle.getHeight() * 1.25));
        }
        if (hover != null) {
            hover.scale((int)(hover.getWidth() * 1.25), (int)(hover.getHeight() * 1.25));
        }
        
        // Cập nhật lại hình ảnh ban đầu
        setImage(idle);
    }

    public void act()
    {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        MainMenu world = (MainMenu)getWorld();
        
        if (clicked) {
            // Hiệu ứng nháy/animation khi đã click
            animate(start, 80, true);
            counter++;
            if (counter == 200) {
                update();
            }
        } else {
            if (mouse != null) {
                // Luôn cập nhật hitbox theo chuột
                world.moveHitbox();
                
                // Hiệu ứng Hover
                if (this.intersects(world.hitbox)) {
                    setImage(hover);
                } else {
                    setImage(idle);
                }
                
                // Xử lý khi click vào nút
                if (Greenfoot.mouseClicked(this)) {
                    clicked = true;
                    world.menutheme.stop();
                    
                    // Chạy âm thanh rùng rợn
                    AudioPlayer.play(100, "gravebutton.mp3");
                    AudioPlayer.play(70, "losemusic.mp3");
                    getWorld().addObject(new DelayAudio(new GreenfootSound("evillaugh.mp3"), 70, false, 1000L), 0, 0);
                    
                    // Hiện cái tay Zombie (Cũng nên scale 1.5 trong class ZombieHand cho khớp)
                    getWorld().addObject(new ZombieHand(), 300, 500);
                }
            }
        }
    }

    public void update() {
        // Chuyển cảnh sang Intro
        getWorld().addObject(new Transition(false, new Intro(), 4), 365, 215);
    }
}