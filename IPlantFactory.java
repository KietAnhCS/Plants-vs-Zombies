public interface IPlantFactory {
    Plant createPlant(PlantType type);
    SeedPacket createSeedPacket(PlantType type);
    
    Plant createPlant(String typeName);
    SeedPacket createSeedPacket(String typeName);
}