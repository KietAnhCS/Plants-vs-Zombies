import greenfoot.*; 
import java.util.*;

public class PlayScene extends World {  
    
    private int rollLevel = 1;
    private boolean isPlaying = false;
    public boolean lose = false;
    public boolean loseOnce = false;
    public boolean winOnce = false;
    private boolean isWaterMap = true; 
    
    public Board board = new Board();
    public GreenfootSound Grasswalk = new GreenfootSound("intro3.mp3");
    
    public GreenfootSound CYS;
    public World restartWorld;
    public FallingObject winPlant;
    
    public Zombie n = null;
    public Zombie[][] level1 = {
        {null, new BasicZombie(), null, null}, {n},
        {new BasicZombie(), null, null, null, null}, {n},
        {null, new BasicZombie(), null, new BasicZombie()}, {new BasicZombie()},
        {null, null, new Conehead(), null, null}, {n},
        {new BasicZombie(), new Conehead(), new BasicZombie(), new BasicZombie(), new BasicZombie(), n, new BasicZombie()}, {n},
        {new Conehead(), n, null, new BasicZombie(), null, null, new BasicZombie()},
        {new BasicZombie(), n, n, new BasicZombie(), null, new BasicZombie(), new BasicZombie()},
        {new Buckethead(), null, null, null, null},
        {n, new BasicZombie(), n, n, new Conehead(), n, n, new BasicZombie()},
        {null, new BasicZombie(), null, null, new Conehead(), n, n, new BasicZombie()},
        {new BasicZombie(), new BasicZombie(), new BasicZombie(), null, new Conehead()}, 
        {null, null, new BasicZombie(), null, null}, {n},
        {new Conehead(), new Conehead(), new Conehead(), new BasicZombie(), new BasicZombie(), new Buckethead(), null, new BasicZombie(), new Conehead(), new Buckethead()}
    };

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
        new RarityEntry(WalnutPacket.class, 0),
        new RarityEntry(CactusPacket.class, 10),
        new RarityEntry(TwinSunflowerPacket.class, 0),
        new RarityEntry(RepeaterPacket.class, 1),
        new RarityEntry(BonkchoyPacket.class, 0),
        new RarityEntry(TorchwoodPacket.class, 1),
        
        new RarityEntry(PotatoPacket.class, 2),
        new RarityEntry(GatlingPeaPacket.class, 0)
    
    };
    
    public SeedBank seedbank = new SeedBank(bank);   
    public Hitbox hitbox = new Hitbox();
    public Shovel shovel = new Shovel();
    public PlantFood plantfood = new PlantFood();
    public RollButton rollbutton = new RollButton();
    public RupButton rupbutton = new RupButton();
    public LilypadPacket lilypad = new LilypadPacket();
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
        
        if(isWaterMap) {
            setBackground("mapwater.png");
        } else {
            setBackground("maptft.png");
        }
        
        addObject(new ThuyThan(), 110, 642);
        addObject(seedbank, 0, 0);
        addObject(board, 0, 0);
        board.setupLayout(isWater);
        addObject(hitbox, 0, 0);
        addObject(shovel, 1052, 537);
        addObject(plantfood, 125, 550);
        addObject(rollbutton, 277, 679);
        addObject(rupbutton, 278, 638);
        
        
        
        prepareLawnmowers();

        setPaintOrder(
            AugmentCard.class,BonkChoy.class, Overlay.class, Setting.class, Transition.class, AHugeWave.class, ReadySetPlant.class, 
            SunCounter.class, ThuyThan.class, clickShovel.class, Shovel.class, Lawnmower.class, 
            TransparentObject.class, SeedPacket.class, FallingSun.class, 
            Sun.class, Dirt.class, Projectile.class, FallingObject.class, 
            Zombie.class, fallingZombie.class, Explosion.class, Plant.class, Board.class
        );
    }

    public void act() {
        checkEscape();
        
        if (!getObjects(Setting.class).isEmpty() || level.choosingCard) {
            moveHitbox(); 
            return; 
        }

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
        if (currentSuns >= 25) {
            seedbank.addSun(-25); 
            SeedPacket[] newBank = new SeedPacket[5]; 
            int totalWeight = 0;
            for (RarityEntry entry : weightedPool) totalWeight += entry.weight;

            for (int i = 0; i < 5; i++) {
                int randomNumber = Greenfoot.getRandomNumber(totalWeight);
                int cursor = 0;
                for (RarityEntry entry : weightedPool) {
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
        if (rollLevel < 9) { 
            rollLevel++;
            for (RarityEntry entry : weightedPool) {
                if (entry.packetClass == CactusPacket.class || entry.packetClass == PotatoPacket.class ) {
                    if (entry.weight > 1) entry.weight -= 1; 
                }
                if (entry.packetClass == TwinSunflowerPacket.class || entry.packetClass == RepeaterPacket.class ||entry.packetClass == SunflowerPacket.class ||entry.packetClass == WalnutPacket.class ||entry.packetClass == BonkchoyPacket.class ) {
                    entry.weight += 2;
                }
                if (entry.packetClass == GatlingPeaPacket.class || entry.packetClass == TorchwoodPacket.class) {
                    entry.weight += 1;
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
            {214, 375}, 
            {202, 436}  
        };
    

        int rowCount = isWaterMap ? 6 : 5; 
    
        for (int i = 0; i < rowCount; i++) {
            
            if (i < coordinates.length) {
                int xPos = coordinates[i][0];
                int yPos = coordinates[i][1];
                addObject(new Lawnmower(), xPos, yPos);
            } else if (isWaterMap) {
                
                addObject(new Lawnmower(), 170, 510); 
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

    private void checkEscape() {
        if ("escape".equals(Greenfoot.getKey())) {
            if (getObjects(Setting.class).isEmpty()) {
                addObject(new Overlay(getWidth(), getHeight()), getWidth()/2, getHeight()/2);
                addObject(new Setting(), getWidth() / 2, getHeight() / 2);
                Grasswalk.pause();
            } else {
                removeObjects(getObjects(Setting.class));
                removeObjects(getObjects(Overlay.class));
                Grasswalk.playLoop();
            }
        }
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