import greenfoot.*;

public class PlantInputHandler {
    private Plant plant;
    private int startGridX, startGridY;

    public PlantInputHandler(Plant plant) {
        this.plant = plant;
    }

    public void handleMouse() {
        MouseInfo mouse = Greenfoot.getMouseInfo();

        if (mouse == null || plant.getState() == PlantState.DYING || plant.getState() == PlantState.MERGING) {
            return;
        }

        if (Greenfoot.mousePressed(plant) && mouse.getButton() == 1) {
            World world = plant.getWorld();
            if (!(world instanceof PlayScene)) return;
            PlayScene scene = (PlayScene) world;

            int[] grid = scene.GridManager.getGridPos(plant.getX(), plant.getY());
            startGridX = grid[0];
            startGridY = grid[1];

            plant.isDragging = true;
            plant.setState(PlantState.DRAGGING);
        }

        if (plant.getState() == PlantState.DRAGGING) {
            if (Greenfoot.mouseDragged(null)) {
                plant.setLocation(mouse.getX(), mouse.getY());
            }
            if (Greenfoot.mouseClicked(null)) {
                processDrop();
            }
        }
    }

    private void processDrop() {
        World world = plant.getWorld();
        if (!(world instanceof PlayScene)) return;

        PlayScene scene = (PlayScene) world;
        int nx = scene.GridManager.getGridX(plant.getX(), plant.getY());
        int ny = scene.GridManager.getGridY(plant.getX(), plant.getY());

        plant.isDragging = false;

        if (nx >= 0 && ny >= 0 && scene.GridManager.placePlant(nx, ny, plant)) {
            plant.syncGridPosition();
            plant.setState(PlantState.IDLE);
        } else {
            returnToOldPosition(scene);
            plant.setState(PlantState.IDLE);
        }
    }

    private void returnToOldPosition(PlayScene scene) {
        if (startGridX >= 0 && startGridY >= 0) {
            int oldX = scene.GridManager.getXCoord(startGridX, startGridY);
            int oldY = scene.GridManager.getYCoord(startGridX, startGridY);
            plant.setLocation(oldX, oldY);
        }
    }
}