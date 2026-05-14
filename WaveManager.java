import greenfoot.*;
import java.util.*;

public class WaveManager extends Actor {

    private WaveState currentState = WaveState.BATTLE;
    private long stateTimer = 0;
    private long lastClearTime = 0;
    private int winDelayCounter = 0;

    private BattlePhase battlePhase = BattlePhase.PREP;
    private long phaseStartTime = 0;
    private static final long PREP_DURATION_MS = 10000L;
    private static final long COUNTDOWN_DURATION_MS = 3000L;

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
    public final ArrayList<ArrayList<Zombie>> zombieRow = new ArrayList<>();

    public PlayScene playScene;
    public static final int xOffset = 950;

    private PrepCountdown countdownUI = null;

    private final Map<String, Integer> lawnmowerActivatedWave = new HashMap<>();

    public WaveManager(long waveTime, String[][][] levelData,
                       long firstWave, boolean startAsFirst,
                       int daveWave, int... hugeWaves) {
        this.waveTime    = waveTime;
        this.levelData   = levelData;
        this.firstWave   = firstWave;
        this.isFirstWave = startAsFirst;
        this.daveWave    = daveWave;
        this.hugeWaves   = hugeWaves;
        for (int i = 0; i < 6; i++) {
            spawnQueues.add(new ArrayList<>());
            zombieRow.add(new ArrayList<>());
            lastSpawnTime[i] = 0;
        }
    }

    public BattlePhase getBattlePhase() { return battlePhase; }

    public boolean canDragPlant(Plant plant) {
        if (battlePhase == BattlePhase.PREP || battlePhase == BattlePhase.COUNTDOWN) return true;
        return plant.getYPos() == 5;
    }

    public boolean canMerge(Plant plant) {
        return canDragPlant(plant);
    }

    public void trackLawnmowerUsed(int spawnX, int spawnY) {
        String key = spawnX + "," + spawnY;
        if (!lawnmowerActivatedWave.containsKey(key)) {
            lawnmowerActivatedWave.put(key, wave);
        }
    }

    public void startLevel() {
        wave = 0;
        AudioManager.getInstance().playSound(80, false, "readysetplant.mp3");
        if (getWorld() != null) getWorld().addObject(new ReadySetPlant(), 620, 295);
        enterPrep();
    }

    @Override
    public void act() {
        if (playScene == null) playScene = (PlayScene) getWorld();
        if (playScene.isGameOver) return;
        if (wave == -1) return;

        cleanZombieRows();
        handlePerRowSpawning();

        long now = System.currentTimeMillis();

        switch (battlePhase) {
            case PREP:
                if (countdownUI != null) {
                    long elapsed = now - phaseStartTime;
                    int secsLeft = (int) Math.ceil((PREP_DURATION_MS - elapsed) / 1000.0);
                    countdownUI.setSeconds(Math.max(0, secsLeft));
                }
                if (now - phaseStartTime >= PREP_DURATION_MS) {
                    removeCountdownUI();
                    launchWave();
                    battlePhase = BattlePhase.BATTLE;
                }
                break;

            case BATTLE:
                handleBattleState(now);
                break;

            case COUNTDOWN:
                if (countdownUI != null) {
                    long elapsed = now - phaseStartTime;
                    int secsLeft = (int) Math.ceil((COUNTDOWN_DURATION_MS - elapsed) / 1000.0);
                    countdownUI.setSeconds(Math.max(0, secsLeft));
                }
                if (now - phaseStartTime >= COUNTDOWN_DURATION_MS) {
                    removeCountdownUI();
                    enterPrep();
                }
                break;
        }
    }

    private void handleBattleState(long now) {
        switch (currentState) {
            case BATTLE:
                if (choosingCard) return;
                if (allClear()) {
                    if (lastClearTime == 0) lastClearTime = now;
                    if (now - lastClearTime >= 5000 || wave >= levelData.length) {
                        if (wave >= levelData.length) {
                            if (!finishedSending) {
                                finishedSending = true;
                                playScene.addObject(new WaveNotification(true), 360, 215);
                            }
                            winDelayCounter++;
                            if (winDelayCounter > 150) Greenfoot.setWorld(new VictoryScreen());
                            return;
                        } else {
                            lastClearTime = 0;
                            stateTimer = now;
                            currentState = WaveState.WAITING_FOR_REWARD;
                        }
                    }
                } else {
                    lastClearTime = 0;
                }
                break;

            case WAITING_FOR_REWARD:
                if (now - stateTimer >= 3000) {
                    if (wave > 0) playScene.addObject(new Sun(300, true), 900, 300);
                    stateTimer = now;
                    currentState = WaveState.WAITING_FOR_DAVE;
                }
                break;

            case WAITING_FOR_DAVE:
                if (now - stateTimer >= 3000) {
                    if (wave == daveWave) {
                        triggerDaveDialogue();
                        currentState = WaveState.DAVE_TALKING;
                    } else if (isHugeWave(wave)) {
                        triggerCardSelection();
                        currentState = WaveState.SELECTING_AUGMENT;
                    } else {
                        enterCountdown();
                    }
                }
                break;

            case WAITING_FOR_AUGMENT:
                if (now - stateTimer >= 5000) {
                    triggerCardSelection();
                    currentState = WaveState.SELECTING_AUGMENT;
                }
                break;

            case DAVE_TALKING:
            case SELECTING_AUGMENT:
                break;

            case PREPARING_NEXT_WAVE:
                long delay = (isFirstWave ? firstWave : 3000L);
                if (now - stateTimer >= delay) {
                    if (isFirstWave) {
                        AudioManager.getInstance().playSound(80, false, "awooga.mp3");
                        isFirstWave = false;
                    }
                    launchWave();
                    battlePhase = BattlePhase.BATTLE;
                }
                break;
        }
    }

    private void enterPrep() {
        battlePhase = BattlePhase.PREP;
        phaseStartTime = System.currentTimeMillis();
        currentState = WaveState.PREPARING_NEXT_WAVE;
        spawnCountdownUI("PREP PHASE");
        if (isFirstWave) {
            AudioManager.getInstance().playSound(80, false, "awooga.mp3");
            isFirstWave = false;
        }
    }

    private void enterCountdown() {
        battlePhase = BattlePhase.COUNTDOWN;
        phaseStartTime = System.currentTimeMillis();
        spawnCountdownUI("NEXT WAVE IN");
    }

    private void spawnCountdownUI(String label) {
        removeCountdownUI();
        if (getWorld() != null) {
            int secs = (battlePhase == BattlePhase.COUNTDOWN)
                ? (int)(COUNTDOWN_DURATION_MS / 1000)
                : (int)(PREP_DURATION_MS / 1000);
            countdownUI = new PrepCountdown(label, secs);
            getWorld().addObject(countdownUI, 980, 60);
        }
    }

    private void removeCountdownUI() {
        if (countdownUI != null && countdownUI.getWorld() != null) {
            getWorld().removeObject(countdownUI);
        }
        countdownUI = null;
    }

    private void launchWave() {
        if (wave >= 0 && wave < levelData.length) {
            spawnZombies(levelData[wave]);
            playScene.addObject(new WaveNotification(wave == levelData.length - 1), 360, 215);
            wave++;
            checkLawnmowerRespawn();
            currentState = WaveState.BATTLE;
            lastClearTime = 0;
        }
    }

    private void checkLawnmowerRespawn() {
        if (playScene == null) return;
        List<String> toRespawn = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : lawnmowerActivatedWave.entrySet()) {
            if (wave - entry.getValue() >= 2) {
                toRespawn.add(entry.getKey());
            }
        }
        for (String key : toRespawn) {
            lawnmowerActivatedWave.remove(key);
            String[] parts = key.split(",");
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            playScene.addObject(new Lawnmower(), x, y);
        }
    }

    private boolean isHugeWave(int idx) {
        for (int h : hugeWaves) if (h == idx) return true;
        return false;
    }

    private boolean allClear() {
        if (!playScene.getObjects(Zombie.class).isEmpty()) return false;
        for (ArrayList<Zombie> q : spawnQueues) if (!q.isEmpty()) return false;
        return true;
    }

    private void cleanZombieRows() {
        for (ArrayList<Zombie> row : zombieRow) {
            row.removeIf(z -> z.getWorld() == null || !z.isLiving());
        }
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

    private void triggerDaveDialogue() {
        String[] lines = {
            "Good job, neighbor!\nYour lawn is looking\nremarkably zombie-free!",
            "Victory smells like... tacos!\nI'm giving you 300 extra Sun\nevery time you win!",
            "Ooh! The Augment Card system!\nGrab those rewards to give your plants\nsome serious [oomph]!",
            "Quick tip: click those Lawn Mower\nand watch 'em go!\nThey don't just sit there looking pretty,\nyou know!"
        };
        String[] voices = {
            "crazydavecrazy.mp3", "crazydaveextralong1.mp3",
            "crazydaveextralong2.mp3", "crazydaveextralong3.mp3"
        };
        playScene.addObject(new CrazyDave(this, lines, voices), 555, 349);
    }

    private void triggerCardSelection() {
        choosingCard = true;
        playScene.addObject(new Overlay(1111, 698), 555, 349);
        String[] pool = {"rerollcard", "TD", "HM"};
        for (int i = 0; i < 3; i++)
            playScene.addObject(new AugmentCard(this, pool[i], null), 255 + (i * 300), 349);
        fixOrder();
        AudioManager.getInstance().playSound(70, false, "hugewave.mp3");
    }

    public void onDaveFinished() {
        stateTimer = System.currentTimeMillis();
        currentState = WaveState.WAITING_FOR_AUGMENT;
    }

    public void nextWave() {
        playScene.removeObjects(playScene.getObjects(Overlay.class));
        playScene.removeObjects(playScene.getObjects(AugmentCard.class));
        choosingCard = false;
        enterCountdown();
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
        return wave >= levelData.length && allClear();
    }

    public int getWaveNumber() {
        return Math.max(0, wave - 1);
    }
}