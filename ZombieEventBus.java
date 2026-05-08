import java.util.List;
import java.util.ArrayList;

/**
 * ZombieEventBus: Trung tâm phân phối sự kiện (Event Dispatcher).
 */
public class ZombieEventBus {
    // - List<IZombieEventListener> listeners
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

    /**
     * + unsubscribe(IZombieEventListener) : void
     */
    public void unsubscribe(IZombieEventListener listener) {
        listeners.remove(listener);
    }

    /**
     * + publishDeath(Zombie) : void
     */
    public void publishDeath(Zombie zombie) {
        // Sử dụng bản sao để tránh lỗi nếu danh sách bị thay đổi trong lúc duyệt
        for (IZombieEventListener listener : new ArrayList<>(listeners)) {
            listener.onZombieDied(zombie);
        }
    }

    /**
     * + publishHit(Zombie, int) : void
     */
    public void publishHit(Zombie zombie, int damage) {
        for (IZombieEventListener listener : new ArrayList<>(listeners)) {
            listener.onZombieHit(zombie, damage);
        }
    }

    /**
     * + publishAteTarget(Zombie, IEatable) : void
     */
    public void publishAteTarget(Zombie zombie, IEatable target) {
        for (IZombieEventListener listener : new ArrayList<>(listeners)) {
            listener.onZombieAteTarget(zombie, target);
        }
    }

    /**
     * + publishStateChanged(Zombie, IZombieState) : void
     */
    public void publishStateChanged(Zombie zombie, IZombieState newState) {
        for (IZombieEventListener listener : new ArrayList<>(listeners)) {
            listener.onZombieStateChanged(zombie, newState);
        }
    }
}