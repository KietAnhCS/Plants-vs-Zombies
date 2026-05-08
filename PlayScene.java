import greenfoot.*;
import java.util.*;

public class PlayScene extends World {
    public boolean loseOnce = false;
    public boolean winOnce = false;
    public boolean isGameOver = false;
    
    public GridManager GridManager = new GridManager();
    public WaveManager level;
    public FallingObject winPlant;
    public SeedBank seedbank;
    public World restartWorld;
    public GreenfootSound CYS;
    
    public SunDisplay sunDisplay = new SunDisplay();
    public Hitbox hitbox = new Hitbox();
    public Shovel shovel = new Shovel();
    public MuteButton mutebutton = new MuteButton();
    public RollButton rollbutton = new RollButton();
    public RupButton rupbutton = new RupButton();
    
    private SunManager sunManager;
    private MusicController musicController;
    private SunSpawner sunSpawner;
    private WinLossHandler winLossHandler;
    private DebugHandler debugHandler;
    private UpgradeManager upgradeManager;
    private List<Merger> activeMergers = new ArrayList<>();
    private boolean isPlaying = false;

    public PlayScene(GreenfootSound CYS, WaveManager level, SeedBank seedbank, World restartWorld, FallingObject winPlant, boolean isWater) {
        super(1111, 698, 1, false);
        this.CYS = CYS;
        this.seedbank = seedbank;
        this.restartWorld = restartWorld;
        this.level = level;
        this.winPlant = winPlant;
        this.sunManager = new SunManager();
        this.upgradeManager = new UpgradeManager(PlantFactory.getInstance(), new PlantEventBus());
        
        Greenfoot.setSpeed(50);
        setBackground("maptft2.png");
        
        addObject(GridManager, 555, 349);
        addObject(new SliderBar(), 850, 50);
        addObject(mutebutton, 1050, 50);
        addObject(new ThuyThan(), 110, 500);
        addObject(seedbank, 0, 0);
        addObject(sunDisplay, 600, 570);
        addObject(hitbox, 555, 349);
        addObject(shovel, 930, 615);
        addObject(rollbutton, 325, 625);
        addObject(rupbutton, 175, 625);
        
        prepareLawnmowers();
        
        musicController = new MusicController(this);
        sunSpawner = new SunSpawner(this);
        winLossHandler = new WinLossHandler(this);
        debugHandler = new DebugHandler(this);
        
        applyDefaultPaintOrder();
    }

    public void act() {
        if (isGameOver) return;
        moveHitbox();
        
        if (!isPlaying && level != null) {
            addObject(level, 0, 0);
            isPlaying = true;
            level.startLevel();
        }
        
        musicController.update();
        sunSpawner.update();
        winLossHandler.update();
        debugHandler.update();
        updateMergers();
        drawWaveUI();
    }

    public PlantFactory getPlantFactory() {
        return PlantFactory.getInstance();
    }

    public UpgradeManager getUpgradeManager() {
        return upgradeManager;
    }

    public SunManager getSunManager() {
        return sunManager;
    }

    public void increasePlantSlots(int amount) {
        if (GridManager != null) {
            GridManager.addBonusSlots(amount);
            AudioManager.getInstance().playSound(80, false, "achievement.mp3");
        }
    }

    public boolean tryPlacePlant(int gridX, int gridY, Plant newPlant) {
        if (newPlant == null || level.choosingCard || !getObjects(CrazyDave.class).isEmpty()) return false;
        boolean placed = GridManager.placePlant(gridX, gridY, newPlant);
        if (placed) PlantCombineHandler.checkAndCombine(this, newPlant);
        return placed;
    }

    public void rollPackets() {
        if (!getSunManager().hasEnough(25)) return;
        RupButton.RarityEntry[] pool = rupbutton.getPoolForRoll();
        int total = 0;
        for (RupButton.RarityEntry e : pool) if (e != null && e.weight > 0) total += e.weight;
        if (total <= 0) return;
        getSunManager().spend(25);
        AudioManager.getInstance().playSound(80, false, "achievement.mp3");
        SeedPacket[] newBank = new SeedPacket[3];
        for (int i = 0; i < 3; i++) {
            int rnd = Greenfoot.getRandomNumber(total), cursor = 0;
            for (RupButton.RarityEntry e : pool) {
                if (e == null || e.weight <= 0) continue;
                cursor += e.weight;
                if (rnd < cursor) {
                    String name = e.packetClass.getSimpleName().replace("Packet", "").toUpperCase();
                    newBank[i] = getPlantFactory().createSeedPacket(name);
                    break;
                }
            }
        }
        seedbank.updateBank(newBank);
    }

    public boolean hasLost() {
        for (Zombie z : getObjects(Zombie.class)) {
            if (z.getWorld() != null && z.getX() < 155) return true;
        }
        return false;
    }

    public boolean hasWon() {
        return level != null && level.hasWon();
    }

    public void finishLevel() {
        this.isGameOver = true;
    }

    public void stopAllMusic() {
        AudioManager.stopBGM();
        if (CYS != null && CYS.isPlaying()) CYS.stop();
        if (musicController != null) musicController.resetBGM();
    }

    public void applyDefaultPaintOrder() {
        setPaintOrder(
            CrazyDave.class, Transition.class, AugmentCard.class, Overlay.class,
            SliderKnob.class, SliderBar.class, MuteButton.class,
            ThuyThan.class, RupButton.class, RollButton.class,
            WaveNotification.class, ReadySetPlant.class,
            SunDisplay.class, SeedPacket.class, SellShovel.class, Shovel.class,
            Lawnmower.class, Sun.class, HealthBar.class,
            Pea.class, FirePea.class, Needle.class, Zombie.class,
            Plant.class, GridManager.class, Dirt.class
        );
    }

    private void updateMergers() {
        Iterator<Merger> it = activeMergers.iterator();
        while (it.hasNext()) if (it.next().update()) it.remove();
    }

    public void addActiveMerger(Merger m) { activeMergers.add(m); }

    public void moveHitbox() {
        MouseInfo m = Greenfoot.getMouseInfo();
        if (m != null) hitbox.setLocation(m.getX(), m.getY());
    }

    private void prepareLawnmowers() {
        int[][] coords = {{240,180},{236,240},{225,295},{220,360},{190,420}};
        for (int[] c : coords) addObject(new Lawnmower(), c[0], c[1]);
    }

    private void drawWaveUI() {
        if (level == null) return;
        GreenfootImage canvas = getBackground();
        String text = "WAVE " + level.getWaveNumber();
        canvas.setColor(new Color(0, 0, 0, 160));
        canvas.fillRect(24, 84, 140, 40);
        canvas.setColor(Color.WHITE);
        canvas.drawRect(20, 80, 140, 40);
        canvas.setFont(new Font("Courier New", true, false, 22));
        canvas.setColor(new Color(0, 191, 255));
        canvas.drawString(text, 40, 107);
    }

    public void started() { if (!isGameOver) musicController.update(); }
    public void stopped() { AudioManager.stopBGM(); }
}