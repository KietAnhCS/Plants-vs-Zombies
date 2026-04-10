import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Pea here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */

public class Projectile extends animatedObject {
    public int speed = 4;
    public GreenfootImage[] image;
    public boolean hit = false;
    public MyWorld MyWorld;
    public boolean foundTarget = false;
    public Zombie hitZombie;
    public int frameCount;
    public int yPos;
    public int damage;
    /**
     * Act - do whatever the Pea wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public Projectile(String name, int frameCount, int yPos, int dmg, int speed) {
        this.frameCount = frameCount;
        this.image = importSprites(name, frameCount);
        this.yPos = yPos;
        this.damage = dmg;
        this.speed = speed;
        setImage(image[0]);
        
        
        
    }
    public void act() {
        if (getWorld() == null) return;
        if (getWorld() != null) {
            MyWorld = (MyWorld)getWorld();
        
            // 1. Kiểm tra xóa do hết frame anime
        if (frame >= frameCount) {
            MyWorld.removeObject(this);   
            return; // Thoát ngay lập tức
        }
        
        // 2. Di chuyển hoặc Animate
        if (!hit) {
            move(speed);
        } else {
            animate(image, 150, false);
        }
        
        // 3. Kiểm tra biên
        if (isAtEdge()) {
            MyWorld.removeObject(this);   
            return; // Thoát ngay lập tức
        }
        
        // 4. Kiểm tra va chạm với Zombie
        // Dùng phương thức này của Greenfoot sẽ tối ưu và an toàn hơn là tự duyệt list
        for (Zombie i : MyWorld.level.zombieRow.get(yPos)) {
            // CỰC KỲ QUAN TRỌNG: Kiểm tra xem viên đạn còn sống không trước khi lấy tọa độ
            if (getWorld() == null) return; 

            if (Math.abs(i.getX() - getX()) < 30) {
                if (!foundTarget) {
                    hitZombie = i;
                    foundTarget = true;
                } 
                
                if (!hit) {
                    hitZombie.hit(damage);
                    hit = true;
                    // Nếu cậu muốn đạn biến mất ngay khi chạm, hãy thêm removeObject ở đây và return luôn
                } else if (hitZombie.getWorld() != null && getX() < hitZombie.getX()) {
                    move(speed); 
                }
            }
        }
    }
}
}
