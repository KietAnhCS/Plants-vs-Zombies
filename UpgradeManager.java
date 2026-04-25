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
        if (p instanceof Cactus) return new Cactus2();
        if (p instanceof Cactus2) return new Cactus3();
        if (p instanceof BonkChoy) return new BonkChoy2();
        if (p instanceof BonkChoy2) return new BonkChoy3();
        return null;
    }

    public static Plant getSpecialUpgrade(Plant p1, Plant p2) {
        
        return null;
    }
}