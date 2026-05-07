import greenfoot.*;
import java.util.*;

public class WaveManager extends Actor {
    // ─── State & Timing ───────────────────────────────────────────────
    private WaveState currentState = WaveState.BATTLE;
    private long stateTimer = 0;

    // ─── Config ───────────────────────────────────────────────────────
    public String[][][] levelData;
    public long waveTime;       // không dùng trực tiếp nhưng giữ lại
    public long firstWave;
    public int[] hugeWaves;
    public int daveWave;
    public boolean isFirstWave;

    // ─── Progress ─────────────────────────────────────────────────────
    public int wave = -1;       // index wave SẮP spawn (-1 = chưa start)
    public boolean choosingCard   = false;
    public boolean finishedSending = false;

    // ─── Spawn queues ─────────────────────────────────────────────────
    private final ArrayList<ArrayList<Zombie>> spawnQueues   = new ArrayList<>();
    private final long[]                       lastSpawnTime = new long[6];
    public  final ArrayList<ArrayList<Zombie>> zombieRow     = new ArrayList<>();

    // ─── Ref ──────────────────────────────────────────────────────────
    public PlayScene playScene;
    public static final int xOffset = 950;

    // ══════════════════════════════════════════════════════════════════
    // Constructor
    // new WaveManager(23500L, data, 15000L, true, 5,   0,1,2,5,10)
    //                 waveTime       firstWave isFirst daveWave hugeWaves...
    // ══════════════════════════════════════════════════════════════════
    public WaveManager(long waveTime, String[][][] levelData,
                       long firstWave, boolean startAsFirst,
                       int daveWave, int... hugeWaves) {
        this.waveTime   = waveTime;
        this.levelData  = levelData;
        this.firstWave  = firstWave;
        this.isFirstWave = startAsFirst;
        this.daveWave   = daveWave;
        this.hugeWaves  = hugeWaves;
        for (int i = 0; i < 6; i++) {
            spawnQueues.add(new ArrayList<>());
            zombieRow.add(new ArrayList<>());
            lastSpawnTime[i] = 0;
        }
    }

    // ══════════════════════════════════════════════════════════════════
    // startLevel — gọi từ Arena/ReadySetPlant khi bắt đầu
    // ══════════════════════════════════════════════════════════════════
    public void startLevel() {
        wave = 0;
        AudioManager.playSound(80, false, "readysetplant.mp3");
        if (getWorld() != null) getWorld().addObject(new ReadySetPlant(), 620, 295);
        // Dùng PREPARING_NEXT_WAVE với timer = firstWave để chờ rồi spawn wave 0
        currentState = WaveState.PREPARING_NEXT_WAVE;
        stateTimer   = System.currentTimeMillis();
    }

    // ══════════════════════════════════════════════════════════════════
    // act
    // ══════════════════════════════════════════════════════════════════
    public void act() {
        if (wave == -1 || playScene == null) return;

        // Luôn xử lý queue spawn từng hàng
        handlePerRowSpawning();

        long now = System.currentTimeMillis();

        switch (currentState) {

            // ── BATTLE ────────────────────────────────────────────────
            case BATTLE:
                if (choosingCard) return; // augment đang mở, đừng làm gì
                if (allClear()) {
                    if (wave >= levelData.length) {
                        // Hết tất cả wave
                        finishedSending = true;
                        return;
                    }
                    // Zombie sạch -> bắt đầu chuỗi nghỉ
                    stateTimer   = now;
                    currentState = WaveState.WAITING_FOR_REWARD;
                }
                break;

            // ── WAITING_FOR_REWARD (3s) -> spawn Sun ──────────────────
            // Luồng: Xong Wave -> Đợi 3s -> Nhận Sun
            case WAITING_FOR_REWARD:
                if (now - stateTimer >= 3000) {
                    if (wave > 0) { // Không thưởng sun trước wave đầu
                        playScene.addObject(new Sun(100, true), 555, 300);
                    }
                    stateTimer   = now;
                    currentState = WaveState.WAITING_FOR_DAVE;
                }
                break;

            // ── WAITING_FOR_DAVE (3s) -> quyết định Dave hay không ────
            // Luồng: Nhận Sun -> Đợi 3s -> Dave nói (nếu có) / thẳng Augment / thẳng Wave
            case WAITING_FOR_DAVE:
                if (now - stateTimer >= 3000) {
                    if (wave == daveWave) {
                        // Wave đặc biệt: Dave nói trước rồi mới Augment
                        triggerDaveDialogue();
                        currentState = WaveState.DAVE_TALKING;
                    } else if (isHugeWave(wave)) {
                        // Huge wave thường: bỏ qua Dave, thẳng tới Augment
                        triggerCardSelection();
                        currentState = WaveState.SELECTING_AUGMENT;
                    } else {
                        // Wave thường: không Dave, không Augment
                        stateTimer   = now;
                        currentState = WaveState.PREPARING_NEXT_WAVE;
                    }
                }
                break;

            // ── DAVE_TALKING -> chờ callback onDaveFinished() ─────────
            case DAVE_TALKING:
                // Không làm gì — CrazyDave sẽ gọi onDaveFinished() khi xong
                break;

            // ── WAITING_FOR_AUGMENT (5s) -> hiện bảng chọn card ───────
            // Luồng: Dave biến mất -> Đợi 5s -> Augment
            case WAITING_FOR_AUGMENT:
                if (now - stateTimer >= 5000) {
                    triggerCardSelection();
                    currentState = WaveState.SELECTING_AUGMENT;
                }
                break;

            // ── SELECTING_AUGMENT -> chờ nextWave() từ AugmentCard ────
            case SELECTING_AUGMENT:
                // Không làm gì — AugmentCard gọi nextWave() khi chọn xong
                break;

            // ── PREPARING_NEXT_WAVE (3s nếu thường / firstWave nếu đầu)
            // Luồng: Chọn xong Augment -> Đợi 3s -> Wave mới
            case PREPARING_NEXT_WAVE:
                long delay = (isFirstWave ? firstWave : 3000L);
                if (now - stateTimer >= delay) {
                    if (isFirstWave) {
                        AudioManager.playSound(80, false, "awooga.mp3");
                        isFirstWave = false;
                    }
                    launchWave();
                }
                break;
        }
    }

    // ══════════════════════════════════════════════════════════════════
    // Internal helpers
    // ══════════════════════════════════════════════════════════════════

    /** Spawn wave[wave], tăng wave, chuyển về BATTLE */
    private void launchWave() {
        spawnZombies(levelData[wave]);
        playScene.addObject(new WaveNotification(wave == levelData.length - 1), 360, 215);
        wave++;
        currentState = WaveState.BATTLE;
    }

    private boolean isHugeWave(int idx) {
        for (int h : hugeWaves) if (h == idx) return true;
        return false;
    }

    /** Tất cả zombie trên map và trong queue đều sạch */
    private boolean allClear() {
        if (!playScene.getObjects(Zombie.class).isEmpty()) return false;
        for (ArrayList<Zombie> q : spawnQueues) if (!q.isEmpty()) return false;
        return true;
    }

    private void triggerDaveDialogue() {
        String[] lines  = {
            "Good job, neighbor!\nYour lawn is looking\nremarkably zombie-free!", 
            "Victory smells like... tacos!\nI'm giving you 100 extra Sun\nevery time you win!", 
            "Ooh! The Augment Card system!\nGrab those rewards to give your plants\nsome serious [oomph]!",
            "Quick tip: click those Lawn Mower\nand watch 'em go!\nThey don't just sit there looking pretty,\nyou know!"};
        String[] voices = {"crazydavecrazy.mp3", "crazydaveextralong1.mp3", "crazydaveextralong2.mp3", "crazydaveextralong3.mp3"};
        playScene.addObject(new CrazyDave(this, lines, voices), 555, 349);
    }

    private void triggerCardSelection() {
        choosingCard = true;
        playScene.addObject(new Overlay(1111, 698), 555, 349);
        String[] pool = {"rerollcard", "TD", "HM"};
        for (int i = 0; i < 3; i++)
            playScene.addObject(new AugmentCard(this, pool[i], null), 255 + (i * 300), 349);
        fixOrder();
        AudioManager.playSound(70, false, "hugewave.mp3");
    }

    private void handlePerRowSpawning() {
        long now = System.currentTimeMillis();
        for (int i = 0; i < 6; i++) {
            ArrayList<Zombie> q = spawnQueues.get(i);
            if (!q.isEmpty() && now - lastSpawnTime[i] >= 2000) {
                Zombie z = q.remove(0);
                int x = 915 + Greenfoot.getRandomNumber(36);
                int y = playScene.GridManager.getYCoord(8, i) - 20;
                playScene.addObject(z, x, y);
                zombieRow.get(i).add(z);
                lastSpawnTime[i] = now;
            }
        }
    }

    private void spawnZombies(String[][] waveData) {
        if (waveData == null) return;
        GridManager board = playScene.GridManager;
        for (int r = 0; r < waveData.length; r++) {
            if (waveData[r] == null) continue;
            int row = board.clampRow(r);
            for (String id : waveData[r]) {
                Zombie z = ZombieFactory.createZombie(id);
                if (z != null) spawnQueues.get(row).add(z);
            }
            lastSpawnTime[row] = System.currentTimeMillis() - 2000;
        }
        playScene.addObject(new FixOrder(this, 10), 0, 0);
    }

    // ══════════════════════════════════════════════════════════════════
    // Public callbacks & API
    // ══════════════════════════════════════════════════════════════════

    /** Gọi bởi CrazyDave khi hết thoại */
    public void onDaveFinished() {
        stateTimer   = System.currentTimeMillis();
        currentState = WaveState.WAITING_FOR_AUGMENT;
    }

    /** Gọi bởi AugmentCard khi player chọn xong */
    public void nextWave() {
        playScene.removeObjects(playScene.getObjects(Overlay.class));
        playScene.removeObjects(playScene.getObjects(AugmentCard.class));
        choosingCard = false;
        stateTimer   = System.currentTimeMillis();
        currentState = WaveState.PREPARING_NEXT_WAVE;
    }

    public void fixOrder() {
        if (playScene != null) playScene.applyDefaultPaintOrder();
    }

    @Override
    protected void addedToWorld(World world) {
        playScene = (PlayScene) world;
        playScene.addObject(new Progress(this), 490, 25);
    }

    // ══════════════════════════════════════════════════════════════════
    // Getters
    // ══════════════════════════════════════════════════════════════════

    public boolean hasWon() {
        return wave >= levelData.length && allClear();
    }

    /** Trả về số wave hiển thị (1-based, tối thiểu 1) */
    public int getWaveNumber() {
        return Math.max(0, wave - 1);
    }

    public WaveState getCurrentState() {
        return currentState;
    }
}
/**BATTLE → (allClear) → WAITING_FOR_REWARD (3s)
       → spawn Sun  → WAITING_FOR_DAVE   (3s)
       → nếu daveWave  → DAVE_TALKING → [callback] → WAITING_FOR_AUGMENT (5s) → SELECTING_AUGMENT
       → nếu hugeWave  → SELECTING_AUGMENT
       → nếu thường    → PREPARING_NEXT_WAVE (3s) → launchWave → BATTLE**/