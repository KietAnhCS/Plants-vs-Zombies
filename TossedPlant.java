import greenfoot.*;

public class TossedPlant extends Actor {
    private Plant plantRef;
    private int endX, endY, gridX;
    private boolean out;
    private double t = 0; // Time factor from 0.0 to 1.0
    private double speed = 0.03; // Higher is faster flight

    // This Constructor MUST match the ExcavatorShovelState call exactly
    public TossedPlant(Plant p, int ex, int ey, int gx, boolean isOut) {
        this.plantRef = p;
        this.endX = ex;
        this.endY = ey;
        this.gridX = gx;
        this.out = isOut;
        
        // Use the plant's image for the flying visual
        GreenfootImage img = new GreenfootImage(p.getImage());
        img.setTransparency(200); 
        setImage(img);
    }

    public void act() {
        t += speed;
        if (t > 1.0) t = 1.0;

        int curX = (int)(getX() + (endX - getX()) * t);
        int curY = (int)(getY() + (endY - getY()) * t);
        
        int height = (int)(250 * Math.sin(Math.PI * t)); 
        
        setLocation(curX, curY - height);

        if (t >= 1.0) {
            land();
        }
    }

    private void land() {
        PlayScene scene = (PlayScene) getWorld();
        if (scene != null) {
            if (!out) {
                scene.addObject(plantRef, endX, endY);
                scene.GridManager.Board[5][gridX] = plantRef;
                plantRef.setGridPosition(gridX, 5);
                plantRef.setState(PlantState.IDLE);
                AudioManager.getInstance().playSound(80, false, "plant.mp3");
            } else {
                AudioManager.getInstance().playSound(80, false, "gulp.mp3");
            }
        }
        if (getWorld() != null) {
            getWorld().removeObject(this);
        }
    }
}