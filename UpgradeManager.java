import greenfoot.*;

public class UpgradeManager {
    public static boolean canUpgrade(Plant p1, Plant p2) {
        if (p1 == null || p2 == null) return false;
        
        if ((p1 instanceof Torchwood && p2 instanceof GatlingPea) || 
            (p1 instanceof GatlingPea && p2 instanceof Torchwood)) {
            return true;
        }
        
        return false;
    }

    public static Plant getUpgradeResult(Plant p) {
        if (p instanceof Peashooter) return new Repeater();
        if (p instanceof Sunflower) return new TwinSunflower();
        if (p instanceof Repeater) return new GatlingPea();
        if (p instanceof Cactus) return new CactusPF();
        return null;
    }

    public static Plant getSpecialUpgrade(Plant p1, Plant p2) {
        if ((p1 instanceof Torchwood && p2 instanceof GatlingPea) || 
            (p1 instanceof GatlingPea && p2 instanceof Torchwood)) {
            return new MegaGatlingPea();
        }
        return null;
    }
}