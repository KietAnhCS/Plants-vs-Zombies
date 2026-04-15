import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Plant here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Plant extends animatedObject
{
    public int maxHp;
    public boolean isAlive = true;
    public int hp;
    public int damage;
    public boolean opaque = false;
    public PlayScene PlayScene;
 
    public Plant() {
        
    }
    /**
     * Act - do whatever the Plant wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        if (getWorld() != null) {
            if (isLiving()) {
                update();    
                if (!opaque) {
                    getImage().setTransparency(255);
                } else {
                    getImage().setTransparency(125);
                }
            } else {
                PlayScene = (PlayScene)getWorld();
                AudioPlayer.play(80,"gulp.mp3");
                
                PlayScene.board.removePlant(getXPos(), getYPos());
                PlayScene.removeObject(this);
                
                return;
            } 
        }
    
    }
    public void update() {
        
    }
    
    
    public void activatePlantFood() {
                                 
        this.hp = maxHp;                                  
                                 
    }
    public int getXPos() {
        return ((getX()-Board.xOffset)/Board.xSpacing);
    }   
    public int getYPos() {
        return ((getY()-Board.yOffset)/Board.ySpacing);
    }
    @Override
    public void addedToWorld(World world) {
        PlayScene = (PlayScene)getWorld();
        PlayScene.addObject(new Dirt(), getX(), getY()+30);
    }
    public boolean isLiving() {
        if (hp <=0) {
            isAlive = false;
        } else {
            isAlive = true;
        }
        return isAlive;
    }
    public void hit(int dmg) {
        
    }
    
}
