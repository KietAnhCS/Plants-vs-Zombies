public interface IPacketState {
    void onEnter(SeedPacket packet);
    void tick(SeedPacket packet);
    void onClick(SeedPacket packet);
    boolean canPurchase(SeedPacket packet);
    void onSunChanged(SeedPacket packet, int currentSun);
}