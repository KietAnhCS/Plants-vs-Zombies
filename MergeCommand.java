import greenfoot.*;

public class MergeCommand implements PlantCommand {
    private Plant source1;
    private Plant source2;
    private Plant target;
    private Plant upgraded;

    private int gridX, gridY;
    private int px, py;
    private PlayScene scene;

    private int arrivedCount = 0; 

    public MergeCommand(Plant source1, Plant source2, Plant target) {
        this.source1 = source1;
        this.source2 = source2;
        this.target  = target;
    }

    public void notifyArrived(Plant arrivedSource) {
        if (arrivedSource != null && arrivedSource.getWorld() != null) {
            arrivedSource.getWorld().removeObject(arrivedSource);
        }

        arrivedCount++;
        if (arrivedCount >= 2) {
            execute(); 
        }
    }

    @Override
    public void execute() {
        if (target == null || target.getWorld() == null) return;
        if (!(target.getWorld() instanceof PlayScene)) return;

        scene = (PlayScene) target.getWorld();
        gridX = target.getXPos();
        gridY = target.getYPos();
        px    = target.getX();
        py    = target.getY();

        upgraded = UpgradeManager.getUpgradeResult(target);
        if (upgraded == null) return;

        scene.GridManager.removePlant(gridX, gridY);
        scene.removeObject(target);       

        upgraded.setGridPosition(gridX, gridY);
        upgraded.setState(PlantState.IDLE);
        scene.addObject(upgraded, px, py);
        scene.GridManager.placePlant(gridX, gridY, upgraded);
        scene.addObject(new Dirt(), px, py + 30);
    }

    @Override
    public void undo() {
        if (scene == null) return;
        if (upgraded != null && upgraded.getWorld() != null) {
            scene.GridManager.removePlant(gridX, gridY);
            scene.removeObject(upgraded);
        }
        if (target != null) {
            target.setGridPosition(gridX, gridY);
            target.setState(PlantState.IDLE);
            scene.addObject(target, px, py);
            scene.GridManager.placePlant(gridX, gridY, target);
        }
    }
}