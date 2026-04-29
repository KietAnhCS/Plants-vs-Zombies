public class SoldOutState implements IPacketState {

    @Override
    public void onEnter(SeedPacket packet) {
        packet.showDark(130);
        
        packet.updateOverlay(0f);
    }

    @Override
    public void tick(SeedPacket packet) {
    }

    @Override
    public void onSunChanged(SeedPacket packet, int currentSun) {
    }

    @Override
    public boolean canPurchase(SeedPacket packet) {
        return false;
    }
    
    @Override
    public void onClick(SeedPacket packet) {
    }
}