import greenfoot.*;
import java.util.*;

public class WaveManager extends Actor {
    public boolean choosingCard = false;
    public boolean finishedSending = false;
    public long lastFrame = System.nanoTime();
    public long currentFrame, deltaTime;
    public Zombie[][][] level; 
    public long waveTime, firstWave;
    public int wave = -1;
    public int[] hugeWaves;
    public PlayScene playScene;
    public boolean isFirstWave;
    private boolean sunSpawnedForThisWave = false;
    public ArrayList<ArrayList<Zombie>> zombieRow = new ArrayList<ArrayList<Zombie>>();
    public static final int xOffset = 950;

    public WaveManager(long timeBetweenWaves, Zombie[][][] level, long firstWave, boolean startAsFirst, int... hugeWaves) {
        this.level = level;
        this.waveTime = timeBetweenWaves;
        this.firstWave = firstWave;
        this.hugeWaves = hugeWaves;
        this.isFirstWave = startAsFirst;
        for (int i = 0; i < 6; i++) {
            zombieRow.add(new ArrayList<Zombie>());
        }
    }

    public void startLevel() {
        wave = 0;
        AudioManager.playSound(80, false, "readysetplant.mp3");
        if (getWorld() != null) {
            getWorld().addObject(new ReadySetPlant(), 620, 295);
        }
        lastFrame = System.nanoTime();
    }

    public void act() {
        if (choosingCard || wave == -1) {
            lastFrame = System.nanoTime();
            return;
        }

        currentFrame = System.nanoTime();
        deltaTime = (currentFrame - lastFrame) / 1000000;

        if (wave > level.length - 1) {
            if (playScene.getObjects(Zombie.class).isEmpty()) {
                finishedSending = true;
            }
            return;
        }

        long dynamicTargetTime = isFirstWave ? firstWave : 6000L;
        
        if (playScene.getObjects(Zombie.class).isEmpty()) {
            if (deltaTime >= 1000 && !sunSpawnedForThisWave && !isFirstWave) {
                playScene.addObject(new Sun(100, true), 900, 300);
                sunSpawnedForThisWave = true;
            }

            if (deltaTime >= dynamicTargetTime) {
                if (isFirstWave) {
                    AudioManager.playSound(80,false, "awooga.mp3");
                    isFirstWave = false;
                }
                checkAndPrepareWave();
                sunSpawnedForThisWave = false;
                lastFrame = System.nanoTime();
            }
        } else {
            lastFrame = System.nanoTime();
        }
    }

    private void checkAndPrepareWave() {
        if (wave >= level.length) return;
        boolean isHuge = false;
        for (int i : hugeWaves) {
            if (i == wave) {
                isHuge = true;
                break;
            }
        }
        if (isHuge) triggerCardSelection();
        else {
            sendWave(level[wave]);
            wave++;
        }
    }

    private void triggerCardSelection() {
        choosingCard = true;
        playScene.addObject(new Overlay(1111, 698), 555, 349);
        
        String[] pool = {"rerollcard", "TD", "HM"};
        for (int i = 0; i < 3; i++) {
            int xPos = 255 + (i * 300); 
            int yPos = 349; 
            playScene.addObject(new AugmentCard(this, pool[i], null), xPos, yPos);
        }
        
        playScene.setPaintOrder(AugmentCard.class, Overlay.class, WaveNotification.class, GridManager.class, Plant.class, Zombie.class);
        AudioManager.playSound(70, false, "hugewave.mp3");
    }

    public void nextWave() {
        if (playScene != null) {
            playScene.removeObjects(playScene.getObjects(Overlay.class));
            playScene.removeObjects(playScene.getObjects(AugmentCard.class));
            
            playScene.setPaintOrder(
                Transition.class, 
                WaveNotification.class, 
                AugmentCard.class,
                Overlay.class,
                RupButton.class, 
                RollButton.class, 
                SeedPacket.class, 
                Shovel.class, 
                Sun.class,
                Plant.class, 
                GridManager.class
            );
        }
        
        if (wave < level.length) {
            sendHugeWave(level[wave]);
            playScene.addObject(new WaveNotification(wave == level.length - 1), 360, 215);
            wave++;
        }
        
        this.choosingCard = false; 
        this.lastFrame = System.nanoTime();
    }

    public void sendWave(Zombie[][] waveData) {
        spawnZombies(waveData, false);
    }

    public void sendHugeWave(Zombie[][] waveData) {
        spawnZombies(waveData, true);
    }

    private void spawnZombies(Zombie[][] waveData, boolean isHuge) {
        GridManager board = playScene.GridManager;
        for (int rowIndex = 0; rowIndex < waveData.length; rowIndex++) {
            Zombie[] zombiesInRow = waveData[rowIndex];
            if (zombiesInRow != null) {
                for (int j = 0; j < zombiesInRow.length; j++) {
                    Zombie z = zombiesInRow[j];
                    if (z != null) {
                        int row = board.clampRow(rowIndex);
                        int targetY = board.getYCoord(8, row);
                        int yOffsetAdjustment = 20; 
                        targetY -= yOffsetAdjustment;
                        int targetX = xOffset + (isHuge ? 100 : 0) + (j * 40);
                        playScene.addObject(z, targetX, targetY);
                        zombieRow.get(row).add(z);
                    }
                }
            }
        }
    }

    public void fixOrder() {}

    @Override
    protected void addedToWorld(World world) {
        playScene = (PlayScene) world;
        playScene.addObject(new Progress(this), 490, 25);
    }

    public boolean hasWon() {
        return (wave >= level.length && playScene.getObjects(Zombie.class).isEmpty());
    }
    
    public int getWaveNumber() {
        return (wave < 0) ? 0 : (wave + 1);
    }
}