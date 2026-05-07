import greenfoot.*;
import java.util.List;

public class PianoZombie extends Zombie {
    public GreenfootImage[] wNormal, wDamaged, wCritical, pianoDeath;
    private boolean isCrushed = false;
    
    private static final int SCALE_WIDTH = 110;
    private static final int SCALE_HEIGHT = 110;

    private int danceTimer = 0;
    private static final int DANCE_INTERVAL = 250; 

    public PianoZombie() {
        super(ZombieConfig.PIANO);
        this.walkSpeed = (Greenfoot.getRandomNumber(6) + 22) / 100.0;

        wNormal    = importSprites("Piano_Playing", 30);        
        wDamaged   = importSprites("Piano-Damaged", 30);        
        wCritical  = importSprites("Piano_Playing_Damaged", 30); 
        pianoDeath = importSprites("Piano_Death", 29);          

        this.currentState = new ArmoredZombieState(
            this,
            config.thresholds,
            new GreenfootImage[][] { wNormal, wDamaged, wCritical, wCritical, wCritical },
            new GreenfootImage[][] { wNormal, wDamaged, wCritical, wCritical, wCritical } 
        );
    }

    @Override
    public void act() {
        if (getWorld() == null) return;
        if (!getWorld().getObjects(Overlay.class).isEmpty()) return;

        if (isLiving()) {
            handleCrushing();
            updatePianoLogic();  
            handleThresholds();
            makeZombiesDance();
            handleAllZombiesSliding();
        } else {
            deathAnim();
        }
    }

    private void handleAllZombiesSliding() {
        List<Zombie> allZombies = getWorld().getObjects(Zombie.class);
        int slideSpeed = 4;

        for (Zombie z : allZombies) {
            if (z.targetY != -1) {
                z.removeFromRow(); 

                if (Math.abs(z.getY() - z.targetY) <= slideSpeed) {
                    z.setLocation(z.getX(), z.targetY);
                    z.targetY = -1; 
                    
                    int newRow = z.getYPos();
                    if (z.playScene != null && z.playScene.level != null) {
                        if (newRow >= 0 && newRow < z.playScene.level.zombieRow.size()) {
                            if (!z.playScene.level.zombieRow.get(newRow).contains(z)) {
                                z.playScene.level.zombieRow.get(newRow).add(z);
                            }
                        }
                    }
                } else {
                    int step = (z.targetY > z.getY()) ? slideSpeed : -slideSpeed;
                    z.setLocation(z.getX(), z.getY() + step);
                }
            }
        }
    }

    private void makeZombiesDance() {
        danceTimer++;
        if (danceTimer >= DANCE_INTERVAL) {
            danceTimer = 0;
            List<Zombie> nearbyZombies = getObjectsInRange(250, Zombie.class);
            for (Zombie z : nearbyZombies) {
                if (z != this && z.isLiving() && z.targetY == -1) {
                    int direction = (Greenfoot.getRandomNumber(2) == 0) ? -90 : 90;
                    int potentialY = z.getY() + direction;

                    if (potentialY >= 100 && potentialY <= 370) { 
                        z.targetY = potentialY; 
                    } else {
                        z.targetY = z.getY() - direction; 
                        
                        if (z.targetY < 100 || z.targetY > 370) {
                            z.targetY = -1;
                        }
                    }
                }
            }
        }
    }

    private void handleCrushing() {
        int[] offsets = {10, 25, 40}; 
        for (int xOffset : offsets) {
            Plant p = (Plant) getOneObjectAtOffset(xOffset, 0, Plant.class);
            if (p != null) {
                if (p instanceof PotatoMine) {
                    this.hit(this.hp); 
                    p.hit(9999); 
                    AudioManager.playSound(90, false, "piano_smash.mp3");
                } else {
                    p.hit(9999); 
                    AudioManager.playSound(80, false, "plant_crush.mp3");
                }
                break; 
            }
        }
    }

    private void updatePianoLogic() {
        if (currentState != null) {
            currentState.update();
            this.eating = false;
            walk();
            animate(currentState.getAnimation(), 80, true);
            if (getImage() != null) getImage().scale(SCALE_WIDTH, SCALE_HEIGHT);
        }
    }

    @Override
    protected void handleThresholds() {
        if (hp <= 20 && !fallen) {
            fallen = true;
            AudioManager.playSound(90, false, "piano_smash.mp3");
            if (getWorld() != null) getWorld().addObject(new Arm(), getX(), getY());
        }
    }

    @Override
    public void hit(int dmg) {
        if (!isAlive) return;
        AudioManager.playSound(80, false, "piano_hit.mp3");
        if (isLiving()) {
            if (hp > 800) hitFlash(wNormal, "Piano_Playing");
            else if (hp > 400) hitFlash(wDamaged, "Piano-Damaged");
            else hitFlash(wCritical, "Piano_Playing_Damaged");
        }
        super.hit(dmg);
    }
    
    @Override
    public void deathAnim() {
        if (!resetAnim) { frame = 0; resetAnim = true; }
        if (!isCrushed) {
            animate(pianoDeath, 75, false);
            if (getImage() != null) getImage().scale(SCALE_WIDTH, SCALE_HEIGHT);
            if (frame >= pianoDeath.length - 1) {
                isCrushed = true;
                finalDeath = true;
                removeFromRow();
            }
        } else {
            if (getWorld() != null) getWorld().removeObject(this);
        }
    }
}