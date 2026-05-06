import greenfoot.*;

public class PlantFactory {

    public static Plant createPlant(PlantType type) {
        if (type == null) return null;
        switch (type) {
            case PEASHOOTER:    return new Peashooter();
            case REPEATER:      return new Repeater();
            case GATLING_PEA:   return new GatlingPea();
            case GATLING_PEA_2: return new GatlingPea2();
            case CACTUS:        return new Cactus();
            case CACTUS_2:      return new Cactus2();
            case CACTUS_3:      return new Cactus3();
            case BONK_CHOY:     return new BonkChoy();
            case BONK_CHOY_2:   return new BonkChoy2();
            case BONK_CHOY_3:   return new BonkChoy3();
            case POTATO_MINE:   return new PotatoMine();
            default:            return null;
        }
    }

    public static SeedPacket createSeedPacket(PlantType type) {
        if (type == null) return null;
        switch (type) {
            case PEASHOOTER:  return new PeashooterPacket();
            case REPEATER:    return new RepeaterPacket();
            case CACTUS:      return new CactusPacket();
            case BONK_CHOY:   return new BonkchoyPacket();
            case POTATO_MINE: return new PotatoPacket();
            default:          return new PeashooterPacket();
        }
    }

    public static Plant createPlant(String typeName) {
        try {
            return createPlant(PlantType.valueOf(normalize(typeName)));
        } catch (Exception e) {
            System.err.println("Error Plant: " + typeName);
            return null;
        }
    }

    public static SeedPacket createSeedPacket(String typeName) {
        try {
            return createSeedPacket(PlantType.valueOf(normalize(typeName)));
        } catch (Exception e) {
            System.err.println("Error Packet: " + typeName);
            return null;
        }
    }

    private static String normalize(String name) {
        switch (name.toUpperCase()) {
            case "BONKCHOY":    return "BONK_CHOY";
            case "BONKCHOY2":   return "BONK_CHOY_2";
            case "BONKCHOY3":   return "BONK_CHOY_3";
            case "GATLINGPEA":  return "GATLING_PEA";
            case "GATLINGPEA2": return "GATLING_PEA_2";
            case "POTATOMINE":  return "POTATO_MINE";
            case "CACTUS2":     return "CACTUS_2";
            case "CACTUS3":     return "CACTUS_3";
            default:            return name.toUpperCase();
        }
    }
}