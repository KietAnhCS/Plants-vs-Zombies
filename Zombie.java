import greenfoot.*;
import java.util.*;

public class Zombie extends animatedObject {
    public boolean fallen = false;
    public boolean eating = false;
    public boolean eatOnce = false;
    public int hp;
    public int maxHp;
    public double walkSpeed;
    public PlayScene PlayScene;
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

    public Zombie() {
        headless = importSprites("zombieheadless", 7);
        fall = importSprites("zombiefall", 6);
        headlesseating = importSprites("headlesszombieeating", 7);
    }

    public void act() {
        if (getWorld() == null) return;

        
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
                    PlayScene.addObject(new fallingZombie(fall), getX() - 12, getY() + 20);
                    PlayScene.removeObject(this);
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
        PlayScene = (PlayScene) world;
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

    
    private void removeFromRow() {
        if (PlayScene == null || PlayScene.level == null) return;
        for (ArrayList<Zombie> i : PlayScene.level.zombieRow) {
            if (i.contains(this)) {
                i.remove(this);
                break;
            }
        }
    }

    public boolean isEating() {
        if (PlayScene == null || PlayScene.board == null) return false;
        
        int yIdx = getYPos();
        
        if (yIdx < 0 || yIdx >= PlayScene.board.Board.length) return false;

        var row = PlayScene.board.Board[yIdx];
        for (int i = 0; i < row.length; i++) {
            Plant p = row[i];
            
            if (p != null && p.getWorld() != null) {
                if (Math.abs(p.getX() - getX() + 5) < 35) {
                    
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
        if (PlayScene == null || PlayScene.board == null) return 0;
        
        // Sử dụng double để tính toán chính xác trước khi làm tròn
        double calculateY = (double)(getY() - PlayScene.board.yOffset) / PlayScene.board.ySpacing;
        
        // Làm tròn số gần nhất (ví dụ 4.9 -> 5, 5.1 -> 5)
        int row = (int)Math.round(calculateY);
        
        // Đảm bảo không bị văng khỏi mảng (Index out of bounds)
        if (row < 0) return 0;
        if (row >= PlayScene.board.currentRowCount) return PlayScene.board.currentRowCount - 1;
        
        return row;
    }

    public int getXPos() {
        return getX();
    }
    
    public void hit(int dmg) {
        takeDmg(dmg);
    }
}