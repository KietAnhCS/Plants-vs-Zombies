import greenfoot.*;

public class Buckethead extends Zombie
{
    private boolean bucket = true;
    public GreenfootImage[] walk, armless, eat, armlesseat;
    public GreenfootImage[] bucketheadwalk, bucketheadwalkd, bucketheadwalkdd;
    public GreenfootImage[] bucketheadeat, bucketheadeatd, bucketheadeatdd;
    
    public Buckethead() {
        super(); 
        walk = importSprites("zombiewalk", 7);
        eat = importSprites("zombieeating", 7);
        armlesseat = importSprites("armlesszombieeating", 7);
        armless = importSprites("armlesszombie", 7);
        bucketheadwalk = importSprites("bucketheadwalk", 7);
        bucketheadwalkd = importSprites("bucketheadwalkd", 7);
        bucketheadwalkdd = importSprites("bucketheadwalkdd", 7);
        bucketheadeat = importSprites("bucketheadeat", 7);
        bucketheadeatd = importSprites("bucketheadeatd", 7);
        bucketheadeatdd = importSprites("bucketheadeatdd", 7);
        
        this.walkSpeed = (Greenfoot.getRandomNumber(6) + 22) / 100.0;
        
        maxHp = 1100; 
        hp = maxHp;
        this.damage = 20;
    }

    @Override
    public void update() {
        if (hp > 800) {
            handleAnimation(bucketheadwalk, bucketheadeat);
        } 
        else if (hp > 500) {
            handleAnimation(bucketheadwalkd, bucketheadeatd);
        } 
        else if (hp > 200) {
            handleAnimation(bucketheadwalkdd, bucketheadeatdd);
        } 
        else {
            if (bucket) {
                bucket = false;
                AudioManager.playSound(80, false, "shield_break.mp3"); 
                if (getWorld() != null) getWorld().addObject(new Bucket(), getX(), getY() - 25);
            }

            if (hp > 100) {
                handleAnimation(walk, eat);
            } 
            else {
                if (!fallen) {
                    fallen = true;
                    AudioManager.playSound(80, false, "limbs_pop.mp3");
                    if (getWorld() != null) getWorld().addObject(new Arm(), getX() + 8, getY() + 20);
                }
                handleAnimation(armless, armlesseat);
            }
        }
    }

    private void handleAnimation(GreenfootImage[] walkAnim, GreenfootImage[] eatAnim) {
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

        if (hp > 200) {
            AudioManager.playSound(80, false, "shieldhit.mp3", "shieldhit2.mp3");
        } else {
            AudioManager.playSound(80, false, "splat.mp3", "splat2.mp3");
        }

        if (isLiving()) {
            if (hp > 800) {
                hitFlash(eating ? bucketheadeat : bucketheadwalk, eating ? "bucketheadeat" : "bucketheadwalk");
            } else if (hp > 500) {
                hitFlash(eating ? bucketheadeatd : bucketheadwalkd, eating ? "bucketheadeatd" : "bucketheadwalkd");
            } else if (hp > 200) {
                hitFlash(eating ? bucketheadeatdd : bucketheadwalkdd, eating ? "bucketheadeatdd" : "bucketheadwalkdd");
            } else if (!fallen) {
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