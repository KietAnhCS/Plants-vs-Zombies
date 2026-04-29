import greenfoot.*;
import java.util.List;

public class Cactus extends Plant {
    private GreenfootImage[] idle;
    private GreenfootImage[] shoot;
    private boolean shooting = false;
    private boolean isShootingAnimation = false;
    private long shootDelay = 2400L;
    private long lastShootTime = System.currentTimeMillis();

    public Cactus() {
        maxHp = 100;
        hp = maxHp;
        shoot = importSprites("cactusshoot", 2);
        idle = importSprites("cactus", 4);
        setImage(idle[0]);
    }

    @Override
    public void hit(int dmg) {
        if (isLiving()) {
            hitFlash(isShootingAnimation ? shoot : idle, isShootingAnimation ? "cactusshoot" : "cactus");
            hp -= dmg;
        }
    }

    @Override
    public void update() {
        if (getWorld() == null) return;
        
        playScene = (PlayScene) getWorld();

        checkZombieInRow();
        handleAction();
    }

    private void handleAction() {
        long currentTime = System.currentTimeMillis();

        if (!shooting && !isShootingAnimation) {
            animate(idle, 150, true);
            return;
        }

        if (currentTime - lastShootTime >= shootDelay) {
            isShootingAnimation = true;
            animate(shoot, 150, false);

            if (frame == 1) {
                fireNeedle();
            }

            if (frame >= shoot.length - 1) {
                isShootingAnimation = false;
                lastShootTime = currentTime;
                setFrame(0);
            }
        } else {
            animate(idle, 150, true);
        }
    }

    private void fireNeedle() {
        if (getWorld() != null) {
            AudioManager.playSound(80, false, "throw.mp3");
            getWorld().addObject(new Needle(getYPos()), getX() + 30, getY() - 8);
        }
    }

    private void checkZombieInRow() {
        if (playScene == null || playScene.level == null) return;

        List<Zombie> rowZombies = playScene.level.zombieRow.get(getYPos());
        if (rowZombies == null || rowZombies.isEmpty()) {
            shooting = false;
            return;
        }

        shooting = rowZombies.stream().anyMatch(z -> 
            z.getWorld() != null && 
            z.getX() > getX() && 
            z.getX() <= playScene.getWidth() + 10
        );
    }
}