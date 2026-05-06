import greenfoot.*;

public class AvailableState implements IPacketState {
    @Override
    public void onEnter(SeedPacket packet) {
    }

    @Override
    public void onSunChanged(SeedPacket packet, int currentSun) {
    }

    @Override
    public void onClick(SeedPacket packet) {
        if (packet.getCurrentSun() >= packet.sunCost) {
            packet.setState(new SelectedState());
        } else {
            Greenfoot.playSound("gulp.mp3");
        }
    }

    @Override
    public boolean canPurchase(SeedPacket packet) {
        return false;
    }

    @Override
    public void tick(SeedPacket packet) {
    }
}