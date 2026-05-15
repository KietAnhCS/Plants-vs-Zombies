import greenfoot.*;

public interface IPlantProvider {
    
    void rollPackets(int cost);
    
    boolean canAffordRoll(int cost);
    
    void updateStore(SeedPacket[] newPackets);
    
    int getRollCost();
}