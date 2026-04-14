import greenfoot.*;

public class BossZombie extends Zombie
{

    public GreenfootImage[] walk;
    public GreenfootImage[] eat;

    public BossZombie() {
       
        walk = importSprites("BossZombie", 10);
        eat = importSprites("bosszomeat", 10);
        
        
        headless = importSprites("BossZombie", 10); 
        headlesseating = importSprites("bosszomeat", 10);
        fall = importSprites("BossZombie", 1); 

        
        maxHp = 200;
        hp = maxHp;
        walkSpeed = 0.18; 
        
        
        int w = 300;
        int h = 250;
        
        resizeArray(walk, w, h);
        resizeArray(eat, w, h);
        resizeArray(headless, w, h);
        resizeArray(headlesseating, w, h);
        resizeArray(fall, w, h);
        
        setImage(walk[0]);
    }

    @Override
    public void update() {
        if (!isEating()) {
         
            animate(walk, 350, true);   
            
           
            double bossSpeed = 0.67; 
            
           
            setLocation(getX() - bossSpeed, getY());
        } else {
            
            animate(eat, 150, true); 
            playEating(); 
        }
    }
    

    private void resizeArray(GreenfootImage[] imgs, int w, int h) {
        if (imgs != null) {
            for (GreenfootImage img : imgs) {
                if (img != null) img.scale(w, h);
            }
        }
    }
}