import greenfoot.*;

public class PlantDeathHandler {
    private Plant plant;

    public PlantDeathHandler(Plant plant) {
        this.plant = plant;
    }

    public void die() {
        World world = plant.getWorld();
        if (world == null) return;

        plant.setState(PlantState.DYING);
        AudioManager.getInstance().playSound(80, false, "gulp.mp3");

        if (world instanceof PlayScene) {
            PlayScene scene = (PlayScene) world;
            if (scene.GridManager != null) {
                scene.GridManager.removePlant(plant.getXPos(), plant.getYPos());
            }
        }

        if (plant.getWorld() != null) {
            world.removeObject(plant);
        }
    }
}