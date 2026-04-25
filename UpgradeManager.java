import greenfoot.*;

public class UpgradeManager {
    public static boolean canUpgrade(Plant p1, Plant p2) {
        if (p1 == null || p2 == null) return false;
        
        return false;
    }

    public static Plant getUpgradeResult(Plant p) {
        if (p instanceof Peashooter) return new Repeater();
        if (p instanceof Sunflower) return new TwinSunflower();
        if (p instanceof Repeater) return new GatlingPea();
        if (p instanceof Cactus) return new BonkChoy();
        return null;
    }

    public static Plant getSpecialUpgrade(Plant p1, Plant p2) {
        
        return null;
    }
}