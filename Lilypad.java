import greenfoot.*;

public class Lilypad extends Plant
{
    private GreenfootImage[] idle;
    
    public static final int Y_OFFSET = 30; 

    public Lilypad() {
        maxHp = 100; 
        hp = maxHp;
        idle = importSprites("LilyPad", 1); 
        setImage(idle[0]);
    }
   
    @Override
    public void hit(int dmg) {
        
        if (!isOccupied() && isLiving()) {
            hitFlash(idle, "LilyPad");
            hp = hp - dmg;
        }
    }

    @Override
    public void update() {
        if (getWorld() == null) return;
        PlayScene = (PlayScene)getWorld();

        
        ensureLayering();
        
        animate(idle, 200, true);

        if (hp <= 0) {
            removePlantOnTop();
            getWorld().removeObject(this);
        }
    }

    private void ensureLayering() {
       
    }

    public Plant getPlantOnTop() {
        Plant p = (Plant) getOneIntersectingObject(Plant.class);
        if (p != null && !(p instanceof Lilypad)) {
            return p;
        }
        return null;
    }

    public boolean isOccupied() {
        return getPlantOnTop() != null;
    }

    private void removePlantOnTop() {
        Plant p = getPlantOnTop();
        if (p != null) {
            getWorld().removeObject(p);
        }
    }
}