import greenfoot.*;
import java.util.*;

public class WaveManager extends Actor
{
    public long currentFrame = System.nanoTime();
    public static final int xOffset = 1115;
    public static final int yOffset = 135;
    
    // Đổi ySpacing thành biến để lấy giá trị từ Board cho đồng bộ
    public int ySpacing = Board.ySpacing; 
    
    // SỬA: Không khai báo row1, row2... thủ công nữa
    public ArrayList<ArrayList<Zombie>> zombieRow = new ArrayList<ArrayList<Zombie>>();
    
    public long lastFrame = System.nanoTime();
    public Zombie[][] level;
    public long levelTime;
    public long waveTime;
    public long firstWave;
    public long deltaTime;

    public boolean won = false;
    public MyWorld MyWorld;
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

        // SỬA QUAN TRỌNG: Khởi tạo zombieRow dựa trên số hàng thực tế (6 hàng cho map nước)
        // Dùng vòng lặp để tạo túi chứa Zombie cho mỗi hàng
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
    
    // Sửa lại fixOrder để chạy hết 6 hàng
    public void fixOrder() {
        List<Zombie> zombies = MyWorld.getObjects(Zombie.class);
        for (int r = 0; r < 6; r++) { // Sửa từ 5 thành 6
            for (Zombie z : zombies) {
                if (z.getWorld() != null && z.getYPos() == r) {
                    int x = z.getX();
                    int y = z.getY();
                    MyWorld.removeObject(z);
                    MyWorld.addObject(z, x, y);
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
            MyWorld.addObject(new finishedSending(this, 15000L), 0, 0);
            wave = -1;
            return;
        }
        
        if (wave != -1) {
            long targetTime = first ? firstWave : waveTime;
            if (deltaTime >= targetTime || MyWorld.getObjects(Zombie.class).size() == 0) {
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
                MyWorld.addObject(new AHugeWave(wave == level.length - 1), 360, 215);
                return;       
            }
        }
        sendWave(level[wave]);
    }
    
    // Sửa logic gửi Zombie để chia hàng chuẩn (dùng modulo 6 thay vì 5)
    public void sendWave(Zombie[] waveData) {
        int rows = 6; // Số hàng của map nước
        for (int i = 0; i < waveData.length; i++) {
            if (waveData[i] != null) {
                int rowIndex = i % rows; // Chia đều zombie vào 6 hàng
                int wait = i / rows;
                int offset = xOffset + wait * 20;
                
                MyWorld.addObject(waveData[i], offset, rowIndex * ySpacing + yOffset);
                zombieRow.get(rowIndex).add(waveData[i]);
                finishedSending = false;
            }
        }
        MyWorld.addObject(new FixOrder(this, 1000L), 0, 0);
    }

    public void sendHugeWave(Zombie[] waveData) {
        int rows = 6;
        for (int i = 0; i < waveData.length; i++) {
            if (waveData[i] != null) {
                int rowIndex = i % rows;
                int wait = i / rows;
                int offset = xOffset + 50 + wait * 20;
                
                MyWorld.addObject(waveData[i], offset, rowIndex * ySpacing + yOffset);
                zombieRow.get(rowIndex).add(waveData[i]);
            }
        }
        MyWorld.addObject(new FixOrder(this, 1000L), 0, 0);
    }

    public boolean hasWon() {
        return (wave == -1 && MyWorld.getObjects(Zombie.class).size() == 0);
    }

    @Override
    protected void addedToWorld(World world) {
        MyWorld = (MyWorld)world;
        lastFrame = System.nanoTime();
        currentFrame = System.nanoTime();
        getWorld().addObject(new Progress(this), 490, 25);
    }
}