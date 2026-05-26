import greenfoot.*;

public class MergeAnimationCommand implements PlantCommand {
    private Plant source;
    private Plant target;
    private double speed = 15.0;
    private boolean arrived = false;
    private boolean undone  = false;

    private double originalX, originalY;
    private int origGridX, origGridY;

    private final MergeCommand mergeCommand; 

    public MergeAnimationCommand(Plant source, Plant target, MergeCommand mergeCommand) {
        this.source       = source;
        this.target       = target;
        this.mergeCommand = mergeCommand;
        this.originalX    = source.getExactX();
        this.originalY    = source.getExactY();
        this.origGridX    = source.getXPos();
        this.origGridY    = source.getYPos();
    }

    public boolean tick() {
        if (arrived || undone) return true;
        if (target == null || target.getWorld() == null) return true;
        if (source == null || source.getWorld() == null) {
            mergeCommand.notifyArrived(null);
            arrived = true;
            return true;
        }

        double dx   = target.getExactX() - source.getExactX();
        double dy   = target.getExactY() - source.getExactY();
        double dist = Math.sqrt(dx * dx + dy * dy);

        if (dist <= speed) {
            arrived = true;
            mergeCommand.notifyArrived(source);
            return true;
        }

        source.setLocation(
            source.getExactX() + (dx / dist) * speed,
            source.getExactY() + (dy / dist) * speed
        );
        return false;
    }

    @Override public void execute() {}

    @Override
    public void undo() {
        undone = true;
        if (source != null && source.getWorld() != null) {
            source.setLocation((int) originalX, (int) originalY);
            source.setGridPosition(origGridX, origGridY);
        }
    }
}