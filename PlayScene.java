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
    
    public GridManager board = new GridManager();
    public GreenfootSound Grasswalk = new GreenfootSound("intro3.mp3");
    
    // Khai báo nhạc Final Wave
    public GreenfootSound finalWaveMusic = new GreenfootSound("finalwavemp3.mp3");
    private boolean isFinalMusicPlaying = false;

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
        
        addObject(board, 555, 349); 
        addObject(new ThuyThan(), 110, 642);
        addObject(seedbank, 0, 0); 
        addObject(hitbox, 555, 349);
        addObject(shovel, 930, 615);
        addObject(rollbutton, 250, 625);
        addObject(rupbutton, 250, 575);
        
        prepareLawnmowers();
        finalWaveMusic.setVolume(70);

        setPaintOrder(
            AugmentCard.class,
            Overlay.class,
            RupButton.class,
            RollButton.class,    
            RupButton.class,
            Transition.class,
            WaveNotification.class,
            ReadySetPlant.class,
            SunCounter.class,
            SeedPacket.class,
            SellShovel.class,
            Shovel.class,
            Sun.class,
            ThuyThan.class,
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
            addObject(new DelayAudio(Grasswalk, CYS, 70, true, 2000L), 0, 0);
            level.startLevel();
            isPlaying = true;                
        }
        checkMusicInGame();
        
        handleSunSpawn();
        handleWinLoss();
        checkDebugKeys();
    }

    private void checkMusicInGame() {
        if (level == null) return;
        
        int currentWave = level.getWaveNumber();
        showText("Wave: " + currentWave, 100, 100);
    
        if (currentWave >= 3 && currentWave <= 5) {
            if (!isFinalMusicPlaying) {
                
                if (Grasswalk.isPlaying()) Grasswalk.stop();
                if (CYS != null && CYS.isPlaying()) CYS.stop();
                
                finalWaveMusic.playLoop();
                isFinalMusicPlaying = true;
            }
        }
        else {
            if (isFinalMusicPlaying) {
                finalWaveMusic.stop();
                
                Grasswalk.playLoop(); 
                isFinalMusicPlaying = false;
            }
            
            if (!isFinalMusicPlaying && !Grasswalk.isPlaying() && isPlaying) {
                 Grasswalk.playLoop();
            }
        }
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
            Grasswalk.stop();
            finalWaveMusic.stop(); 
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
        int[][] coordinates = {{240, 180}, {228, 235}, {226, 300}, {200, 360}, {180, 430}};
        for (int i = 0; i < 5; i++) {
            addObject(new Lawnmower(), coordinates[i][0], coordinates[i][1]);
        }
    }

    public boolean tryPlacePlant(int gridX, int gridY, Plant newPlant) {
        if (newPlant == null || level.choosingCard) return false;
        return board.placePlant(gridX, gridY, newPlant);
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
        if (!(newPlant instanceof Peashooter || newPlant instanceof Sunflower || newPlant instanceof Repeater || newPlant instanceof Cactus || 
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
            CYS.stop(); 
            Grasswalk.stop(); 
            finalWaveMusic.stop(); 
            Greenfoot.setWorld(new Arena()); 
        }
        if ("r".equals(key)) rollPackets(); 
    }

    public void finishLevel() {
        Grasswalk.stop();
        finalWaveMusic.stop(); 
        AudioPlayer.play(70, "winmusic.mp3");
    }

    public void started() {
        if (isFinalMusicPlaying) {
            finalWaveMusic.playLoop();
        } else {
            if (!Grasswalk.isPlaying()) Grasswalk.playLoop();
        }
        Greenfoot.setSpeed(50);          
    }

    public void stopped() {
        if (Grasswalk.isPlaying()) Grasswalk.pause();
        if (finalWaveMusic.isPlaying()) finalWaveMusic.pause();
    }
}