public class LockedState implements IPacketState {

    @Override
    public void onEnter(SeedPacket packet) {
        packet.showDark(255); 
        packet.updateOverlay(1f); 
    }

    @Override
    public void tick(SeedPacket packet) {
    }

    @Override
    public void onSunChanged(SeedPacket packet, int currentSun) {
        if (currentSun >= packet.sunCost) {
            packet.setState(new AvailableState()); 
        }
    }

    @Override
    public boolean canPurchase(SeedPacket packet) {
        return false; 
    }
    
    @Override
    public void onClick(SeedPacket packet) {
    }
}