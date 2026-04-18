import greenfoot.*;  

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
        if (PlayScene == null || PlayScene.board == null) return (getX() - 290) / 82;
        double calcX = (double)(getX() - PlayScene.board.xOffset) / PlayScene.board.xSpacing;
        return (int)Math.round(calcX);
    }   

    public int getYPos() {
        if (PlayScene == null || PlayScene.board == null) return (getY() - 135) / 85;
        double calcY = (double)(getY() - PlayScene.board.yOffset) / PlayScene.board.ySpacing;
        return (int)Math.round(calcY);
    }

    @Override
    public void addedToWorld(World world) {
        PlayScene = (PlayScene)world;
        PlayScene.addObject(new Dirt(), getX(), getY() + 30);
    }

    public boolean isLiving() {
        return hp > 0;
    }

    public void hit(int dmg) {
        hp -= dmg;
    }
}