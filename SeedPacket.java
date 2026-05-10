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

    protected PlayScene playScene;

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
        if (state instanceof AvailableState) {
            return getCurrentSun() >= sunCost;
        }
        return state != null && state.canPurchase(this);
    }

    public boolean tryPurchase() {
        if (canBePurchased()) {
            setState(new SelectedState());
            return true;
        }
        Greenfoot.playSound("gulp.mp3");
        return false;
    }

    public void used() {
        if (getWorld() != null) {
            getWorld().removeObject(this);
        }
    }

    public void cancelSelect() {
        setState(new AvailableState());
    }

    public Plant getPlant() {
        if (playScene != null && playScene.getPlantFactory() != null) {
            return playScene.getPlantFactory().createPlant(this.name);
        }
        return PlantFactory.getInstance().createPlant(this.name);
    }

    public void onSunChanged(int currentSun) {
        if (state != null) state.onSunChanged(this, currentSun);
    }

    public void showBright() {
        setImage(imageBright);
    }

    public void showDark(int alpha) {
    }

    public void updateOverlay(float progress) {
    }

    public int getCurrentSun() {
        if (playScene == null || playScene.getSunManager() == null) return 0;
        return playScene.getSunManager().getSun();
    }
    
    public TransparentObject addImage() {
        TransparentObject ghost = new TransparentObject();
        ghost.setImage(new GreenfootImage(imageBright));
        return ghost;
    }
}