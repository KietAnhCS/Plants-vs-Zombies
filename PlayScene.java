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
    private boolean isPlaying = false;

    public PlayScene(GreenfootSound CYS, WaveManager level, SeedBank seedbank,
                     World restartWorld, FallingObject winPlant, boolean isWater) {
        super(1111, 698, 1, false);
        this.CYS = CYS;
        this.seedbank = seedbank;
        this.restartWorld = restartWorld;
        this.level = level;
        this.winPlant = winPlant;
        this.sunManager = new SunManager();

        Greenfoot.setSpeed(50);
        setBackground("maptft2.png");

        addObject(new SliderBar(), 850, 50);
        addObject(mutebutton, 1050, 50);
        addObject(GridManager, 555, 349);
        addObject(new ThuyThan(), 110, 500);
        addObject(seedbank, 0, 0);
        addObject(sunDisplay, 600, 570);
        sunDisplay.setLocation(600, 570);
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
        if (!isPlaying) {
            addObject(level, 0, 0);
            isPlaying = true;
            level.startLevel();
        }
        musicController.update();
        sunSpawner.update();
        winLossHandler.update();
        debugHandler.update();
        drawWaveUI();
    }

    public SunManager getSunManager() { 
        return sunManager; 
    }

    public void stopAllMusic() {
        AudioManager.stopBGM();
        if (CYS != null && CYS.isPlaying()) CYS.stop();
        musicController.resetBGM();
    }

    public void increasePlantSlots(int amount) {
        if (GridManager != null) {
            GridManager.addBonusSlots(amount);
            AudioManager.playSound(80, false, "achievement.mp3");
        }
    }

    public boolean tryPlacePlant(int gridX, int gridY, Plant newPlant) {
        if (newPlant == null || level.choosingCard || !getObjects(CrazyDave.class).isEmpty()) return false;
        return GridManager.placePlant(gridX, gridY, newPlant);
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
        isGameOver = true;
        stopAllMusic();
    }

    public void rollPackets() {
        if (!getSunManager().hasEnough(25)) return;
        RupButton.RarityEntry[] pool = rupbutton.getPoolForRoll();
        int total = 0;
        for (RupButton.RarityEntry e : pool) {
            if (e != null && e.weight > 0) total += e.weight;
        }
        if (total <= 0) return;

        getSunManager().spend(25);
        AudioManager.playSound(80, false, "achievement.mp3");

        SeedPacket[] newBank = new SeedPacket[3];
        for (int i = 0; i < 3; i++) {
            int rnd = Greenfoot.getRandomNumber(total), cursor = 0;
            for (RupButton.RarityEntry e : pool) {
                if (e == null || e.weight <= 0) continue;
                cursor += e.weight;
                if (rnd < cursor) {
                    String name = e.packetClass.getSimpleName().replace("Packet", "").toUpperCase();
                    newBank[i] = PlantFactory.createSeedPacket(name);
                    break;
                }
            }
        }
        seedbank.updateBank(newBank);
    }

    public void checkAndCombine(Plant newPlant) {
        if (newPlant == null || newPlant.isMerging || newPlant.isTarget) return;
        if (!(newPlant instanceof Peashooter || newPlant instanceof Repeater
           || newPlant instanceof GatlingPea || newPlant instanceof Cactus
           || newPlant instanceof Cactus2 || newPlant instanceof BonkChoy
           || newPlant instanceof BonkChoy2)) return;

        List<Plant> available = new ArrayList<>();
        for (Plant p : getObjects(newPlant.getClass())) {
            if (!p.isMerging && !p.isTarget) available.add(p);
        }

        if (available.size() >= 3) {
            Plant p1 = available.get(0), p2 = available.get(1), p3 = available.get(2);
            p3.isTarget = true;
            p1.setMergingTarget(p3);
            p2.setMergingTarget(p3);
        }
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

    public void moveHitbox() {
        MouseInfo m = Greenfoot.getMouseInfo();
        if (m != null) hitbox.setLocation(m.getX(), m.getY());
    }

    public void started() { 
        if (!isGameOver) musicController.update(); 
    }

    public void stopped() { 
        AudioManager.stopBGM(); 
    }

    private void prepareLawnmowers() {
        int[][] coords = {{240,180},{236,240},{225,295},{220,360},{190,420}};
        for (int[] c : coords) addObject(new Lawnmower(), c[0], c[1]);
    }

    private void drawWaveUI() {
        if (level == null) return;
        GreenfootImage canvas = getBackground();
        String text = "WAVE " + level.getWaveNumber();
        int bw = 140, bh = 40, sx = 20, sy = 80;

        canvas.setColor(new Color(0, 0, 0, 160));
        canvas.fillRect(sx + 4, sy + 4, bw, bh);
        canvas.setColor(new Color(50, 50, 50));
        canvas.fillRect(sx, sy, bw, bh);
        canvas.setColor(Color.WHITE);
        canvas.drawRect(sx, sy, bw, bh);
        canvas.drawRect(sx + 1, sy + 1, bw - 2, bh - 2);
        canvas.setFont(new Font("Courier New", true, false, 22));
        canvas.setColor(Color.BLACK);
        canvas.drawString(text, sx + 22, sy + 29);
        canvas.setColor(new Color(0, 191, 255));
        canvas.drawString(text, sx + 20, sy + 27);
    }
}