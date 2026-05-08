import greenfoot.*;

public class PlantStateManager {
    private Plant plant;
    private PlantState currentState;

    public PlantStateManager(Plant plant) {
        this.plant = plant;
        this.currentState = PlantState.IDLE;
    }

    public void update() {
        if (plant.getWorld() == null) return;

        if (plant.getHp() <= 0 && currentState != PlantState.DYING) {
            setState(PlantState.DYING);
            return;
        }

        handleStateTransitions();
    }

    private void handleStateTransitions() {
    }

    public boolean isBusy() {
        return currentState == PlantState.DRAGGING || 
               currentState == PlantState.MERGING || 
               currentState == PlantState.DYING;
    }

    public PlantState getCurrentState() {
        return currentState;
    }

    public void setState(PlantState newState) {
        if (this.currentState == newState) return;
        
        this.currentState = newState;
        updatePlantAppearance();
    }

    private void updatePlantAppearance() {
        GreenfootImage img = plant.getImage();
        if (img == null) return;
        
        if (currentState == PlantState.DRAGGING) {
            img.setTransparency(125);
        } else {
            img.setTransparency(255);
        }
    }

    public boolean canAct() {
        if (plant.getWorld() == null) return false;
        
        PlayScene scene = (PlayScene) plant.getWorld();
        boolean hasOverlay = !scene.getObjects(Overlay.class).isEmpty();
        
        boolean isOnBench = plant.getYPos() == 5;

        return !isBusy() && !hasOverlay && !isOnBench && plant.getHp() > 0;
    }
}