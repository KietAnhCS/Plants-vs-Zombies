import greenfoot.*;

public class SeedPacket extends Actor {

    public final int    sunCost;
    public final String name;
    public final long   rechargeTime;

    private ICooldownState state;

    private final GreenfootImage imageBright;
    private final GreenfootImage imageDark;
    private final GreenfootImage overlayBuffer;
    private final GreenfootImage combined;

    private PlayScene playScene;

    public SeedPacket(long rechargeTime, int sunCost, String name) {
        this.rechargeTime  = rechargeTime;
        this.sunCost       = sunCost;
        this.name          = name.toLowerCase();
        this.imageBright   = new GreenfootImage(name + "1.png");
        this.imageDark     = new GreenfootImage(name + "2.png");
        this.overlayBuffer = new GreenfootImage(imageDark.getWidth(), imageDark.getHeight());
        this.combined      = new GreenfootImage(imageDark.getWidth(), imageDark.getHeight());
        this.state         = new ExhaustedState();
        setImage(imageDark);
    }

    @Override
    public void addedToWorld(World world) {
        playScene = (PlayScene) world;
    }

    @Override
    public void act() {
        if (state == null) return;
        state.tick(this);
    }

    public void setState(ICooldownState newState) {
        this.state = newState;
        newState.onEnter(this);
    }

    public boolean canBeSelected() {
        return state.canSelect(this);
    }

    public boolean trySelect() {
        if (!state.canSelect(this)) return false;
        setState(new SelectedState());
        return true;
    }

    public void confirmPlace() {
        setState(new CoolingState(rechargeTime));
    }

    public void cancelSelect() {
        setState(new ExhaustedState());
    }

    public void onSunChanged(int currentSun) {
        state.onSunChanged(this, currentSun);
    }

    public void showBright() {
        setImage(imageBright);
        getImage().setTransparency(255);
    }

    public void showDark(int alpha) {
        setImage(imageDark);
        getImage().setTransparency(alpha);
    }

    public void updateOverlay(float progress) {
        combined.drawImage(imageDark, 0, 0);
        overlayBuffer.clear();
        overlayBuffer.setColor(new Color(0, 0, 0, 120));
        int darkHeight = (int) (imageDark.getHeight() * (1f - progress));
        if (darkHeight > 0) {
            overlayBuffer.fillRect(0, 0, imageDark.getWidth(), darkHeight);
        }
        combined.drawImage(overlayBuffer, 0, 0);
        setImage(combined);
    }

    public int getCurrentSun() {
        if (playScene == null || playScene.seedbank == null) return 0;
        return playScene.seedbank.getSun();
    }

    public TransparentObject addImage() { return null; }
    public Plant getPlant()             { return null; }
}