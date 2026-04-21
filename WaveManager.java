import greenfoot.*;
import java.util.*;

public class WaveManager extends Actor
{
    public boolean choosingCard = false;
    public boolean finishedSending = false; 
    public long lastFrame = System.nanoTime();
    public long currentFrame, deltaTime;
    
    public Zombie[][] level;
    public long waveTime, firstWave;
    public int wave = -1;
    public int[] hugeWaves;
    public PlayScene PlayScene;
    public boolean isFirstWave; 
    
    private boolean sunSpawnedForThisWave = false;
    
    public ArrayList<ArrayList<Zombie>> zombieRow = new ArrayList<ArrayList<Zombie>>();
    public static final int xOffset = 1115;

    public WaveManager(long timeBetweenWaves, Zombie[][] level, long firstWave, boolean startAsFirst, int... hugeWaves) {
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
        AudioPlayer.play(80, "readysetplant.mp3");
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
            if (PlayScene.getObjects(finishedSending.class).isEmpty()) {
                PlayScene.addObject(new finishedSending(this, 15000L), 0, 0);
            }
            wave = -1;
            return;
        }

        boolean currentWaveIsEmpty = true;
        for (Zombie z : level[wave]) {
            if (z != null) {
                currentWaveIsEmpty = false;
                break;
            }
        }

        long dynamicTargetTime;
        if (isFirstWave) {
            dynamicTargetTime = firstWave;
        } else if (currentWaveIsEmpty) {
            dynamicTargetTime = 10000L; 
        } else {
            dynamicTargetTime = 6000L; 
        }

        if (PlayScene.getObjects(Zombie.class).isEmpty()) {
            if (deltaTime >= 1000 && !sunSpawnedForThisWave && !isFirstWave && wave >= 0) {
                PlayScene.addObject(new Sun(50), 900, 300);
                sunSpawnedForThisWave = true;
            }

            if (deltaTime >= dynamicTargetTime) {
                if (isFirstWave) {
                    AudioPlayer.play(80, "awooga.mp3");
                    isFirstWave = false;
                }
                checkSendWave();
                wave++;
                sunSpawnedForThisWave = false;
                lastFrame = System.nanoTime();
            }
        } else {
            lastFrame = System.nanoTime();
            sunSpawnedForThisWave = false;
        }
    }

    public void checkSendWave() {
        if (wave >= level.length) return;
    
        boolean isHuge = false;
        for (int i : hugeWaves) {
            if (i == wave) {
                isHuge = true;
                break;
            }
        }
    
        if (isHuge) {
            if (wave == level.length - 1) {
                sendHugeWave(level[wave]);
                PlayScene.addObject(new AHugeWave(true), 360, 215);
                
                PlayScene.addObject(new FixOrder(this, 15000L) {
                    private long start = System.currentTimeMillis();
                    @Override
                    public void act() {
                        if (System.currentTimeMillis() - start >= 15000L) {
                            triggerCardSelection();
                            getWorld().removeObject(this);
                        }
                    }
                }, 0, 0);
            } else {
                PlayScene.addObject(new AHugeWave(false), 360, 215);
                PlayScene.addObject(new FixOrder(this, 5000L) {
                private long start = System.currentTimeMillis();
                @Override
                public void act() {
                    if (System.currentTimeMillis() - start >= 5000L) {
                        triggerCardSelection();
                        getWorld().removeObject(this);
                    }
                }
                }, 0, 0);
            }
        } else {
            sendWave(level[wave]);
        }
    }

    private void triggerCardSelection() {
        choosingCard = true;
        finishedSending = false; 
        PlayScene.addObject(new Overlay(PlayScene.getWidth(), PlayScene.getHeight()), PlayScene.getWidth()/2, PlayScene.getHeight()/2);
        
        PlayScene.addObject(new AugmentCard(this, "rerollcard"), 150, 350);
        PlayScene.addObject(new AugmentCard(this, "TD"), 450, 350);
        PlayScene.addObject(new AugmentCard(this, "HM"), 750, 350);
        
        AudioPlayer.play(70, "hugewave.mp3");
    }

    public void resumeAfterSelection() {
        PlayScene.removeObjects(PlayScene.getObjects(Overlay.class));
        PlayScene.removeObjects(PlayScene.getObjects(AugmentCard.class));
        
        sendHugeWave(level[wave]);
        PlayScene.addObject(new AHugeWave(wave == level.length - 1), 360, 215);
        
        choosingCard = false;
        lastFrame = System.nanoTime();
    }

    public void sendWave(Zombie[] waveData) {
        finishedSending = false;
        spawnZombies(waveData, false);
    }

    public void sendHugeWave(Zombie[] waveData) {
        finishedSending = false;
        spawnZombies(waveData, true);
    }

    private void spawnZombies(Zombie[] waveData, boolean isHuge) {
        Board board = (Board)PlayScene.getObjects(Board.class).get(0);
        int rows = board.currentRowCount;
        for (int i = 0; i < waveData.length; i++) {
            if (waveData[i] != null) {
                int rowIndex = i % rows;
                int offset = xOffset + (isHuge ? 50 : 0) + (i / rows) * 20;
                int targetY = rowIndex * board.ySpacing + board.yOffset - (isHuge ? 0 : 20);
                PlayScene.addObject(waveData[i], offset, targetY);
                zombieRow.get(rowIndex).add(waveData[i]);
            }
        }
        PlayScene.addObject(new FixOrder(this, 1000L), 0, 0);
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

    @Override
    protected void addedToWorld(World world) {
        PlayScene = (PlayScene)world;
        getWorld().addObject(new Progress(this), 490, 25);
    }

    public boolean hasWon() {
        return (wave == -1 && finishedSending && PlayScene.getObjects(Zombie.class).isEmpty());
    }
}