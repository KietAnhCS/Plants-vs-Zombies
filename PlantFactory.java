import greenfoot.*;

public class PlantFactory {
    
    public enum PlantType {
        PEASHOOTER, 
        REPEATER, 
        GATLINGPEA,
        GATLINGPEA2,
        CACTUS,
        CACTUS2,
        CACTUS3,
        BONKCHOY,
        BONKCHOY2,
        POTATOMINE
    }

    public static Plant createPlant(PlantType type) {
        if (type == null) return null;
        switch (type) {
            case PEASHOOTER: return new Peashooter();
            case REPEATER: return new Repeater();
            case GATLINGPEA: return new GatlingPea();
            case GATLINGPEA2: return new GatlingPea2();
            case CACTUS: return new Cactus();
            case CACTUS2: return new Cactus2();
            case CACTUS3: return new Cactus3();
            case BONKCHOY: return new BonkChoy();
            case BONKCHOY2: return new BonkChoy2();
            case POTATOMINE: return new PotatoMine();
            default: return null;
        }
    }

    public static SeedPacket createSeedPacket(PlantType type) {
        if (type == null) return null;
        switch (type) {
            case PEASHOOTER: return new PeashooterPacket();
            case REPEATER: return new RepeaterPacket();
            case GATLINGPEA: return new GatlingPeaPacket();
            case CACTUS: return new CactusPacket();
            case BONKCHOY: return new BonkchoyPacket();
            case POTATOMINE: return new PotatoPacket();
            default: return new PeashooterPacket();
        }
    }

    public static Plant createPlant(String typeName) {
        try {
            return createPlant(PlantType.valueOf(typeName.toUpperCase()));
        } catch (Exception e) {
            System.err.println("Error Plant: " + typeName);
            return null;
        }
    }

    public static SeedPacket createSeedPacket(String typeName) {
        try {
            return createSeedPacket(PlantType.valueOf(typeName.toUpperCase()));
        } catch (Exception e) {
            System.err.println("Error Packet: " + typeName);
            return null;
        }
    }
}