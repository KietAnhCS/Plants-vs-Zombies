import greenfoot.*;

public class PlantInputHandler {
    private Plant plant;
    private int startX, startY;

    public PlantInputHandler(Plant plant) { this.plant = plant; }

    public void handleMouse() {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse == null) return;

        if (Greenfoot.mousePressed(plant)) {
            if (plant.isMerging) return;
            plant.isDragging = true;
            startX = plant.getXPos();
            startY = plant.getYPos();
        }

        if (plant.isDragging) {
            if (Greenfoot.mouseDragged(null)) plant.setLocation(mouse.getX(), mouse.getY());

            if (Greenfoot.mouseClicked(null)) {
                plant.isDragging = false;
                processDrop();
            }
        }
    }

    private void processDrop() {
        PlayScene scene = plant.playScene;
        int nx = scene.GridManager.getGridX(plant.getX(), plant.getY());
        int ny = scene.GridManager.getGridY(plant.getX(), plant.getY());

        if (nx >= 0 && ny >= 0 && scene.GridManager.placePlant(nx, ny, plant)) {
            plant.setGridPosition(nx, ny);
            scene.checkAndCombine(plant); 
        } else {
            plant.setLocation(scene.GridManager.getXCoord(startX, startY), 
                              scene.GridManager.getYCoord(startX, startY));
        }
    }
}