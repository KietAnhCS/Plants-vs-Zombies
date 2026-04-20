import greenfoot.*;  

public class Plant extends animatedObject
{
    public int cost;
    public int maxHp;
    public boolean isAlive = true;
    public int hp;
    public int damage;
    public boolean opaque = false;
    public PlayScene PlayScene;

    private boolean isDragging = false;
    private int startGridX, startGridY;

    public Plant() {
    }

    public void act()
    {
        if (getWorld() == null) return;
        handleMouse();

        if (isLiving()) {
            update();    
            
            if (getWorld() != null && getImage() != null) {
                if (isDragging) {
                    getImage().setTransparency(125);
                } else if (!opaque) {
                    getImage().setTransparency(255);
                } else {
                    getImage().setTransparency(125);
                }
            }
        } else {
            PlayScene = (PlayScene)getWorld();
            AudioPlayer.play(80, "gulp.mp3");
            
            int x = getXPos();
            int y = getYPos();
            
            if (PlayScene.board != null) {
                PlayScene.board.removePlant(x, y);
            }
            PlayScene.removeObject(this);
            return;
        }
    }

    private void handleMouse() {
        MouseInfo mouse = Greenfoot.getMouseInfo();
    
        if (Greenfoot.mousePressed(this)) {
            if (mouse != null && mouse.getButton() == 1) { 
                isDragging = true;
                startGridX = getXPos();
                startGridY = getYPos();
                if (PlayScene != null) {
                    PlayScene.setPaintOrder(Plant.class, Board.class);
                }
            }
        }
    
        if (isDragging && Greenfoot.mouseDragged(this)) {
            if (mouse != null) {
                
                setLocation(mouse.getX(), mouse.getY());
            }
        }
    
        if (isDragging && Greenfoot.mouseDragEnded(this)) {
            isDragging = false;
            
            int newGridX = getXPos(); 
            int newGridY = getYPos(); 
    
            if (PlayScene.board != null && PlayScene.board.movePlant(startGridX, startGridY, newGridX, newGridY, this)) {
                
                setLocation(PlayScene.board.getXCoord(newGridX, newGridY), PlayScene.board.getYCoord(newGridX, newGridY));
            } else {
                
                setLocation(PlayScene.board.getXCoord(startGridX, startGridY), PlayScene.board.getYCoord(startGridX, startGridY));
            }
        }
    }

    public void update() {
    }
    
    public void activatePlantFood() {                                     
        this.hp = maxHp;                                            
    }

    public int getXPos() {
        if (getWorld() == null || PlayScene.board == null) return 0;
        Board b = PlayScene.board;
        
        double det = (double)(b.xSpacing * b.ySpacing) - (b.rowDeltaX * b.colDeltaY);
        double dx = getX() - b.xOffset;
        double dy = getY() - b.yOffset;
        
        int x = (int)Math.round((dx * b.ySpacing - dy * b.rowDeltaX) / det);
        
        if (x < 0) return 0;
        if (x > 8) return 8;
        return x;
    }

    public int getYPos() {
        if (getWorld() == null || PlayScene.board == null) return 0;
        Board b = PlayScene.board;
        
        double det = (double)(b.xSpacing * b.ySpacing) - (b.rowDeltaX * b.colDeltaY);
        double dx = getX() - b.xOffset;
        double dy = getY() - b.yOffset;
        
        int y = (int)Math.round((b.xSpacing * dy - b.colDeltaY * dx) / det);
        
        if (y < 0) return 0;
        if (y > 4) return 4; 
        return y;
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