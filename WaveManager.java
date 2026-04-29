import greenfoot.*;
import java.util.*;

public class WaveManager extends Actor {
    public boolean choosingCard = false;
    public boolean finishedSending = false;
    public long lastFrame = System.nanoTime();
    
    public String[][][] levelData; 
    public long waveTime, firstWave;
    public int wave = -1;
    public int[] hugeWaves;
    public PlayScene playScene;
    public boolean isFirstWave;
    private boolean sunSpawnedForThisWave = false;
    public ArrayList<ArrayList<Zombie>> zombieRow = new ArrayList<ArrayList<Zombie>>();
    public static final int xOffset = 950;

    public WaveManager(long timeBetweenWaves, String[][][] levelData, long firstWave, boolean startAsFirst, int... hugeWaves) {
        this.levelData = levelData;
        this.waveTime = timeBetweenWaves;
        this.firstWave = firstWave;
        this.hugeWaves = hugeWaves;
        this.isFirstWave = startAsFirst;
        for (int i = 0; i < 6; i++) zombieRow.add(new ArrayList<Zombie>());
    }

    public void startLevel() {
        wave = 0;
        AudioManager.playSound(80, false, "readysetplant.mp3");
        if (getWorld() != null) getWorld().addObject(new ReadySetPlant(), 620, 295);
        lastFrame = System.nanoTime();
    }

    public void act() {
        if (choosingCard || wave == -1 || playScene == null) {
            lastFrame = System.nanoTime();
            return;
        }

        long deltaTime = (System.nanoTime() - lastFrame) / 1000000;

        if (wave >= levelData.length) {
            if (playScene.getObjects(Zombie.class).isEmpty()) finishedSending = true;
            return;
        }

        if (playScene.getObjects(Zombie.class).isEmpty()) {
            if (deltaTime >= 1000 && !sunSpawnedForThisWave && !isFirstWave) {
                playScene.addObject(new Sun(100, true), 900, 300);
                sunSpawnedForThisWave = true;
            }

            if (deltaTime >= (isFirstWave ? firstWave : 6000L)) {
                if (isFirstWave) {
                    AudioManager.playSound(80, false, "awooga.mp3");
                    isFirstWave = false;
                }
                processNextWave();
                sunSpawnedForThisWave = false;
                lastFrame = System.nanoTime();
            }
        } else {
            lastFrame = System.nanoTime();
        }
    }

    private void processNextWave() {
        boolean isHuge = false;
        for (int i : hugeWaves) if (i == wave) { isHuge = true; break; }

        if (isHuge) triggerCardSelection();
        else spawnZombies(levelData[wave++], false);
    }

    private void triggerCardSelection() {
        choosingCard = true;
        playScene.addObject(new Overlay(1111, 698), 555, 349);
        String[] pool = {"rerollcard", "TD", "HM"};
        for (int i = 0; i < 3; i++) playScene.addObject(new AugmentCard(this, pool[i], null), 255 + (i * 300), 349);
        
        fixOrder();
        AudioManager.playSound(70, false, "hugewave.mp3");
    }

    public void nextWave() {
        playScene.removeObjects(playScene.getObjects(Overlay.class));
        playScene.removeObjects(playScene.getObjects(AugmentCard.class));
        
        spawnZombies(levelData[wave], true);
        playScene.addObject(new WaveNotification(wave == levelData.length - 1), 360, 215);
        
        wave++;
        choosingCard = false; 
        lastFrame = System.nanoTime();
    }

    private void spawnZombies(String[][] waveData, boolean isHuge) {
        if (waveData == null) return;
        GridManager board = playScene.GridManager;
        
        for (int r = 0; r < waveData.length; r++) {
            if (waveData[r] == null) continue;
            for (int j = 0; j < waveData[r].length; j++) {
                Zombie z = ZombieFactory.createZombie(waveData[r][j]);
                if (z != null) {
                    int row = board.clampRow(r);
                    playScene.addObject(z, xOffset + (isHuge ? 150 : 0) + (j * 50), board.getYCoord(8, row) - 20);
                    zombieRow.get(row).add(z);
                }
            }
        }
        playScene.addObject(new FixOrder(this, 10), 0, 0);
    }

    public void fixOrder() {
        if (playScene != null) playScene.applyDefaultPaintOrder();
    }

    @Override
    protected void addedToWorld(World world) {
        playScene = (PlayScene) world;
        playScene.addObject(new Progress(this), 490, 25);
    }

    public boolean hasWon() {
        return (wave >= levelData.length && playScene.getObjects(Zombie.class).isEmpty());
    }

    public int getWaveNumber() {
        return Math.max(0, wave + 1);
    }
}