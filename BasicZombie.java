import greenfoot.*; 

public class BasicZombie extends Zombie
{
    public GreenfootImage[] idle;
    public GreenfootImage[] walk;
    public GreenfootImage[] armless;
    public GreenfootImage[] eat;
    public GreenfootImage[] armlesseat;
    
    public BasicZombie() {
        
        super(); 
        
        walk = importSprites("zombiewalk", 7);
        eat = importSprites("zombieeating", 7);
        armlesseat = importSprites("armlesszombieeating", 7);
        armless = importSprites("armlesszombie", 7);
        
        walkSpeed = Random.Double(22, 28);
        maxHp = 150;
        hp = maxHp;
        
        this.damage = 5; 
    }

    @Override
    public void update() {
        
        if (hp > 50) {
            if (!isEating()) {
                animate(walk, 350, true);   
                move(-walkSpeed);
            } else {
                animate(eat, 200, true);
                playEating(); 
            }
        } else {
           
            if (!fallen) {
                fallen = true;
                AudioManager.playSound(80, false, "limbs_pop.mp3");
                if (PlayScene != null) {
                    PlayScene.addObject(new Arm(), getX() + 8, getY() + 20);
                }
            }
            
            if (!isEating()) {
                animate(armless, 350, true);
                move(-walkSpeed);
            } else {
                animate(armlesseat, 200, true); 
                playEating();
            }
        }
    }
   
    @Override
    public void hit(int dmg) {
        AudioManager.playSound(80, false, "splat.mp3", "splat2.mp3", "splat3.mp3");
        
        if (isLiving()) {
            if (!fallen) {
                hitFlash(eating ? eat : walk, eating ? "zombieeating" : "zombiewalk");
            } else {
                hitFlash(eating ? armlesseat : armless, eating ? "armlesszombieeating" : "armlesszombie");
            }
        } else if (!finalDeath) {
            hitFlash(eating ? headlesseating : headless, eating ? "headlesszombieeating" : "zombieheadless");
        }
        
        super.hit(dmg); 
    }
}