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

        if (plant.hp <= 0) {
            currentState = PlantState.DYING;
            return;
        }

        if (plant.isMerging || plant.isDragging) {
            currentState = PlantState.IDLE;
            return;
        }

        currentState = PlantState.IDLE;
    }

    public boolean isBusy() {
        return plant.isDragging || plant.isMerging || currentState == PlantState.DYING;
    }

    public PlantState getCurrentState() {
        return currentState;
    }

    public void setState(PlantState state) {
        this.currentState = state;
    }

    public boolean canAct() {
        if (plant.playScene == null || plant.getWorld() == null) return false;
        
        boolean hasOverlay = !plant.playScene.getObjects(Overlay.class).isEmpty();
        
        String className = plant.getClass().getSimpleName().toUpperCase();
        boolean isPotatoOnBench = className.contains("POTATOMINE") && plant.getYPos() == 5;

        return !isBusy() && !hasOverlay && !isPotatoOnBench;
    }
}