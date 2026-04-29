import greenfoot.*;

public class SeedBank extends Actor {

    public final SunCounter sunCounter = new SunCounter();

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
        dragController = new DragController(sunCounter, (IPlantPlacer) scene.GridManager);
        scene.addObject(dragController, 0, 0);
        scene.addObject(sunCounter, 600, 600);
        placePacketsInWorld(scene);
        spawnBonkchoyAtBench(scene);
    }

    @Override
    public void act() {
        for (SeedPacket p : bank) {
            if (p != null) p.onSunChanged(sunCounter.sun);
        }
    }

    public void updateBank(SeedPacket[] newBank) {
        removeOldPackets();
        if (isTDActive) {
            newBank[0] = new BonkchoyPacket();
            isTDActive = false;
        }
        this.bank = newBank;
        placePacketsInWorld((PlayScene) getWorld());
    }

    public void addSun(int amount) {
        if (amount >= 0) sunCounter.addSun(amount);
        else sunCounter.removeSun(Math.abs(amount));
        for (SeedPacket p : bank) {
            if (p != null) p.onSunChanged(sunCounter.sun);
        }
    }

    public int getSun() { return sunCounter.sun; }
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