import greenfoot.*;
import java.util.*;

public class Zombie extends animatedObject {
    public boolean fallen = false;
    public boolean eating = false;
    public boolean eatOnce = false;
    public int hp;
    public int maxHp;
    public double walkSpeed;
    public MyWorld MyWorld;
    public boolean spawnHead = false;
    public Plant target; // Mục tiêu đang bị ăn
    public int eatSpeed;
    public boolean isAlive = true;
    public GreenfootImage[] headless;
    public GreenfootImage[] headlesseating;
    public GreenfootImage[] fall;
    public boolean resetAnim = false;
    public boolean finalDeath = false;
    public boolean fixAnim = false;

    public Zombie() {
        headless = importSprites("zombieheadless", 7);
        fall = importSprites("zombiefall", 6);
        headlesseating = importSprites("headlesszombieeating", 7);
    }

    public void act() {
        if (getWorld() == null) return;

        // KIỂM TRA OVERLAY: Pause game
        if (!getWorld().getObjects(Overlay.class).isEmpty()) {
            return;
        }

        if (isLiving()) {
            update();
        } else {
            deathAnim();
        }
    }

    public void update() {
        // Class con (BasicZombie, Conehead...) sẽ override hàm này
    }

    public void deathAnim() {
        if (!resetAnim) {
            frame = 0;
            resetAnim = true;
        }

        if (frame <= 6) {
            if (finalDeath) {
                if (!fixAnim) {
                    fixAnim = true;
                    AudioPlayer.play(80, "zombie_falling_1.mp3", "zombie_falling_2.mp3");
                    MyWorld.addObject(new fallingZombie(fall), getX() - 12, getY() + 20);
                    MyWorld.removeObject(this);
                    return;
                }
            } else {
                if (!spawnHead) {
                    spawnHead = true;
                    AudioPlayer.play(80, "limbs_pop.mp3");
                    getWorld().addObject(new Head(), getX(), getY() - 10);
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
            removeFromRow();
        }
    }

    public void playEating() {
        if (getWorld() == null || !getWorld().getObjects(Overlay.class).isEmpty()) return;
        
        // CHỐT CHẶN: Nếu mục tiêu đã biến mất khỏi World (bị ăn xong), ngừng ăn
        if (target == null || target.getWorld() == null) {
            eating = false;
            target = null;
            return;
        }

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
        MyWorld = (MyWorld) world;
    }

    public boolean isLiving() {
        return hp > 0;
    }

    public void takeDmg(int dmg) {
        if (!isAlive) return;
        hp -= dmg;
        if (hp <= 0) {
            isAlive = false;
            removeFromRow();
        }
    }

    // Hàm phụ để xóa zombie khỏi danh sách quản lý hàng
    private void removeFromRow() {
        if (MyWorld == null || MyWorld.level == null) return;
        for (ArrayList<Zombie> i : MyWorld.level.zombieRow) {
            if (i.contains(this)) {
                i.remove(this);
                break;
            }
        }
    }

    public boolean isEating() {
        if (MyWorld == null || MyWorld.board == null) return false;
        
        int yIdx = getYPos();
        // Kiểm tra biên yIdx để tránh crash mảng Board
        if (yIdx < 0 || yIdx >= MyWorld.board.Board.length) return false;

        var row = MyWorld.board.Board[yIdx];
        for (int i = 0; i < row.length; i++) {
            Plant p = row[i];
            
            // CHỐT CHẶN: Chỉ check nếu cây tồn tại trong World
            if (p != null && p.getWorld() != null) {
                if (Math.abs(p.getX() - getX() + 5) < 35) {
                    // Đặc thù PotatoMine
                    if (p instanceof PotatoMine) {
                        if (((PotatoMine) p).armed) {
                            eating = false;
                            return false;
                        }
                    }
                    
                    eating = true;
                    target = p;
                    return true;
                }
            }
        }

        eating = false;
        target = null;
        return false;
    }

    public int getYPos() {
        if (Board.ySpacing == 0) return 0; // Hoặc một giá trị mặc định an toàn
        return (getY() - Board.yOffset) / Board.ySpacing;
    }

    public int getXPos() {
        return getX();
    }
    
    public void hit(int dmg) {
        takeDmg(dmg);
    }
}