public class AvailableState implements IPacketState {
    @Override
    public void onEnter(SeedPacket packet) {
        packet.showBright();
        packet.updateOverlay(0f);
    }

    @Override
    public void onSunChanged(SeedPacket packet, int currentSun) {
        if (currentSun < packet.sunCost) {
            packet.setState(new LockedState());
        }
    }

    @Override
    public void onClick(SeedPacket packet) {
        packet.setState(new SelectedState());
    }

    @Override
    public boolean canPurchase(SeedPacket packet) {
        return true;
    }

    @Override
    public void tick(SeedPacket packet) {}
}