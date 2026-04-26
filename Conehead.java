import greenfoot.*; 

public class Conehead extends Zombie
{
    public boolean cone = true;
    public GreenfootImage[] walk, armless, eat, armlesseat;
    public GreenfootImage[] coneheadwalk, coneheadwalkd, coneheadwalkdd;
    public GreenfootImage[] coneheadeat, coneheadeatd, coneheadeatdd;
    
    public Conehead() {
        super();
        walk = importSprites("zombiewalk", 7);
        eat = importSprites("zombieeating", 7);
        armlesseat = importSprites("armlesszombieeating", 7);
        armless = importSprites("armlesszombie", 7);
        
        coneheadwalk = importSprites("coneheadwalk", 7);
        coneheadwalkd = importSprites("coneheadwalkd", 7);
        coneheadwalkdd = importSprites("coneheadwalkdd", 7);
        coneheadeat = importSprites("coneheadeat", 7);
        coneheadeatd = importSprites("coneheadeatd", 7);
        coneheadeatdd = importSprites("coneheadeatdd", 7);
        
        walkSpeed = Random.Double(22, 28);
        maxHp = 400;
        hp = maxHp;
        this.damage = 30;
    }

    @Override
    public void update() {
        if (hp > 232) {
            handleAnimation(coneheadwalk, coneheadeat);
        } else if (hp > 166) {
            handleAnimation(coneheadwalkd, coneheadeatd);
        } else if (hp > 100) {
            handleAnimation(coneheadwalkdd, coneheadeatdd);
        } else {
            if (cone) {
                cone = false;
                if (PlayScene != null) PlayScene.addObject(new Cone(), getX(), getY() - 25);
            }
            
            if (hp > 50) {
                handleAnimation(walk, eat);
            } else {
                if (!fallen) {
                    fallen = true;
                    AudioPlayer.play(80, "limbs_pop.mp3");
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
        if (cone) {
            AudioPlayer.play(70, "plastichit.mp3", "plastichit2.mp3");
        }
        AudioPlayer.play(70, "splat.mp3", "splat2.mp3", "splat3.mp3");
        
        if (isLiving()) {
            if (hp > 232) {
                hitFlash(eating ? coneheadeat : coneheadwalk, eating ? "coneheadeat" : "coneheadwalk");
            } else if (hp > 166) {
                hitFlash(eating ? coneheadeatd : coneheadwalkd, eating ? "coneheadeatd" : "coneheadwalkd");
            } else if (hp > 100) {
                hitFlash(eating ? coneheadeatdd : coneheadwalkdd, eating ? "coneheadeatdd" : "coneheadwalkdd");
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