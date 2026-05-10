import greenfoot.*;

public class UpgradeManager {
    private PlantEventBus eventBus;

    public UpgradeManager(IPlantFactory factory, PlantEventBus eventBus) {
        this.eventBus = eventBus;
    }

    public PlantEventBus getEventBus() {
        return eventBus;
    }

    public static Plant getUpgradeResult(Plant p) {
        if (p instanceof Peashooter) return new Repeater();
        if (p instanceof Repeater) return new GatlingPea();
        if (p instanceof GatlingPea) return new GatlingPea2();
        if (p instanceof Cactus) return new Cactus2();
        if (p instanceof Cactus2) return new Cactus3();
        if (p instanceof BonkChoy) return new BonkChoy2();
        if (p instanceof BonkChoy2) return new BonkChoy3();
        return null;
    }

    // Hàm merge cũ có thể bỏ hoặc xóa bớt để không gọi chéo gây lỗi biến mất
    public Plant merge(Plant target) {
        return getUpgradeResult(target); 
    }
}