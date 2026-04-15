import greenfoot.*;
import java.util.*;

public class WaveManager extends Actor
{
    public long currentFrame = System.nanoTime();
    public static final int xOffset = 1115;
    
    public ArrayList<ArrayList<Zombie>> zombieRow = new ArrayList<ArrayList<Zombie>>();
    
    public long lastFrame = System.nanoTime();
    public Zombie[][] level;
    public long levelTime;
    public long waveTime;
    public long firstWave;
    public long deltaTime;

    public boolean won = false;
    public PlayScene PlayScene;
    public int wave = -1;
    public boolean first = false;
    public boolean finishedSending = false;
    public int[] hugeWaves;
    
    public WaveManager(long timeBetweenWaves, Zombie[][] level, long firstWave, boolean first, int... hugeWaves) {
        this.level = level;
        this.waveTime = timeBetweenWaves;
        this.firstWave = firstWave;
        this.hugeWaves = hugeWaves;
        this.first = first;

        for (int i = 0; i < 6; i++) { 
            zombieRow.add(new ArrayList<Zombie>());
        }
    }
    
    public void startLevel() {
        wave = 0;
        AudioPlayer.play(80, "readysetplant.mp3");
        if (getWorld() != null) {
            getWorld().addObject(new ReadySetPlant(), 620, 295);
        }
    }
    
    public void fixOrder() {
        Board board = (Board)PlayScene.getObjects(Board.class).get(0);
        List<Zombie> zombies = PlayScene.getObjects(Zombie.class);
        for (int r = 0; r < board.currentRowCount; r++) { 
            for (Zombie z : zombies) {
                if (z.getWorld() != null && z.getYPos() == r) {
                    int x = z.getX();
                    int y = z.getY();
                    PlayScene.removeObject(z);
                    PlayScene.addObject(z, x, y);
                }
            }
        }
    }
    
    public void act() {
        if (wave != -1) {
            currentFrame = System.nanoTime();
            deltaTime = (currentFrame - lastFrame) / 1000000;
        } else {
            lastFrame = System.nanoTime();
        }

        if (wave != -1 && wave > level.length - 1) {
            PlayScene.addObject(new finishedSending(this, 15000L), 0, 0);
            wave = -1;
            return;
        }
        
        if (wave != -1) {
            long targetTime = first ? firstWave : waveTime;
            if (deltaTime >= targetTime || PlayScene.getObjects(Zombie.class).size() == 0) {
                if (first) AudioPlayer.play(80, "awooga.mp3");
                checkSendWave();
                wave++;
                lastFrame = System.nanoTime();
                first = false;
            }
        }
    }

    public void checkSendWave() {
        if (wave >= level.length) return;
        
        for (int i : hugeWaves) {
            if (i == wave) {
                AudioPlayer.play(70, "hugewave.mp3");
                finishedSending = false;
                sendHugeWave(level[wave]);
                PlayScene.addObject(new AHugeWave(wave == level.length - 1), 360, 215);
                return;       
            }
        }
        sendWave(level[wave]);
    }
    
    public void sendWave(Zombie[] waveData) {
        Board board = (Board)PlayScene.getObjects(Board.class).get(0);
        int rows = board.currentRowCount; 
        for (int i = 0; i < waveData.length; i++) {
            if (waveData[i] != null) {
                int rowIndex = i % rows; 
                int wait = i / rows;
                int offset = xOffset + wait * 20;
                int targetY = rowIndex * board.ySpacing + board.yOffset;
                
                PlayScene.addObject(waveData[i], offset, targetY);
                zombieRow.get(rowIndex).add(waveData[i]);
                finishedSending = false;
            }
        }
        PlayScene.addObject(new FixOrder(this, 1000L), 0, 0);
    }

    public void sendHugeWave(Zombie[] waveData) {
        Board board = (Board)PlayScene.getObjects(Board.class).get(0);
        int rows = board.currentRowCount;
        for (int i = 0; i < waveData.length; i++) {
            if (waveData[i] != null) {
                int rowIndex = i % rows;
                int wait = i / rows;
                int offset = xOffset + 50 + wait * 20;
                int targetY = rowIndex * board.ySpacing + board.yOffset;
                
                PlayScene.addObject(waveData[i], offset, targetY);
                zombieRow.get(rowIndex).add(waveData[i]);
            }
        }
        PlayScene.addObject(new FixOrder(this, 1000L), 0, 0);
    }

    public boolean hasWon() {
        return (wave == -1 && PlayScene.getObjects(Zombie.class).size() == 0);
    }

    @Override
    protected void addedToWorld(World world) {
        PlayScene = (PlayScene)world;
        lastFrame = System.nanoTime();
        currentFrame = System.nanoTime();
        getWorld().addObject(new Progress(this), 490, 25);
    }
}