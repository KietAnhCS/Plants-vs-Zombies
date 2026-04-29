import greenfoot.*;

public class SeedBank extends Actor {
    public SunDisplay sunDisplay; 
    private SeedPacket[] bank;
    private DragController dragController;
    private boolean isTDActive = false;

    private static final int START_X   = 450;
    private static final int START_Y   = 650;
    private static final int SPACING_X = 150;

    public SeedBank(SeedPacket[] bank) {
        this.bank = bank;
        setImage((GreenfootImage) null);
    }

    @Override
    public void addedToWorld(World world) {
        PlayScene scene = (PlayScene) world;
        
        this.sunDisplay = scene.sunDisplay; 
        
        dragController = new DragController(sunDisplay, (IPlantPlacer) scene.GridManager);
        scene.addObject(dragController, 0, 0);
        
        if (sunDisplay.getWorld() == null) {
            scene.addObject(sunDisplay, 600, 600);
        }

        placePacketsInWorld(scene);
        spawnBonkchoyAtBench(scene);
    }

    @Override
    public void act() {
        PlayScene scene = (PlayScene) getWorld();
        if (scene == null || scene.getSunManager() == null) return;

        int currentSun = scene.getSunManager().getSun();

        for (SeedPacket p : bank) {
            if (p != null) {
                p.onSunChanged(currentSun);
            }
        }
    }

    public void updateBank(SeedPacket[] newBank) {
        removeOldPackets();
        if (isTDActive) {
            newBank[0] = new BonkchoyPacket();
            isTDActive = false;
        }
        this.bank = newBank;
        if (getWorld() != null) {
            placePacketsInWorld((PlayScene) getWorld());
        }
    }

    public void addSun(int amount) {
        PlayScene scene = (PlayScene) getWorld();
        if (scene != null && scene.getSunManager() != null) {
            if (amount >= 0) scene.getSunManager().add(amount);
            else scene.getSunManager().spend(Math.abs(amount));
        }
    }

    public int getSun() {
        PlayScene scene = (PlayScene) getWorld();
        if (scene != null && scene.getSunManager() != null) {
            return scene.getSunManager().getSun();
        }
        return 0;
    }

    public void setTD(boolean active) { this.isTDActive = active; }

    private void placePacketsInWorld(PlayScene scene) {
        for (int i = 0; i < bank.length; i++) {
            if (bank[i] != null) {
                scene.addObject(bank[i], START_X + i * SPACING_X, START_Y);
            }
        }
    }

    private void removeOldPackets() {
        for (SeedPacket p : bank) {
            if (p != null && p.getWorld() != null) {
                getWorld().removeObject(p);
            }
        }
    }

    private void spawnBonkchoyAtBench(PlayScene scene) {
        if (scene.GridManager != null) {
            scene.GridManager.placePlant(1, 5, new BonkChoy());
        }
    }
}