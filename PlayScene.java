import greenfoot.*; 
import java.util.*;
import java.util.List;
import java.util.ArrayList;

public class PlayScene extends World {  
    private long lastSunSpawnTime;
    private boolean isPlaying = false;
    public boolean lose = false;
    public boolean loseOnce = false;
    public boolean winOnce = false;
    private boolean isWaterMap = true; 
    
    public GridManager GridManager = new GridManager();
    public GreenfootSound Grasswalk = new GreenfootSound("intro3.mp3");
    public GreenfootSound easyWaveMusic = new GreenfootSound("sans.mp3");
    public GreenfootSound finalWaveMusic = new GreenfootSound("finalwavemp3.mp3");
    
    private GreenfootSound currentlyPlaying;

    public GreenfootSound CYS;
    public World restartWorld;
    public FallingObject winPlant;
    
    public SeedPacket[] bank = {};
    public SeedBank seedbank; 
    public Hitbox hitbox = new Hitbox();
    public Shovel shovel = new Shovel();
    
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
        lastSunSpawnTime = System.currentTimeMillis();
        
        Greenfoot.setSpeed(50);
        setBackground("maptft2.png");
        
        addObject(GridManager, 555, 349); 
        addObject(new ThuyThan(), 110, 642);
        addObject(seedbank, 0, 0); 
        addObject(hitbox, 555, 349);
        addObject(shovel, 930, 615);
        addObject(rollbutton, 250, 625);
        addObject(rupbutton, 250, 575);
        
        prepareLawnmowers();
        finalWaveMusic.setVolume(70);
        easyWaveMusic.setVolume(70);

        setPaintOrder(
            AugmentCard.class,
            Overlay.class,
            ThuyThan.class,
            RupButton.class,
            RollButton.class,    
            Transition.class,
            WaveNotification.class,
            ReadySetPlant.class,
            SunCounter.class,
            SeedPacket.class,
            SellShovel.class,
            Shovel.class,
            Sun.class,
            
            HealthBar.class,
            Plant.class,          
            GridManager.class,   
            Zombie.class,
            Projectile.class,
            Dirt.class,
            Lawnmower.class
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
        
        int currentWave = level.getWaveNumber()-1;
        showText("Wave: " + currentWave, 100, 100);

        GreenfootSound targetMusic;

        if (currentWave >= 0 && currentWave <= 6) {
            targetMusic = easyWaveMusic;
        } else if (currentWave >= 12 && currentWave <= 16) {
            targetMusic = finalWaveMusic;
        } else {
            targetMusic = Grasswalk;
        }

        if (currentlyPlaying != targetMusic) {
            stopAllMusic();
            currentlyPlaying = targetMusic;
            currentlyPlaying.playLoop();
        }
    }

    private void stopAllMusic() {
        if (Grasswalk.isPlaying()) Grasswalk.stop();
        if (easyWaveMusic.isPlaying()) easyWaveMusic.stop();
        if (finalWaveMusic.isPlaying()) finalWaveMusic.stop();
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
        RupButton rup = rupbutton;
        if (seedbank.getSun() >= 25) {
            int totalWeight = 0;
            for (RupButton.RarityEntry entry : rup.weightedPool) {
                if (entry.weight > 0) totalWeight += entry.weight;
            }
            if (totalWeight <= 0) return; 

            seedbank.addSun(-25); 
            AudioPlayer.play(80, "achievement.mp3");

            SeedPacket[] newBank = new SeedPacket[3]; 
            for (int i = 0; i < 3; i++) {
                int randomNumber = Greenfoot.getRandomNumber(totalWeight);
                int cursor = 0;
                for (RupButton.RarityEntry entry : rup.weightedPool) {
                    if (entry.weight <= 0) continue;
                    cursor += entry.weight;
                    if (randomNumber < cursor) {
                        try {
                            newBank[i] = (SeedPacket) entry.packetClass.getDeclaredConstructor().newInstance();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
            }
            seedbank.updateBank(newBank);
        }
    }

    private void handleWinLoss() {
        if (!loseOnce && hasLost()) {
            stopAllMusic();
            AudioPlayer.play(80, "losemusic.mp3");
            addObject(new DelayAudio(new GreenfootSound("scream.mp3"), 70, false, 4000L), 0, 0);
            loseOnce = true;
            Greenfoot.delay(250);
            addObject(new Transition(false, new ResultScreen(restartWorld), "gameover.png", 5), 365, 215);
        } else if (!winOnce && hasWon()) {
            winOnce = true;
            finishLevel();
            addObject(winPlant, Greenfoot.getRandomNumber(266) + 400, 215);
        }
    }

    private void prepareLawnmowers() {
        int[][] coordinates = {{240, 180}, {226, 300}, {180, 430}};
        for (int i = 0; i < 3; i++) {
            addObject(new Lawnmower(), coordinates[i][0], coordinates[i][1]);
        }
    }

    public boolean tryPlacePlant(int gridX, int gridY, Plant newPlant) {
        if (newPlant == null || level.choosingCard) return false;
        return GridManager.placePlant(gridX, gridY, newPlant);
    }

    public boolean hasLost() {
        List<Zombie> zombies = getObjects(Zombie.class);
        for (Zombie i : zombies) {
            if (i.getWorld() != null && i.getX() < 125) return true;
        }
        return false;
    }
    
    public void checkAndCombine(Plant newPlant) {
        if (newPlant == null || newPlant.isMerging || newPlant.isTarget) return;
        if (!(newPlant instanceof Peashooter|| newPlant instanceof Repeater ||  newPlant instanceof GatlingPea || newPlant instanceof Cactus || 
              newPlant instanceof Cactus2 || newPlant instanceof BonkChoy || newPlant instanceof BonkChoy2)) {
            return; 
        }
    
        List<? extends Plant> plants = getObjects(newPlant.getClass());
        List<Plant> available = new ArrayList<>();
        for (Plant p : plants) {
            if (!p.isMerging && !p.isTarget) available.add(p);
        }
    
        if (available.size() >= 3) {
            Plant p1 = available.get(0);
            Plant p2 = available.get(1);
            Plant p3 = available.get(2);
            p3.isTarget = true; 
            p1.setMergingTarget(p3);
            p2.setMergingTarget(p3);
        }
    }

    public boolean hasWon() { return level.hasWon(); }

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
        AudioPlayer.play(70, "winmusic.mp3");
    }

    public void started() {
        if (currentlyPlaying != null) {
            currentlyPlaying.playLoop();
        }
        Greenfoot.setSpeed(50);          
    }

    public void stopped() {
        if (currentlyPlaying != null && currentlyPlaying.isPlaying()) {
            currentlyPlaying.pause();
        }
    }
}