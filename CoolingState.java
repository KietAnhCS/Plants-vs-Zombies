public class CoolingState implements ICooldownState {

    private final long durationMs;
    private long startMs;

    public CoolingState(long durationMs) {
        this.durationMs = durationMs;
    }

    @Override
    public void onEnter(SeedPacket packet) {
        startMs = System.currentTimeMillis();
        packet.showDark(130);
        packet.updateOverlay(0f);
    }

    @Override
    public void tick(SeedPacket packet) {
        long elapsed = System.currentTimeMillis() - startMs;
        float progress = Math.min(1f, (float) elapsed / durationMs);
        packet.updateOverlay(progress);
        if (progress >= 1f) {
            transitionOut(packet);
        }
    }

    @Override
    public boolean canSelect(SeedPacket packet) {
        return false;
    }

    @Override
    public void onSunChanged(SeedPacket packet, int currentSun) { }

    private void transitionOut(SeedPacket packet) {
        if (packet.getCurrentSun() >= packet.sunCost) {
            packet.setState(new ReadyState());
        } else {
            packet.setState(new ExhaustedState());
        }
    }
}