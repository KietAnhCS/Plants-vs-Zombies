import greenfoot.*;
import java.util.*;

public class PlayScene extends World {
    private SunManager sunManager = new SunManager();
    public SunDisplay sunDisplay = new SunDisplay();
    private long lastSunSpawnTime;
    private boolean isPlaying = false;
    public boolean lose = false;
    public boolean loseOnce = false;
    public boolean winOnce = false;
    private boolean isWaterMap = true;
    public GridManager GridManager = new GridManager();
    private String currentBGM = "";
    public GreenfootSound CYS;
    public World restartWorld;
    public FallingObject winPlant;
    public SeedPacket[] bank = {};
    public SeedBank seedbank;
    public Hitbox hitbox = new Hitbox();
    public Shovel shovel = new Shovel();
    public MuteButton mutebutton = new MuteButton();
    public RollButton rollbutton = new RollButton();
    public RupButton rupbutton = new RupButton();
    public WaveManager level;

    public PlayScene(GreenfootSound CYS, WaveManager level, SeedBank seedbank, World restartWorld, FallingObject winPlant, boolean isWater) {
        super(1111, 698, 1, false);
        this.isWaterMap = isWater;
        this.CYS = CYS;
        this.seedbank = seedbank;
        this.restartWorld = restartWorld;
        this.level = level;
        this.winPlant = winPlant;
        this.lastSunSpawnTime = System.currentTimeMillis();
        
        Greenfoot.setSpeed(50);
        setBackground("maptft2.png");
        
        addObject(new SliderBar(), 850, 50);
        addObject(mutebutton, 1050, 50);
        addObject(GridManager, 555, 349);
        addObject(new ThuyThan(), 110, 642);
        addObject(seedbank, 0, 0);
        addObject(sunDisplay, 600, 600);
        addObject(hitbox, 555, 349);
        addObject(shovel, 930, 615);
        addObject(rollbutton, 250, 625);
        addObject(rupbutton, 250, 575);
        
        prepareLawnmowers();
        applyDefaultPaintOrder();
    }

    public void applyDefaultPaintOrder() {
        setPaintOrder(
            SliderKnob.class, SliderBar.class, MuteButton.class, AugmentCard.class,
            Overlay.class, ThuyThan.class, RupButton.class, RollButton.class,
            Transition.class, WaveNotification.class, ReadySetPlant.class,
            SunDisplay.class, SeedPacket.class, SellShovel.class, Shovel.class,
            Sun.class, HealthBar.class, Pea.class, FirePea.class, Needle.class,
            Plant.class, GridManager.class, Zombie.class, Dirt.class, Lawnmower.class
        );
    }

    public void act() {
        moveHitbox();
        if (!isPlaying) {
            addObject(level, 0, 0);
            isPlaying = true;
            level.startLevel();
        }
        updateGameMusic();
        handleSunSpawn();
        handleWinLoss();
        checkDebugKeys();
    }

    private void updateGameMusic() {
        if (level == null || loseOnce || winOnce) return;
        int currentWave = level.getWaveNumber() - 1;
        showText("Wave: " + currentWave, 100, 100);

        String targetMusic;
        if (currentWave >= 0 && currentWave <= 6) {
            targetMusic = "sans.mp3";
        } else if (currentWave >= 12 && currentWave <= 16) {
            targetMusic = "finalwavemp3.mp3";
        } else {
            targetMusic = "intro3.mp3";
        }

        if (!currentBGM.equals(targetMusic)) {
            currentBGM = targetMusic;
            AudioManager.playBGM(targetMusic);
        }
    }

    private void stopAllMusic() {
        AudioManager.stopBGM();
        if (CYS != null && CYS.isPlaying()) CYS.stop();
    }

    private void handleSunSpawn() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastSunSpawnTime >= 30000) {
            lastSunSpawnTime = currentTime;
            int x = Greenfoot.getRandomNumber(700) + 200;
            addObject(new FallingSun(), x, -30);
        }
    }

    public void rollPackets() {
        if (getSunManager().hasEnough(25)) {
            RupButton.RarityEntry[] currentPool = rupbutton.getPoolForRoll();
            int totalWeight = 0;
            for (RupButton.RarityEntry entry : currentPool) {
                if (entry != null && entry.weight > 0) totalWeight += entry.weight;
            }
            if (totalWeight <= 0) return;

            getSunManager().spend(25);
            AudioManager.playSound(80, false, "achievement.mp3");

            SeedPacket[] newBank = new SeedPacket[3];
            for (int i = 0; i < 3; i++) {
                int randomNumber = Greenfoot.getRandomNumber(totalWeight);
                int cursor = 0;
                for (RupButton.RarityEntry entry : currentPool) {
                    if (entry == null || entry.weight <= 0) continue;
                    cursor += entry.weight;
                    if (randomNumber < cursor) {
                        String typeName = entry.packetClass.getSimpleName().replace("Packet", "").toUpperCase();
                        newBank[i] = PlantFactory.createSeedPacket(typeName);
                        break;
                    }
                }
            }
            seedbank.updateBank(newBank);
        }
    }

    private void handleWinLoss() {
        if (!loseOnce && hasLost()) {
            loseOnce = true;
            stopAllMusic();
            AudioManager.playSound(80, false, "losemusic.mp3");
            addObject(new DelayAudio("scream.mp3", 70, false, 4000L), 0, 0);
            addObject(new Transition(false, new ResultScreen(restartWorld), "gameover.png", 5), 365, 215);
        } else if (!winOnce && hasWon()) {
            winOnce = true;
            finishLevel();
            addObject(winPlant, Greenfoot.getRandomNumber(266) + 400, 215);
        }
    }

    private void prepareLawnmowers() {
        int[][] coords = {{240, 180}, {226, 300}, {180, 430}};
        for (int i = 0; i < 3; i++) {
            addObject(new Lawnmower(), coords[i][0], coords[i][1]);
        }
    }

    public boolean tryPlacePlant(int gridX, int gridY, Plant newPlant) {
        if (newPlant == null || level.choosingCard) return false;
        return GridManager.placePlant(gridX, gridY, newPlant);
    }

    public boolean hasLost() {
        List<Zombie> zombies = getObjects(Zombie.class);
        for (Zombie z : zombies) {
            if (z.getWorld() != null && z.getX() < 125) return true;
        }
        return false;
    }

    public void checkAndCombine(Plant newPlant) {
        if (newPlant == null || newPlant.isMerging || newPlant.isTarget) return;
        
        List<? extends Plant> plants = getObjects(newPlant.getClass());
        List<Plant> available = new ArrayList<>();
        for (Plant p : plants) {
            if (!p.isMerging && !p.isTarget) available.add(p);
        }

        if (available.size() >= 3) {
            Plant p3 = available.get(2);
            p3.isTarget = true;
            available.get(0).setMergingTarget(p3);
            available.get(1).setMergingTarget(p3);
        }
    }

    public boolean hasWon() {
        return level != null && level.hasWon();
    }

    public void moveHitbox() {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse != null) hitbox.setLocation(mouse.getX(), mouse.getY());
    }

    private void checkDebugKeys() {
        String key = Greenfoot.getKey();
        if ("1".equals(key)) {
            stopAllMusic();
            Greenfoot.setWorld(new Arena());
        }
        if ("r".equals(key)) rollPackets();
    }

    public void finishLevel() {
        stopAllMusic();
        AudioManager.playSound(80, false, "winmusic.mp3");
    }

    public void started() {
        updateGameMusic();
        Greenfoot.setSpeed(50);
    }

    public void stopped() {
        AudioManager.stopBGM();
    }

    public SunManager getSunManager() {
        return sunManager;
    }
}