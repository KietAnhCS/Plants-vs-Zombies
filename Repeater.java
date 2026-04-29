import greenfoot.*; 

public class Repeater extends Plant
{
    private GreenfootImage[] idle;
    private GreenfootImage[] shoot;
    private boolean shootOnce = false;
    private int shootCount = 0;
    private boolean resetFrame = false;
    private boolean shooting = false;
    private long shootDelay = PlantRegistry.REPEATER_SHOOT_DELAY;
    private long lastFrame2 = System.nanoTime();
    private long deltaTime2;
    
    public Repeater() {
        maxHp = PlantRegistry.REPEATER_HP;
        hp = maxHp;
        shoot = importSprites(PlantAssets.REPEATER_SHOOT, 3);
        idle = importSprites(PlantAssets.REPEATER_IDLE, 7);
    }
   
    public void hit(int dmg) {
        if (isLiving()) {
            if (!shootOnce) {
                hitFlash(idle, PlantAssets.REPEATER_IDLE);
            } else {
                hitFlash(shoot, PlantAssets.REPEATER_SHOOT);  
            }
            hp = hp - dmg;
        }
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
                if (shootCount >= 2) { // Repeater bắn 2 viên mỗi lượt
                    lastFrame2 = currentFrame;
                }
                if (!resetFrame) {
                    setFrame(1);
                    resetFrame = true;
                }
                
                if (frame >= 4) {
                    AudioManager.playSound(80, false, PlantAssets.SOUND_THROW, PlantAssets.SOUND_THROW2);
                    
                    if (getWorld() != null) {
                        world.addObject(new Pea(getYPos()), getX() + 25, getY() - 17);
                    }
                    
                    setFrame(1);
                    shootCount++;
                }
                animate(shoot, 70, false);
            }
        }

        checkZombieInRow(world);
    }

    private void checkZombieInRow(PlayScene world) {
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