import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Pea-nut: A hybrid of Walnut and Peashooter.
 * Fires peas and changes appearance as it takes damage.
 */
public class PeaNut extends Plant
{
    private GreenfootImage[] idle;
    private GreenfootImage[] d;     // Damaged
    private GreenfootImage[] dd;    // Critically Damaged
    private GreenfootImage[] shoot; // Shooting animation (Healthy)
    
    private boolean shootOnce = false;
    private boolean shooting = false;
    private long shootDelay = 17L; 
    private long lastFrame2 = System.nanoTime();
    private long deltaTime2;

    public PeaNut() {
        // High HP like a Walnut
        maxHp = 730;
        hp = maxHp;
        
        // Import all sprite states
        idle = importSprites("walnut", 5);     // Use walnut idle as base
        d = importSprites("walnutd", 5);       // Damaged walnut
        dd = importSprites("walnutdd", 5);     // Cracked walnut
        shoot = importSprites("walnut", 3); // Shooting animation
        
        setImage(idle[0]);
    }

    @Override
    public void hit(int dmg) {
        if (getWorld() != null && isLiving()) {
            // Logic to decide which flash animation to show based on HP
            if (hp >= 480) {
                hitFlash(idle, "walnut");
            } else if (hp >= 240) {
                hitFlash(d, "walnutd");
            } else {
                hitFlash(dd, "walnutdd");
            }
            hp = hp - dmg;
        }
    }

    @Override
    public void update() {
        // Safety Check 1: Stop if already removed
        if (getWorld() == null) return;
        
        MyWorld = (MyWorld)getWorld();
        currentFrame = System.nanoTime();

        // Handle the hybrid logic
        handleAnimationAndShooting();

        // Safety Check 2: Check again before logic
        if (getWorld() == null) return;

        checkZombieInRow();
    }

    private void handleAnimationAndShooting() {
        // Pick the correct animation set based on health
        GreenfootImage[] currentIdleSet;
        if (hp >= 480) currentIdleSet = idle;
        else if (hp >= 240) currentIdleSet = d;
        else currentIdleSet = dd;

        if (!shooting) {
            animate(currentIdleSet, 10, true);
            lastFrame2 = System.nanoTime();
        } else {
            deltaTime2 = (currentFrame - lastFrame2) / 1000000;
            if (deltaTime2 < shootDelay) {
                animate(currentIdleSet, 1, true);
                shootOnce = false;
            } else {
                if (!shootOnce) {
                    shootOnce = true;
                    frame = 0;
                }
                
                // Fire the projectile
                if (frame >= 3) {
                    if (getWorld() != null) {
                        AudioPlayer.play(80, "throw.mp3", "throw2.mp3");
                        MyWorld.addObject(new FirePea(getYPos()), getX() + 25, getY() - 17);
                        lastFrame2 = currentFrame;
                    }
                }
                // Note: You can swap 'shoot' with damaged shooting sprites if you have them
                animate(shoot, 70, false);
            }
        }
    }

    private void checkZombieInRow() {
        int myRow = getYPos();
        if (MyWorld.level.zombieRow.get(myRow).isEmpty()) {
            shooting = false;
        } else {
            boolean found = false;
            for (Zombie i : MyWorld.level.zombieRow.get(myRow)) {
                if (i.getWorld() != null && i.getX() > getX() && i.getX() <= MyWorld.getWidth() + 10) {
                    found = true;
                    break;
                }
            }
            shooting = found;
        }
    }
}