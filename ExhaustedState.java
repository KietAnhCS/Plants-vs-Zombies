public class ExhaustedState implements ICooldownState {
    @Override
    public void onEnter(SeedPacket packet) {
        packet.showDark(255); 
    }

    @Override
    public void tick(SeedPacket packet) {
    }

    @Override
    public void onSunChanged(SeedPacket packet, int currentSun) {
        if (currentSun >= packet.sunCost) {
            packet.setState(new ReadyState());
        }
    }

    @Override
    public boolean canSelect(SeedPacket packet) {
        return false; 
    }
}