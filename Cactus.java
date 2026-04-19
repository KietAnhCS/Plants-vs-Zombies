import greenfoot.*;

public class Cactus extends Plant
{
    private GreenfootImage[] idle;
    private GreenfootImage[] shoot;
    private boolean shootOnce = false;
    private boolean shooting = false;
    private long shootDelay = 2400L;
    private long lastFrame2 = System.nanoTime();
    private long deltaTime2;

    public Cactus() {
        maxHp = 100;
        hp = maxHp;
        shoot = importSprites("cactusshoot", 2);
        idle = importSprites("cactus", 4);
    }

    public void hit(int dmg) {
        if (isLiving()) {
            if (!shootOnce) {
                hitFlash(idle, "cactus");
            } else {
                hitFlash(shoot, "cactusshoot");
            }
            hp = hp - dmg;
        }
    }

    public void update() {
        if (getWorld() == null) return;
        
        PlayScene = (PlayScene)getWorld();
        currentFrame = System.nanoTime();

        if (PlayScene.level.zombieRow.get(getYPos()).isEmpty()) {
            shooting = false;
        } else {
            boolean found = false;
            for (Zombie i : PlayScene.level.zombieRow.get(getYPos())) {
                if (i != null && i.getWorld() != null) {
                    if (i.getX() > getX() && i.getX() <= PlayScene.getWidth() + 10) {
                        found = true;
                        break;
                    }
                }
            }
            shooting = found;
        }

        if (!shooting) {
            animate(idle, 150, true);
            lastFrame2 = System.nanoTime();
        } else {
            deltaTime2 = (currentFrame - lastFrame2) / 1000000;
            if (deltaTime2 < shootDelay) {
                animate(idle, 150, true);
                shootOnce = false;
            } else {
                if (!shootOnce) {
                    shootOnce = true;
                    frame = 0;
                }
                if (frame >= 2 && getWorld() != null) {
                    PlayScene.addObject(new Needle(getYPos()), getX() + 30, getY() - 8);
                    lastFrame2 = currentFrame;
                }
                animate(shoot, 150, false);
            }
        }
    }
}