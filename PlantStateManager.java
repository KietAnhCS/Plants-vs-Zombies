import greenfoot.*;
import java.util.List;

public class PlantStateManager {
    private final Plant plant;

    public PlantStateManager(Plant plant) {
        this.plant = plant;
    }

    public void update() {
        if (plant.getWorld() == null) return;

        if (plant.getHp() <= 0 && plant.getState() != PlantState.DYING) {
            plant.setState(PlantState.DYING);
        }
    }

    public boolean isBusy() {
        PlantState s = plant.getState();
        return s == PlantState.DRAGGING || 
               s == PlantState.MERGING || 
               s == PlantState.DYING;
    }

    public boolean canAct() {
        World world = plant.getWorld();
        
        if (world == null || plant.getHp() <= 0 || isBusy()) {
            return false;
        }

        return world.getObjects(Overlay.class).isEmpty();
    }
}