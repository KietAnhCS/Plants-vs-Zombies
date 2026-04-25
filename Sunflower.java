import greenfoot.*; 
public class Sunflower extends Plant
{
    private int sunProductionTime = 30000;
    private GreenfootImage[] idle;
    private boolean test = false;
    private long lastFrame2 = System.nanoTime();
    private long deltaTime2;
    public Sunflower() {
        idle = importSprites("sunfloweridle", 8);
        maxHp = 60;
        hp = maxHp;
    }
    public void update(){
        produceSun();
        animate(idle, 200, true);

    }
    public void hit(int dmg) {
        if (isLiving()) {
            hitFlash(idle, "sunfloweridle");
            
            hp = hp-dmg;
        }
    }
    public void produceSun() {
    
    deltaTime2 = (System.nanoTime() - lastFrame2) / 1000000;
    
    
    if (deltaTime2 > sunProductionTime) {
        lastFrame2 = System.nanoTime(); 
        
        hitFlash(idle, "sunfloweridle");
        test = true;
        
        PlayScene.addObject(new Sun(25,false), getX(), getY() - 10);
    }
}
  
}
