import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;


public class Zombie extends animatedObject
{
    public boolean fallen = false;
    public boolean eating = false;
    public boolean eatOnce = false;
    public int hp;
    public int maxHp;
    public double walkSpeed;
    public MyWorld MyWorld;
    public boolean spawnHead = false;
    public Plant target;
    public int eatSpeed;
    public boolean isAlive = true;
    public GreenfootImage[] headless;
    public GreenfootImage[] headlesseating;
    public GreenfootImage[] fall;
    public boolean resetAnim = false;
    public boolean finalDeath = false;
    public boolean fixAnim = false;
    /**
     * Act - do whatever the Zombie wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public Zombie() {
    
        headless = importSprites("zombieheadless", 7);
        fall = importSprites("zombiefall",6);
        headlesseating = importSprites("headlesszombieeating", 7);
    }
    public void act()
    {
        if (getWorld() != null) {
            // KIỂM TRA OVERLAY: Nếu có màn hình tối (Pause) thì dừng toàn bộ hành động
            if (!getWorld().getObjects(Overlay.class).isEmpty()) {
                return; 
            }
            
            if (isLiving()) {
                update();
            } else {
                deathAnim();            
            }
        }
    }
    
    public void update() {
        
    }
    
    public void deathAnim() {
        if (!resetAnim) {
                frame = 0;    
                resetAnim = true;
        }
        if (frame <=6) {
            if (finalDeath) {
                if (!fixAnim) {
                    fixAnim = true;
                    AudioPlayer.play(80, "zombie_falling_1.mp3", "zombie_falling_2.mp3");
                    
                    MyWorld.addObject(new fallingZombie(fall), getX()-12, getY()+20);
                    MyWorld.removeObject(this);
                    return;
                }
                
                
            } else {
                if (!spawnHead) {
                    spawnHead = true;
                    AudioPlayer.play(80, "limbs_pop.mp3");
                    getWorld().addObject(new Head(), getX(), getY()-10);
                }
                if (!eating) {
                    animate(headless, 350, false);
                    move(-walkSpeed);
                } else {
                    animate(headlesseating, 350, false);
                }
                
            }
        } else if (!finalDeath) {
            resetAnim = false;
            finalDeath = true;
            
            for (ArrayList<Zombie> i : MyWorld.level.zombieRow) {
                if (i.contains(this)) {
                    i.remove(this);                    
                    break;
                }
            }
            
        } 

    }
    
    public void playEating() {
        // Chặn âm thanh và trừ máu nếu game đang Pause
        if (!getWorld().getObjects(Overlay.class).isEmpty()) return;

        if (frame == 5 || frame == 2) {
            if (!eatOnce) {
                eatOnce = true;
                AudioPlayer.play(70, "chomp.mp3", "chomp2.mp3", "chompsoft.mp3");
                target.hit(10);
            } 
        } else {
            eatOnce = false;
        }
    }
    @Override
    protected void addedToWorld(World world) {
        MyWorld = (MyWorld)getWorld();
        
    }
    public boolean isLiving() {
        if (hp <=0) {
            isAlive = false;
        } else {
            isAlive = true;
        }
        return isAlive;
    }
    public void hit(int dmg) {
        
    }
    
    public void takeDmg(int dmg) {
        hp -= dmg;
        if (hp <= 0) {
            // Đánh dấu là đã chết để hàm act() chuyển sang gọi deathAnim()
            isAlive = false; 
            
            // Xóa Zombie khỏi danh sách quản lý của WaveManager ngay để 
            // màn chơi biết con này đã bị tiêu diệt (không tính vào số lượng zombie còn sống)
            for (ArrayList<Zombie> i : MyWorld.level.zombieRow) {
                if (i.contains(this)) {
                    i.remove(this);                    
                    break;
                }
            }
            
            // TUYỆT ĐỐI KHÔNG gọi getWorld().removeObject(this) ở đây.
            // Nếu xóa ở đây, Zombie sẽ biến mất ngay lập tức, không kịp diễn anim ngã.
        }
    }
    public boolean isEating() {
        var row = MyWorld.board.Board[getYPos()];
        for (int i = 0; i < MyWorld.board.Board[0].length; i++) {
            if (row[i] != null) {
                
                if (Math.abs(row[i].getX() - getX()+5) < 35) {
                    if (row[i] instanceof PotatoMine) {
                        if (((PotatoMine)row[i]).armed == true) {
                            eating = false;
                            return false;
                        }
                    }
                    
                    eating = true;
                    target = row[i];
                    return eating;
                }
                
            }
        }
        
        eating = false;
        return eating;
    
    }
    public int getYPos() {
        return ((getY()-MyWorld.level.yOffset)/MyWorld.level.ySpacing);
    }
    public int getXPos() {
        return getX();
    }
    
}
