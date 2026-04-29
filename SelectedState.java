public class SelectedState implements IPacketState {
    @Override
    public void onEnter(SeedPacket packet) {
        packet.showBright();
    }

    @Override
    public void onClick(SeedPacket packet) {
        packet.cancelSelect();
        packet.setState(new AvailableState());
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