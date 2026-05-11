import java.util.List;
import java.util.ArrayList;
 
public class ZombieEventBus {
    private final List<IZombieEventListener> listeners;

    public ZombieEventBus() {
        this.listeners = new ArrayList<>();
    }

    /**
     * + subscribe(IZombieEventListener) : void
     */
    public void subscribe(IZombieEventListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }
    
    public void unsubscribe(IZombieEventListener listener) {
        listeners.remove(listener);
    }
    
    public void publishDeath(Zombie zombie) {
        for (IZombieEventListener listener : new ArrayList<>(listeners)) {
            listener.onZombieDied(zombie);
        }
    }
    public void publishHit(Zombie zombie, int damage) {
        for (IZombieEventListener listener : new ArrayList<>(listeners)) {
            listener.onZombieHit(zombie, damage);
        }
    }
    public void publishAteTarget(Zombie zombie, IEatable target) {
        for (IZombieEventListener listener : new ArrayList<>(listeners)) {
            listener.onZombieAteTarget(zombie, target);
        }
    }
    public void publishStateChanged(Zombie zombie, IZombieState newState) {
        for (IZombieEventListener listener : new ArrayList<>(listeners)) {
            listener.onZombieStateChanged(zombie, newState);
        }
    }
}