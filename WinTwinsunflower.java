import greenfoot.*; 

public class WinTwinsunflower extends FallingObject
{
    
    public GreenfootImage[] sprites = importSprites("stwinsunflowerpacket", 2);
    public boolean move = false;    
    public boolean addTrans = false;
    
    public WinTwinsunflower() {
        super(-3, 0.15, Random.Double(-100, 100), 0, 750L);
    }
    public void update() {
        
        
        currentFrame = System.nanoTime();
        deltaTime = (currentFrame - lastFrame) / 1000000;
        if (move) {
            setImage("stwinsunflowerpacket1.png");
            
            
            if (getX() < (getWorld().getWidth()/2-5)) {
                move(2);
            } else if (getX() > (getWorld().getWidth()/2+5)){
                move(-2);
            } else {
                if (!addTrans) {
                    addTrans=true;
                    getWorld().addObject(new Transition(false, new Level3Preview(), "whitetransition.png", 1), 360, 215);
                    AudioPlayer.play(70, "lightfill.mp3");
                }
            }
        } else {
            animate(sprites, 200);
        }
        if (deltaTime < fallTime) {
            double x = getExactX()+hSpeed;
            double y = getExactY()+vSpeed;
            setLocation(x,y);
            
            turn(rotate);
            
            vSpeed = vSpeed + acceleration;
        } else {
            
        }
        
        if (Greenfoot.mouseClicked(this) && !move) {
            
            move = true;
            ((PlayScene)getWorld()).finishLevel();
        }
    }
    
}
