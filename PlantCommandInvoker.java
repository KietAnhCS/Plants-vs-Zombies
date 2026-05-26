import java.util.Stack;

public class PlantCommandInvoker {
    private final Stack<PlantCommand> history = new Stack<>();

    public void executeCommand(PlantCommand cmd) {
        cmd.execute();
        history.push(cmd);
    }
    
    public void register(PlantCommand cmd) {
        history.push(cmd);
    }

    public void undoLast() {
        if (!history.isEmpty()) {
            history.pop().undo();
        }
    }

    public void clear() {
        history.clear();
    }
}