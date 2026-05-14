import greenfoot.*;
import java.util.List;

public class PianoZombie extends Zombie {
    public GreenfootImage[] wNormal, wDamaged, wCritical, pianoDeath;
    private boolean fallen = false;
    private boolean isCrushed = false;
    
    private GreenfootSound pianoSound = new GreenfootSound("pianozombiemusic.mp3");
    private int danceTimer = 0;
    private static final int DANCE_INTERVAL = 250; 

    public PianoZombie() {
        super(ZombieConfig.PIANO);
        this.walkSpeed = (Greenfoot.getRandomNumber(6) + 22) / 100.0;

        wNormal    = importSprites(ZombieAssets.PIANO_WALK.path,    ZombieAssets.PIANO_WALK.count, 0.45);
        wDamaged   = importSprites(ZombieAssets.PIANO_WALK_D1.path, ZombieAssets.PIANO_WALK_D1.count, 0.45);
        wCritical  = importSprites(ZombieAssets.PIANO_WALK_D2.path, ZombieAssets.PIANO_WALK_D2.count, 0.45);
        pianoDeath = importSprites(ZombieAssets.PIANO_DEATH.path,   ZombieAssets.PIANO_DEATH.count, 0.45);

        pianoSound.setVolume(30); 
        setState(new WalkingState(this));
    }

    @Override
    public void addedToWorld(World world) {
        super.addedToWorld(world);
        pianoSound.playLoop();
    }

    @Override
    public void act() {
        if (getWorld() == null) return;
        if (!getWorld().getObjects(Overlay.class).isEmpty()) {
            if (pianoSound.isPlaying()) pianoSound.pause();
            return;
        } else {
            if (!pianoSound.isPlaying() && isLiving()) pianoSound.playLoop();
        }

        if (isLiving()) {
            handleCrushing();
            handleThresholds();
            makeZombiesDance();
            handleSliding();
            
            this.eating = false;
            walk();
            animate(getCurrentAnimation(false), 80, true);
        } else {
            deathAnim();
        }
    }

    @Override
    protected void handleThresholds() {
        if (getHp() <= 20 && !fallen) {
            fallen = true;
            if (getWorld() != null) {
                getWorld().addObject(new Arm(), getX(), getY());
            }
        }
    }

    @Override
    public void hit(int dmg) {
        if (getHp() <= 0 && !isLiving() && finalDeath) return;
        
        if (isLiving()) {
            ZombieAssets asset;
            if (getHp() > 800) asset = ZombieAssets.PIANO_WALK;
            else if (getHp() > 400) asset = ZombieAssets.PIANO_WALK_D1;
            else asset = ZombieAssets.PIANO_WALK_D2;
            
            hitFlash(asset.path);
        }
        super.hit(dmg);
    }

    @Override
    public GreenfootImage[] getCurrentAnimation(boolean isEating) {
        if (getHp() > 800) return wNormal;
        if (getHp() > 400) return wDamaged;
        return wCritical;
    }

    private void handleCrushing() {
        int[] offsets = {10, 25, 40}; 
        for (int xOffset : offsets) {
            Plant p = (Plant) getOneObjectAtOffset(xOffset, 0, Plant.class);
            if (p != null) {
                p.hit(9999);
                break; 
            }
        }
    }

    private void makeZombiesDance() {
        danceTimer++;
        if (danceTimer >= DANCE_INTERVAL) {
            danceTimer = 0;
            List<Zombie> nearbyZombies = getObjectsInRange(250, Zombie.class);
            for (Zombie z : nearbyZombies) {
                if (z != this && z.isLiving()) {
                    int direction = (Greenfoot.getRandomNumber(2) == 0) ? -90 : 90;
                    int potentialY = z.getY() + direction;
                    if (potentialY >= 100 && potentialY <= 370) { 
                        z.forceChangeLane(potentialY); 
                    } else {
                        z.forceChangeLane(z.getY() - direction);
                    }
                }
            }
        }
    }

    @Override
    protected void deathAnim() {
        if (pianoSound.isPlaying()) pianoSound.stop();
        
        if (!resetAnim) { 
            frame = 0; 
            resetAnim = true; 
            removeFromRow();
            eventBus.publishDeath(this);
            target = null;
            eating = false;
        }

        if (!isCrushed) {
            if (animate(pianoDeath, 75, false) || frame >= pianoDeath.length - 1) {
                isCrushed = true;
                finalDeath = true;
            }
        } else {
            if (getWorld() != null) {
                getWorld().removeObject(this);
            }
        }
    }
}