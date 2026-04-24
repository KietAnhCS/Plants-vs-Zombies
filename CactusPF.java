import greenfoot.*; 

public class CactusPF extends Plant
{
    private GreenfootImage[] idle;
    private GreenfootImage[] shoot;
    private boolean shootOnce = false;
    private int shootCount = 0;
    private boolean resetFrame = false;
    private boolean shooting = false;
    private long shootDelay = 1700L;
    private long lastFrame2 = System.nanoTime();
    private long deltaTime2;
    
    public CactusPF() {
        maxHp = 60;
        hp = maxHp;
        
        // Sử dụng hàm loadResized thay vì importSprites trực tiếp
        // 0.45 là tỷ lệ thu nhỏ (45% kích thước gốc)
        shoot = loadResized("attackcactusPF", 30, 0.45); 
        idle = loadResized("idlecactusPF", 30, 0.45);
        
        if (idle != null && idle.length > 0) {
            setImage(idle[0]);
        }
    }

    // Hàm resize giống BonkChoy nhưng thêm tham số scale để linh hoạt
    private GreenfootImage[] loadResized(String prefix, int count, double scale) {
        GreenfootImage[] imgs = importSprites(prefix, count);
        
        for (GreenfootImage img : imgs) {
            if (img != null) {
                int oldWidth = img.getWidth();
                int oldHeight = img.getHeight();
                
                int newWidth = (int)(oldWidth * scale);
                int newHeight = (int)(oldHeight * scale);
                
                // Đảm bảo kích thước không bằng 0 để tránh lỗi Greenfoot
                if (newWidth <= 0) newWidth = 1;
                if (newHeight <= 0) newHeight = 1;
                
                img.scale(newWidth, newHeight);
            }
        }
        return imgs;
    }
   
    public void update() {
        if (getWorld() == null) return;
        
        PlayScene world = (PlayScene)getWorld();
        currentFrame = System.nanoTime();
        
        if (!shooting) {
            animate(idle, 225, true);
            lastFrame2 = System.nanoTime();
        } else {
            deltaTime2 = (currentFrame - lastFrame2) / 1000000;
            if (deltaTime2 < shootDelay) {
                animate(idle, 225, true);
                shootCount = 0;
                resetFrame = false;
            } else {
                if (shootCount >= 4) {
                    lastFrame2 = currentFrame;
                }
                if (!resetFrame) {
                    setFrame(1);
                    resetFrame = true;
                }
                
                if (frame >= 4) {
                    AudioPlayer.play(80, "throw.mp3", "throw2.mp3");
                    
                    // Bắn đạn (Needle)
                    if (getWorld() != null) {
                        world.addObject(new Needle(getYPos()), getX() + 25, getY() - 17);
                    }
                    
                    setFrame(1);
                    // Lưu ý: Nếu bạn dùng setImage bằng tên file thủ công, 
                    // ảnh đó sẽ không được resize. Tốt nhất là dùng:
                    setImage(shoot[1]); 
                    
                    shootCount++;
                }
                animate(shoot, 70, false);
            }
        }
        
        // Logic kiểm tra Zombie trong hàng
        checkZombies(world);
    }

    private void checkZombies(PlayScene world) {
        if (world.level.zombieRow.get(getYPos()).size() == 0) {
            shooting = false;
        } else {
            boolean foundZombie = false;
            for (Zombie i : world.level.zombieRow.get(getYPos())) {
                if (i != null && i.getWorld() != null && i.getX() > getX() && i.getX() <= world.getWidth() + 10) {
                    foundZombie = true;
                    break;
                }
            }
            shooting = foundZombie;                      
        }
    }
}