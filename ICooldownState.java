public interface ICooldownState {
    void onEnter(SeedPacket packet);
    void tick(SeedPacket packet);
    boolean canSelect(SeedPacket packet);
    void onSunChanged(SeedPacket packet, int currentSun);
}