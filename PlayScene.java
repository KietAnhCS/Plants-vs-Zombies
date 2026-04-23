import greenfoot.*; 
import java.util.*;

public class PlayScene extends World {  
    
    private int rollLevel = 1;
    private boolean isPlaying = false;
    public boolean lose = false;
    public boolean loseOnce = false;
    public boolean winOnce = false;
    private boolean isWaterMap = true; 
    
    public GridManager board = new GridManager();
    public GreenfootSound Grasswalk = new GreenfootSound("intro3.mp3");
    
    public GreenfootSound CYS;
    public World restartWorld;
    public FallingObject winPlant;
    
    public Zombie n = null;
    
    public SeedPacket[] bank = {
        new SunflowerPacket(), 
        new PeashooterPacket(), 
        new WalnutPacket(), 
        new CactusPacket(), 
        new TwinSunflowerPacket()
    };

    private class RarityEntry {
        Class packetClass;
        int weight;
        RarityEntry(Class packetClass, int weight) {
            this.packetClass = packetClass;
            this.weight = weight;
        }
    }
    
    private RarityEntry[] weightedPool = {
        new RarityEntry(SunflowerPacket.class, 0),   
        new RarityEntry(PeashooterPacket.class, 3),
        new RarityEntry(WalnutPacket.class, 2),
        new RarityEntry(CactusPacket.class, 10),
        new RarityEntry(BonkchoyPacket.class, 2),
        new RarityEntry(TorchwoodPacket.class, 0), 
        new RarityEntry(PotatoPacket.class, 1),
        new RarityEntry(RepeaterPacket.class, 0),
        new RarityEntry(GatlingPeaPacket.class, 0)
    };
    
    public SeedBank seedbank = new SeedBank(bank);   
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
        
        Greenfoot.setSpeed(50);
        
        setBackground("maptft.png");
        
        addObject(new ThuyThan(), 110, 642);
        addObject(seedbank, 0, 0);
        addObject(board, 0, 0);
        
        addObject(hitbox, 0, 0);
        addObject(shovel, 1052, 537);
       
        addObject(rollbutton, 277, 679);
        addObject(rupbutton, 278, 638);
        
        prepareLawnmowers();

        setPaintOrder(
            AugmentCard.class, Overlay.class, BonkChoy.class, Transition.class, WaveNotification.class, ReadySetPlant.class, 
            SunCounter.class, ThuyThan.class, clickShovel.class, Shovel.class, Lawnmower.class, 
            TransparentObject.class, SeedPacket.class, FallingSun.class, 
            Sun.class, Dirt.class, Projectile.class, FallingObject.class, 
            HealthBar.class, Zombie.class, fallingZombie.class, Explosion.class, Plant.class, GridManager.class
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
        handleWinLoss();
        checkDebugKeys();
    }
    
    public void rollPackets() {
        int currentSuns = seedbank.getSun(); 
        if (currentSuns >= 100) {
            int totalWeight = 0;
            for (RarityEntry entry : weightedPool) {
                if (entry.weight > 0) totalWeight += entry.weight;
            }
    
            if (totalWeight <= 0) return; 
    
            seedbank.addSun(-100); 
            SeedPacket[] newBank = new SeedPacket[5]; 
    
            for (int i = 0; i < 5; i++) {
                int randomNumber = Greenfoot.getRandomNumber(totalWeight);
                int cursor = 0;
                for (RarityEntry entry : weightedPool) {
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
            this.bank = newBank;
            seedbank.updateBank(newBank); 
        }
    }
    
    public void upgradeProbabilities() {
        if (rollLevel < 5) { 
            rollLevel++;
            for (RarityEntry entry : weightedPool) {
                if (entry.packetClass == SunflowerPacket.class) {
                    if (rollLevel >= 3) entry.weight = 5;
                }
                if (entry.packetClass == PotatoPacket.class) {
                    if (rollLevel >= 4) entry.weight = 2;
                }
                if (entry.packetClass == TwinSunflowerPacket.class) {
                    if (rollLevel >= 3) entry.weight = 3;
                }
                if (entry.packetClass == GatlingPeaPacket.class) {
                    if (rollLevel == 5) entry.weight = 3;
                }
                if (entry.packetClass == RepeaterPacket.class) {
                    if (rollLevel == 5) entry.weight = 3;
                }
                if (entry.packetClass == PeashooterPacket.class) {
                    if (rollLevel == 5) entry.weight = 0;
                }
                if (entry.packetClass == CactusPacket.class) {
                    if (rollLevel >= 4) entry.weight = 0;
                }
                if (entry.packetClass == TorchwoodPacket.class) {
                    if (rollLevel >= 4) entry.weight = 3;
                }
            }
        }
    }

    private void handleWinLoss() {
        if (!loseOnce && hasLost()) {
            Grasswalk.stop();
            AudioPlayer.play(80, "losemusic.mp3");
            addObject(new DelayAudio(new GreenfootSound("scream.mp3"), 70, false, 4000L), 0, 0);
            loseOnce = true;
            Greenfoot.delay(250);
            addObject(new Transition(false, new ResultScreen(restartWorld), "gameover.png", 5), 365, 215);
        } else if (!winOnce && hasWon()) {
            winOnce = true;
            finishLevel();
            addObject(winPlant, Random.Int(SeedBank.x1, SeedBank.x2), 215);
        }
    }

    private void prepareLawnmowers() {
        int[][] coordinates = {
            {250, 175}, 
            {238, 228}, 
            {226, 293}, 
            {200, 358}, 
            {180, 436}  
        };

        for (int i = 0; i < 5; i++) {
            if (i < coordinates.length) {
                addObject(new Lawnmower(), coordinates[i][0], coordinates[i][1]);
            }  
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

    public boolean hasWon() {
        return level.hasWon();
    }

    public void moveHitbox() {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse != null) hitbox.setLocation(mouse.getX(), mouse.getY());
    }

    private void checkDebugKeys() {
        String key = Greenfoot.getKey();
        if (key == null) return;
        if (key.equals("1")) { CYS.stop(); Grasswalk.stop(); Greenfoot.setWorld(new CinematicIntro()); }
        if (key.equals("r")) rollPackets(); 
    }

    public void finishLevel() {
        Grasswalk.stop();
        AudioPlayer.play(70, "winmusic.mp3");
    }

    public void started() {
        if (!Grasswalk.isPlaying()) Grasswalk.playLoop();
        Greenfoot.setSpeed(50);          
    }

    public void stopped() {
        if (Grasswalk.isPlaying()) Grasswalk.pause();
    }
}