import greenfoot.*;

public class ZombieFactory {

    public enum ZombieType {
        NORMAL(BasicZombie::new),
        CONEHEAD(Conehead::new),
        BUCKETHEAD(Buckethead::new),
        BRICKHEAD(Brickhead::new),
        PIANO(PianoZombie::new);

        private final java.util.function.Supplier<Zombie> supplier;

        ZombieType(java.util.function.Supplier<Zombie> supplier) {
            this.supplier = supplier;
        }

        public Zombie create() {
            return supplier.get();
        }
    }

    public static Zombie createZombie(ZombieType type) {
        if (type == null) return new BasicZombie();
        return type.create();
    }

    public static Zombie createZombie(String typeName) {
        try {
            return ZombieType.valueOf(typeName.toUpperCase()).create();
        } catch (IllegalArgumentException e) {
            System.err.println("Unknown zombie type: " + typeName);
            return new BasicZombie();
        }
    }
}