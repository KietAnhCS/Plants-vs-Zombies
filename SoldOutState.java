public class SoldOutState implements IPacketState {
    @Override
    public void onEnter(SeedPacket packet) {
        packet.showDark(100); 
        packet.updateOverlay(1f); 
    }

    @Override
    public void onClick(SeedPacket packet) {
    }

    @Override
    public void onSunChanged(SeedPacket packet, int currentSun) {
    }

    @Override
    public boolean canPurchase(SeedPacket packet) {
        return false;
    }

    @Override
    public void tick(SeedPacket packet) {}
}