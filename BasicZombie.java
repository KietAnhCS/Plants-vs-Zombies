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
        
        this.walkSpeed = (Greenfoot.getRandomNumber(6) + 22) / 100.0;
        
        maxHp = 150;
        hp = maxHp;
        this.damage = 5; 
    }

    @Override
    public void update() {
        if (hp > 50) {
            handleMovement(walk, eat);
        } else {
            if (!fallen) {
                fallen = true;
                AudioManager.playSound(80, false, "limbs_pop.mp3");
                if (getWorld() != null) {
                    getWorld().addObject(new Arm(), getX() + 8, getY() + 20);
                }
            }
            handleMovement(armless, armlesseat);
        }
    }
    
    private void handleMovement(GreenfootImage[] walkAnim, GreenfootImage[] eatAnim) {
        if (!isEating()) {
            animate(walkAnim, 350, true);   
            move(-walkSpeed);
        } else {
            animate(eatAnim, 200, true);
            playEating(); 
        }
    }
   
    @Override
    public void hit(int dmg) {
        if (!isAlive) return;
        
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