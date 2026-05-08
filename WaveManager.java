import greenfoot.*;
import java.util.*;

public class WaveManager extends Actor {
    private WaveState currentState = WaveState.BATTLE;
    private long stateTimer = 0;

    public String[][][] levelData;
    public long waveTime;
    public long firstWave;
    public int[] hugeWaves;
    public int daveWave;
    public boolean isFirstWave;

    public int wave = -1;
    public boolean choosingCard = false;
    public boolean finishedSending = false;

    private final ArrayList<ArrayList<Zombie>> spawnQueues = new ArrayList<>();
    private final long[] lastSpawnTime = new long[6];
    // zombieRow dùng để quản lý zombie theo hàng cho cây bắn
    public final ArrayList<ArrayList<Zombie>> zombieRow = new ArrayList<>();

    public PlayScene playScene;
    public static final int xOffset = 950;

    public WaveManager(long waveTime, String[][][] levelData,
                       long firstWave, boolean startAsFirst,
                       int daveWave, int... hugeWaves) {
        this.waveTime = waveTime;
        this.levelData = levelData;
        this.firstWave = firstWave;
        this.isFirstWave = startAsFirst;
        this.daveWave = daveWave;
        this.hugeWaves = hugeWaves;
        for (int i = 0; i < 6; i++) {
            spawnQueues.add(new ArrayList<>());
            zombieRow.add(new ArrayList<>());
            lastSpawnTime[i] = 0;
        }
    }

    public void startLevel() {
        wave = 0;
        AudioManager.getInstance().playSound(80, false, "readysetplant.mp3");
        if (getWorld() != null) getWorld().addObject(new ReadySetPlant(), 620, 295);
        currentState = WaveState.PREPARING_NEXT_WAVE;
        stateTimer = System.currentTimeMillis();
    }

    public void act() {
        if (playScene == null || playScene.isGameOver) return;
        if (wave == -1) return;

        // CỰC KỲ QUAN TRỌNG: Dọn dẹp Zombie chết khỏi bộ nhớ mỗi frame
        cleanZombieRows();
        handlePerRowSpawning();

        long now = System.currentTimeMillis();

        switch (currentState) {
            case BATTLE:
                if (choosingCard) return;
                if (allClear()) {
                    if (wave >= levelData.length) {
                        finishedSending = true;
                        if (!hasWonNotification()) { // Tránh add trùng
                             playScene.addObject(new WaveNotification(true), 360, 215);
                        }
                        return;
                    }
                    stateTimer = now;
                    currentState = WaveState.WAITING_FOR_REWARD;
                }
                break;

            case WAITING_FOR_REWARD:
                if (now - stateTimer >= 2000) { // Giảm thời gian chờ cho mượt
                    if (wave > 0 && wave % 2 == 0) { // Ví dụ: Cứ 2 wave tặng 1 sun lớn
                        playScene.addObject(new Sun(400, true), 555, 300);
                    }
                    stateTimer = now;
                    currentState = WaveState.WAITING_FOR_DAVE;
                }
                break;

            case WAITING_FOR_DAVE:
                if (now - stateTimer >= 1500) {
                    if (wave == daveWave) {
                        triggerDaveDialogue();
                        currentState = WaveState.DAVE_TALKING;
                    } else if (isHugeWave(wave)) {
                        triggerCardSelection();
                        currentState = WaveState.SELECTING_AUGMENT;
                    } else {
                        stateTimer = now;
                        currentState = WaveState.PREPARING_NEXT_WAVE;
                    }
                }
                break;

            case PREPARING_NEXT_WAVE:
                long delay = (isFirstWave ? firstWave : 3000L);
                if (now - stateTimer >= delay) {
                    if (isFirstWave) {
                        AudioManager.getInstance().playSound(80, false, "awooga.mp3");
                        isFirstWave = false;
                    }
                    launchWave();
                }
                break;
            
            default: break;
        }
    }

    // Hàm dọn dẹp zombie chết để cây không bắn vào khoảng không
    private void cleanZombieRows() {
        for (ArrayList<Zombie> row : zombieRow) {
            row.removeIf(z -> z.getWorld() == null || !z.isLiving());
        }
    }

    private void launchWave() {
        if (wave >= 0 && wave < levelData.length) {
            spawnZombies(levelData[wave]);
            playScene.addObject(new WaveNotification(wave == levelData.length - 1), 360, 215);
            wave++;
            currentState = WaveState.BATTLE;
        }
    }

    private boolean isHugeWave(int idx) {
        for (int h : hugeWaves) if (h == idx) return true;
        return false;
    }

    private boolean allClear() {
        // Kiểm tra zombie thực tế trên sân
        if (!playScene.getObjects(Zombie.class).isEmpty()) return false;
        // Kiểm tra zombie đang đợi trong hàng chờ spawn
        for (ArrayList<Zombie> q : spawnQueues) if (!q.isEmpty()) return false;
        return true;
    }

    private boolean hasWonNotification() {
        return !playScene.getObjects(WaveNotification.class).isEmpty();
    }

    private void handlePerRowSpawning() {
        long now = System.currentTimeMillis();
        for (int i = 0; i < 6; i++) {
            ArrayList<Zombie> q = spawnQueues.get(i);
            // Spawn giãn cách 1.5s để mượt hơn 2s
            if (!q.isEmpty() && now - lastSpawnTime[i] >= 1500) {
                Zombie z = q.remove(0);
                int x = 950 + Greenfoot.getRandomNumber(40);
                int y = playScene.GridManager.getYCoord(8, i) - 20;
                playScene.addObject(z, x, y);
                zombieRow.get(i).add(z);
                lastSpawnTime[i] = now;
            }
        }
    }

    private void spawnZombies(String[][] waveData) {
        if (waveData == null) return;
        for (int r = 0; r < waveData.length; r++) {
            if (waveData[r] == null) continue;
            int row = playScene.GridManager.clampRow(r);
            for (String id : waveData[r]) {
                Zombie z = ZombieFactory.createZombie(id);
                if (z != null) spawnQueues.get(row).add(z);
            }
            lastSpawnTime[row] = System.currentTimeMillis() - 1500;
        }
    }

    public void nextWave() {
        playScene.removeObjects(playScene.getObjects(Overlay.class));
        playScene.removeObjects(playScene.getObjects(AugmentCard.class));
        choosingCard = false;
        stateTimer = System.currentTimeMillis();
        currentState = WaveState.PREPARING_NEXT_WAVE;
    }

    private void triggerDaveDialogue() {
        String[] lines = {
            "Làm tốt lắm neighbor!\nSân cỏ của ngươi trông\nsạch bóng zombie rồi đấy!", 
            "Chiến thắng có mùi vị... tacos!\nTa sẽ tặng ngươi thêm Sun\nmỗi khi thắng wave!", 
            "Ồ! Hệ thống Augment Card!\nChọn lấy thẻ bổ trợ để giúp\ncây của ngươi mạnh hơn!"
        };
        String[] voices = {"crazydavecrazy.mp3", "crazydaveextralong1.mp3", "crazydaveextralong2.mp3"};
        playScene.addObject(new CrazyDave(this, lines, voices), 555, 349);
    }

    private void triggerCardSelection() {
        choosingCard = true;
        playScene.addObject(new Overlay(1111, 698), 555, 349);
        String[] pool = {"rerollcard", "TD", "HM"};
        for (int i = 0; i < 3; i++)
            playScene.addObject(new AugmentCard(this, pool[i], null), 255 + (i * 300), 349);
        fixOrder();
        AudioManager.getInstance().playSound(80, false, "hugewave.mp3");
    }

    public void fixOrder() {
        if (playScene != null) playScene.applyDefaultPaintOrder();
    }

    @Override
    protected void addedToWorld(World world) {
        playScene = (PlayScene) world;
        playScene.addObject(new Progress(this), 490, 25);
    }
    
    public void onDaveFinished() {
        stateTimer = System.currentTimeMillis();
        
        currentState = WaveState.WAITING_FOR_AUGMENT;
        
        AudioManager.getInstance().playSound(80, false, "points.mp3");
    }
    
    public int getWaveNumber() {
        return Math.max(0, this.wave);
    }
    
    public boolean hasWon() {
        return wave >= levelData.length && allClear();
    }
    
}