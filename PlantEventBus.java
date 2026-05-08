import java.util.List;
import java.util.ArrayList;

public class PlantEventBus {
    private List<IPlantEventListener> listeners;

    public PlantEventBus() {
        listeners = new ArrayList<>();
    }

    public void subscribe(IPlantEventListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void unsubscribe(IPlantEventListener listener) {
        listeners.remove(listener);
    }

    public void publishDeath(Plant plant) {
        for (IPlantEventListener listener : new ArrayList<>(listeners)) {
            listener.onPlantDied(plant);
        }
    }

    public void publishHit(Plant plant, int damage) {
        for (IPlantEventListener listener : new ArrayList<>(listeners)) {
            listener.onPlantHit(plant, damage);
        }
    }

    public void publishMerge(Plant source, Plant target) {
        for (IPlantEventListener listener : new ArrayList<>(listeners)) {
            listener.onPlantMerged(source, target);
        }
    }

    public void publishStateChanged(Plant plant, PlantState newState) {
        for (IPlantEventListener listener : new ArrayList<>(listeners)) {
            listener.onPlantStateChanged(plant, newState);
        }
    }
}