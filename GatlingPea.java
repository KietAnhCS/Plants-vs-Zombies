import greenfoot.*; 

public class GatlingPea extends Plant
{
    private GreenfootImage[] idle;
    private GreenfootImage[] shoot;
    private int shootCount = 0;
    private boolean resetFrame = false;
    private boolean shooting = false;
    private long shootDelay = 800L;
    private long lastFrame2 = System.nanoTime();
    private long deltaTime2;
    
    public GatlingPea() {
        maxHp = 60;
        hp = maxHp;
        idle = importSprites("GatlingPea", 19);
        shoot = importSprites("GatlingPea", 19);
        setImage(idle[0]);
    }
   
    public void hit(int dmg) {
        if (isLiving()) {
            hitFlash(shooting ? shoot : idle, "GatlingPea");
            hp -= dmg;
        }
    }

    public void update() {
        if (getWorld() == null) return;
        
        PlayScene world = (PlayScene)getWorld();
        currentFrame = System.nanoTime();

        checkForZombies(world);

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
                if (shootCount >= 7) {
                    lastFrame2 = currentFrame;
                    return;
                }

                if (!resetFrame) {
                    frame = 0;
                    resetFrame = true;
                }
                
                if (frame >= 4) {
                    AudioManager.playSound(80, false, "throw.mp3", "throw2.mp3");
                    world.addObject(new Pea(getYPos()), getX() + 25, getY() - 17);
                    frame = 0; 
                    shootCount++;
                }
                
                if (frame < 0) frame = 0; 
                animate(shoot, 30, false);
            }
        }
    }

    private void checkForZombies(PlayScene world) {
        java.util.List<Zombie> zombiesInRow = world.level.zombieRow.get(getYPos());
        
        if (zombiesInRow == null || zombiesInRow.isEmpty()) {
            shooting = false;
            return;
        }

        boolean foundZombie = false;
        for (Zombie z : zombiesInRow) {
            if (z != null && z.getWorld() != null && z.getX() > getX() && z.getX() <= world.getWidth() + 10) {
                foundZombie = true;
                break;
            }
        }
        shooting = foundZombie;
    }
}