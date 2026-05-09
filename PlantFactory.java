import greenfoot.*;

public class PlantFactory implements IPlantFactory {
    
    private static PlantFactory instance;
    
    public static PlantFactory getInstance() {
        if (instance == null) {
            instance = new PlantFactory();
        }
        return instance;
    }

    @Override
    public Plant createPlant(PlantType type) {
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
            default:            return null;
        }
    }

    @Override
    public SeedPacket createSeedPacket(PlantType type) {
        if (type == null) return null;
        switch (type) {
            case PEASHOOTER:    return new SeedPacket(5000, 100, "peashooterpacket");
            case REPEATER:      return new SeedPacket(5000, 200, "Repeater");
            case CACTUS:        return new SeedPacket(7000, 125, "Cactus");
            case BONK_CHOY:     return new SeedPacket(7000, 150, "bonkchoypacket");
            case POTATO_MINE:   return new SeedPacket(15000, 25, "PotatoMine");
            default:            return new SeedPacket(5000, 100, "Peashooter");
        }
    }

    @Override
    public Plant createPlant(String typeName) {
        if (typeName == null) return null;
        try {
            return createPlant(PlantType.valueOf(normalize(typeName)));
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public SeedPacket createSeedPacket(String typeName) {
        if (typeName == null) return null;
        try {
            return createSeedPacket(PlantType.valueOf(normalize(typeName)));
        } catch (Exception e) {
            return null;
        }
    }

    private String normalize(String name) {
        if (name == null) return "";
        String n = name.toUpperCase().replace(" ", "_");
        
        if (n.endsWith("_PACKET")) n = n.substring(0, n.lastIndexOf("_PACKET"));
        if (n.endsWith("PACKET")) n = n.substring(0, n.lastIndexOf("PACKET"));

        switch (n) {
            case "BONKCHOY":    return "BONK_CHOY";
            case "BONKCHOY2":   return "BONK_CHOY_2";
            case "BONKCHOY3":   return "BONK_CHOY_3";
            case "GATLINGPEA":  return "GATLING_PEA";
            case "GATLINGPEA2": return "GATLING_PEA_2";
            case "POTATOMINE":  return "POTATO_MINE";
            case "CACTUS2":     return "CACTUS_2";
            case "CACTUS3":     return "CACTUS_3";
            default:            return n;
        }
    }
}