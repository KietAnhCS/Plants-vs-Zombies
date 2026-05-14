import greenfoot.*;
import java.util.*;
import java.util.function.Supplier;

public class SecretSun extends FallingObject {
    private PlayScene scene;
    private GreenfootImage[] sprites;
    private boolean pickedUp = false;
    private boolean stationary = false;
    private double startY;
    private boolean hasFinishedFalling = false;

    public SecretSun() {
        // Mặc định rơi với các thông số vật lý từ FallingObject
        super(-3.5, 0.15, (Greenfoot.getRandomNumber(3) - 1), 1, 800L);
        sprites = importSprites("srsun", 2);
    }

    public SecretSun(boolean stationary) {
        super(0, 0, 0, 0, 0L);
        this.stationary = stationary;
        this.hasFinishedFalling = stationary;
        sprites = importSprites("srsun", 2);
    }

    @Override
    public void addedToWorld(World world) {
        if (world instanceof PlayScene) {
            scene = (PlayScene) world;
        }
        this.startY = getY();
    }

    public void update() {
        if (getWorld() == null) return;

        animate(sprites, 200, true);

        if (!pickedUp) {
            // Kiểm tra va chạm với ThuyThan (người chơi) để thu thập
            if (isTouching(ThuyThan.class)) {
                collect();
            } else {
                if (!stationary && !hasFinishedFalling) {
                    applyFallingPhysics();
                }
            }
        } else {
            flyToCounter();
        }

        checkRemoval();
    }

    public void collect() {
        if (pickedUp) return;
        pickedUp = true;
        setRotation(0);
        
        AudioManager.getInstance().playSound(80, false, "achievement.mp3");
        
        PlayScene currentScene = getPlayScene();
        if (currentScene != null) {
            spawnPlantsAtRowFive(currentScene);
        }
    }

    private void spawnPlantsAtRowFive(PlayScene world) {
        GridManager gm = world.GridManager;
        List<Integer> emptyCols = new ArrayList<>();
        int targetRow = 5; // Hàng 5 theo yêu cầu

        // 1. Tìm các cột còn trống ở hàng 5
        for (int c = 0; c < 9; c++) {
            if (gm.Board[targetRow][c] == null) {
                emptyCols.add(c);
            }
        }

        if (!emptyCols.isEmpty()) {
            Collections.shuffle(emptyCols);
            Random rand = new Random();

            Supplier<Plant> randomPlant = () -> {
                int r = Greenfoot.getRandomNumber(4); 
                switch (r) {
                    case 0: return new Peashooter();
                    case 1: return new Cactus();
                    case 2: return new Repeater();
                    case 3: return new BonkChoy();
                    default: return new Peashooter();
                }
            };

            int amountToSpawn = Math.min(2, emptyCols.size());
            for (int i = 0; i < amountToSpawn; i++) {
                int col = emptyCols.get(i);
                Plant p = randomPlant.get();
                
                gm.Board[targetRow][col] = p;
                world.addObject(p, gm.getXCoord(col, targetRow), gm.getYCoord(col, targetRow));
            }
        }
    }

    private void applyFallingPhysics() {
        if (elapsedTime < fallTime) {
            double nextX = getExactX() + hSpeed;
            double nextY = getExactY() + vSpeed;

            if (nextY >= startY + 20) {
                nextY = startY + 20;
                hasFinishedFalling = true;
            }

            setLocation(nextX, nextY);
            turn(rotate);
            vSpeed += acceleration;
        } else {
            hasFinishedFalling = true;
        }
    }

    private void flyToCounter() {
        PlayScene currentScene = getPlayScene();
        if (currentScene == null) return;
        List<SunDisplay> displays = currentScene.getObjects(SunDisplay.class);
        if (!displays.isEmpty()) {
            SunDisplay ds = displays.get(0);
            turnTowards(ds.getX(), ds.getY());
            move(15);
        }
    }

    private void checkRemoval() {
        if (getWorld() == null || !pickedUp) return;
        PlayScene currentScene = getPlayScene();
        if (currentScene != null) {
            List<SunDisplay> displays = currentScene.getObjects(SunDisplay.class);
            if (!displays.isEmpty()) {
                SunDisplay ds = displays.get(0);
                if (Math.hypot(getX() - ds.getX(), getY() - ds.getY()) < 20) {
                    getWorld().removeObject(this);
                }
            }
        }
    }

    private PlayScene getPlayScene() {
        if (scene != null) return scene;
        if (getWorld() instanceof PlayScene) {
            scene = (PlayScene) getWorld();
            return scene;
        }
        return null;
    }
}