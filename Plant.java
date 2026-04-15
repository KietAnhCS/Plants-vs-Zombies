import greenfoot.*;

public class Plant extends animatedObject {
    public int maxHp;
    public boolean isAlive = true;
    public int hp;
    public int damage;
    public boolean opaque = false;
    public PlayScene PlayScene;

    public Plant() {}

    public void act() {
        if (getWorld() == null) return;

        if (!getWorld().getObjects(Overlay.class).isEmpty()) return; 
        
        if (isLiving()) {
            update(); 
            if (getWorld() == null) return;

            if (!opaque) {
                getImage().setTransparency(255);
            } else {
                getImage().setTransparency(125);
            }
        } else {
            die();
        }
    }

        public void die() {
        
        World world = getWorld(); 
        if (world != null) {
            PlayScene = (PlayScene)world;
            AudioPlayer.play(80, "gulp.mp3");
            
            int xPos = getXPos();
            int yPos = getYPos();
            
            if (PlayScene.board != null) {
                PlayScene.board.removePlant(xPos, yPos);
            }
           
            world.removeObject(this);
        }
    }

    public void update() {}

    public int getXPos() {
        if (getWorld() == null) return -1; 
        return ((getX() - Board.xOffset) / Board.xSpacing);
    }   

    public int getYPos() {
        if (getWorld() == null) return -1;
        return ((getY() - Board.yOffset) / Board.ySpacing);
    }

    @Override
    public void addedToWorld(World world) {
        PlayScene = (PlayScene)world;
        
        world.addObject(new Dirt(), getX(), getY() + 30);
    }

    public boolean isLiving() {
        if (hp <= 0) isAlive = false;
        return isAlive && getWorld() != null;
    }

    public void hit(int dmg) {
        if (getWorld() == null) return;
        if (!getWorld().getObjects(Overlay.class).isEmpty()) return;
        hp -= dmg;
    }
}