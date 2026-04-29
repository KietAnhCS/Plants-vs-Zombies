import greenfoot.*;

public class SeedPacket extends Actor {
    public final int sunCost;
    public final String name;
    public final long rechargeTime;
    private IPacketState state;

    private final GreenfootImage imageBright;
    private final GreenfootImage imageDark;
    private final GreenfootImage overlayBuffer;
    private final GreenfootImage combined;

    private PlayScene playScene;

    public SeedPacket(long rechargeTime, int sunCost, String name) {
        this.rechargeTime = rechargeTime;
        this.sunCost = sunCost;
        this.name = name.toUpperCase();
        this.imageBright = new GreenfootImage(name.toLowerCase() + "1.png");
        this.imageDark = new GreenfootImage(name.toLowerCase() + "2.png");
        this.overlayBuffer = new GreenfootImage(imageDark.getWidth(), imageDark.getHeight());
        this.combined = new GreenfootImage(imageDark.getWidth(), imageDark.getHeight());
        
        this.state = new AvailableState(); 
        setImage(imageBright);
    }

    @Override
    public void addedToWorld(World world) {
        playScene = (PlayScene) world;
        onSunChanged(getCurrentSun());
    }

    @Override
    public void act() {
        if (state != null) state.tick(this);
    }

    public void setState(IPacketState newState) {
        this.state = newState;
        newState.onEnter(this);
    }

    public boolean canBePurchased() {
        return state != null && state.canPurchase(this);
    }

    public boolean tryPurchase() {
        return canBePurchased();
    }

    public void confirmPlace() {
        setState(new LockedState()); 
    }

    public void cancelSelect() {
        onSunChanged(getCurrentSun());
    }

    public Plant getPlant() {
        return PlantFactory.createPlant(this.name);
    }

    public TransparentObject addImage() {
        return null;
    }

    public void onSunChanged(int currentSun) {
        if (state != null) state.onSunChanged(this, currentSun);
    }

    public void showBright() {
        setImage(imageBright);
        if (getImage() != null) getImage().setTransparency(255);
    }

    public void showDark(int alpha) {
        setImage(imageDark);
        if (getImage() != null) getImage().setTransparency(alpha);
    }

    public void updateOverlay(float progress) {
        if (imageDark == null || combined == null) return;
        combined.drawImage(imageDark, 0, 0);
        overlayBuffer.clear();
        overlayBuffer.setColor(new Color(0, 0, 0, 150));
        int darkHeight = (int) (imageDark.getHeight() * (1f - progress));
        if (darkHeight > 0) {
            overlayBuffer.fillRect(0, 0, imageDark.getWidth(), darkHeight);
        }
        combined.drawImage(overlayBuffer, 0, 0);
        setImage(combined);
    }

    public int getCurrentSun() {
        if (playScene == null || playScene.getSunManager() == null) return 0;
        return playScene.getSunManager().getSun();
    }
}