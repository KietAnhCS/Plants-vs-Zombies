import greenfoot.*;

public class ZombieFactory {
    
    public enum ZombieType {
        NORMAL,
        CONEHEAD,
        BUCKETHEAD,
        BRICKHEAD
    }

    public static Zombie createZombie(ZombieType type) {
        if (type == null) return null;

        switch (type) {
            case NORMAL:
                return new BasicZombie();
            case CONEHEAD:
                return new Conehead();
            case BUCKETHEAD:
                return new Buckethead();
            case BRICKHEAD:
                return new Brickhead();
            default:
                return new BasicZombie();
        }
    }

    public static Zombie createZombie(String typeName) {
        try {
            return createZombie(ZombieType.valueOf(typeName.toUpperCase()));
        } catch (Exception e) {
            System.err.println("Error: " + typeName);
            return new BasicZombie();
        }
    }
}