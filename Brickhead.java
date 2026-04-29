import greenfoot.*; 

public class Brickhead extends Zombie
{
    private boolean brick = true;
    public GreenfootImage[] walk, armless, eat, armlesseat;
    public GreenfootImage[] brickheadwalk, brickheadwalkd, brickheadwalkdd;
    public GreenfootImage[] brickheadeat, brickheadeatd, brickheadeatdd;
    
    public Brickhead() {
        super(); 
        walk = importSprites("zombiewalk", 7);
        eat = importSprites("zombieeating", 7);
        armlesseat = importSprites("armlesszombieeating", 7);
        armless = importSprites("armlesszombie", 7);
        brickheadwalk = importSprites("brickhead", 7);
        brickheadwalkd = importSprites("brickheadd", 7);
        brickheadwalkdd = importSprites("brickheaddd", 7);
        brickheadeat = importSprites("brickheadeat", 7);
        brickheadeatd = importSprites("brickheadeatd", 7);
        brickheadeatdd = importSprites("brickheadeatdd", 7);
        
        walkSpeed = Random.Double(22, 28);
        maxHp = 300;
        hp = maxHp;
        this.damage = 150; 
    }

    @Override
    public void update() {
        if (hp > 200) {
            handleAnimation(brickheadwalk, brickheadeat);
        } 
        else if (hp > 100) {
            handleAnimation(brickheadwalkd, brickheadeatd);
        } 
        else if (hp > 50) {
            handleAnimation(brickheadwalkdd, brickheadeatdd);
        } 
        else {
            if (brick) {
                brick = false;
                if (PlayScene != null) PlayScene.addObject(new Brick(), getX(), getY() - 25);
            }

            if (hp > 25) {
                handleAnimation(walk, eat);
            } 
            else {
                if (!fallen) {
                    fallen = true;
                    AudioManager.playSound(80, false, "limbs_pop.mp3");
                    if (PlayScene != null) PlayScene.addObject(new Arm(), getX() + 8, getY() + 20);
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
        AudioManager.playSound(80, false, "splat.mp3", "splat2.mp3", "splat3.mp3");

        if (isLiving()) {
            if (hp > 200) {
                hitFlash(eating ? brickheadeat : brickheadwalk, eating ? "brickheadeat" : "brickhead");
            } else if (hp > 100) {
                hitFlash(eating ? brickheadeatd : brickheadwalkd, eating ? "brickheadeatd" : "brickheadd");
            } else if (hp > 50) {
                hitFlash(eating ? brickheadeatdd : brickheadwalkdd, eating ? "brickheadeatdd" : "brickheaddd");
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