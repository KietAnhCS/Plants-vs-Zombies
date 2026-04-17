import greenfoot.*;

public class UpgradeManager {
    
    public static boolean canUpgrade(Plant handPlant, Plant boardPlant) {
        if (handPlant == null || boardPlant == null) return false;
        
        if (handPlant instanceof Peashooter && boardPlant instanceof Peashooter) {
            return true;
        }
        if (handPlant instanceof Repeater && boardPlant instanceof Repeater) {
            return true;
        }
        
        return false;
    }

    public static Plant getUpgradeResult(Plant handPlant, Plant boardPlant) {
        if (handPlant instanceof Peashooter && boardPlant instanceof Peashooter) {
            return new Repeater();
        }
        if (handPlant instanceof Repeater && boardPlant instanceof Repeater) {
            return new GatlingPea();
        }
        return null;
    }
    
    
}