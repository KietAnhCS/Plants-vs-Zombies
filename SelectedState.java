public class SelectedState implements ICooldownState {
    
    @Override
    public void onEnter(SeedPacket packet) {
        packet.showBright(); 
    }

    @Override
    public void tick(SeedPacket packet) {
    }

    @Override
    public boolean canSelect(SeedPacket packet) {
        return false; 
    }

    @Override
    public void onSunChanged(SeedPacket packet, int currentSun) {
        if (currentSun < packet.sunCost) {
            packet.cancelSelect();
        }
    }
}