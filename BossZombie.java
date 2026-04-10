import greenfoot.*;

public class BossZombie extends Zombie
{
    // Phải khai báo ở đây nếu lớp cha Zombie không có sẵn biến walk/eat
    public GreenfootImage[] walk;
    public GreenfootImage[] eat;

    public BossZombie() {
        // 1. Nạp ảnh
        walk = importSprites("BossZombie", 10);
        eat = importSprites("bosszomeat", 10);
        
        // Gán các mảng kế thừa từ lớp Zombie
        headless = importSprites("BossZombie", 10); 
        headlesseating = importSprites("bosszomeat", 10);
        fall = importSprites("BossZombie", 1); 

        // 2. Chỉ số
        maxHp = 200;
        hp = maxHp;
        walkSpeed = 0.18; 
        
        // 3. Resize
        int w = 300;
        int h = 250;
        
        resizeArray(walk, w, h);
        resizeArray(eat, w, h);
        resizeArray(headless, w, h);
        resizeArray(headlesseating, w, h);
        resizeArray(fall, w, h);
        
        setImage(walk[0]);
    }

    @Override
    public void update() {
        if (!isEating()) {
            // 1. Tốc độ hình ảnh: 800-1000ms là đủ để thấy nó nhấc chân nặng nề
            animate(walk, 350, true);   
            
            // 2. Tốc độ di chuyển: 
            // Dùng 0.1 hoặc 0.15. Đây là con số "vàng" cho Boss to xác.
            // Nếu vẫn thấy nhanh, hãy giảm xuống 0.05
            double bossSpeed = 0.67; 
            
            // 3. Sử dụng setLocation với getX() - số thập phân
            // Greenfoot sẽ tự tích lũy phần lẻ để di chuyển cực mịn
            setLocation(getX() - bossSpeed, getY());
        } else {
            // Khi ăn thì cho nó nhai nhanh một chút để tạo áp lực
            animate(eat, 150, true); 
            playEating(); 
        }
    }
    

    private void resizeArray(GreenfootImage[] imgs, int w, int h) {
        if (imgs != null) {
            for (GreenfootImage img : imgs) {
                if (img != null) img.scale(w, h);
            }
        }
    }
}