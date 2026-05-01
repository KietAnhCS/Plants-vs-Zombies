import greenfoot.*;  
import java.util.*;

public class Arena extends World {
    private static final int SCROLL_SPEED = 4;
    private static final int SCROLL_START_OUT = 100;
    private static final int SCROLL_END_OUT = 160;
    private static final int SCROLL_START_IN = 350;
    private static final int SCROLL_END_IN = 410;
    private static final int TRANSITION_TICK = 500;

    public int count = 0;
    public int location = 0;
    private boolean daveTalking = true;
    
    private GreenfootImage backgroundMap = new GreenfootImage("maptft1.png");
    private GifImage daveGif = new GifImage("Dave3.gif");

    public String[][][] levelData = {
        {{"NORMAL"}, {"BRICKHEAD"}, {"BUCKETHEAD"}, {"CONEHEAD"}, null}, 
        {null, {"NORMAL"}, null, {"NORMAL"}, null}, 
        {{"NORMAL"}, {"NORMAL"}, {"NORMAL"}, null, null}, 
        {null, {"CONEHEAD"}, null, {"CONEHEAD"}, {"BUCKETHEAD"}, null},
        {null, {"CONEHEAD"}, null, {"CONEHEAD"}, {"BUCKETHEAD"}, null},
        {null, {"NORMAL"}, null, {"NORMAL"}, {"BUCKETHEAD"}, null},
        {{"BUCKETHEAD"}, {"CONEHEAD"}, null, {"NORMAL"}, {"BUCKETHEAD"}, null},
        {{"NORMAL"}, {"CONEHEAD"}, null, {"CONEHEAD"}, {"NORMAL"}, null},
        {{"BUCKETHEAD"}, {"CONEHEAD"}, null, {"CONEHEAD"}, {"BUCKETHEAD"}, null}, 
        {{"CONEHEAD"}, {"CONEHEAD"}, {"BUCKETHEAD"}, {"CONEHEAD"}, {"CONEHEAD"}, null},
        {{"BRICKHEAD"}, {"BUCKETHEAD"}, {"BUCKETHEAD"}, {"BUCKETHEAD"}, {"BRICKHEAD"}, null},
        {{"BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD"}, {"BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD"}, {"BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD"}, {"BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD"}, {"BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD"}, null},
        {{"BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD"}, {"BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD"}, {"BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD"}, {"BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD"}, {"BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD"}, null},
        {{"BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD"}, {"BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD"}, {"BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD"}, {"BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD"}, {"BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD"}, null},
        {{"BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD"}, {"BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD"}, {"BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD"}, {"BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD"}, {"BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD"}, null},
        {{"BRICKHEAD", "BRICKHEAD", "BRICKHEAD"}, {"BRICKHEAD", "BRICKHEAD"}, {"BRICKHEAD", "BRICKHEAD"}, {"BRICKHEAD", "BRICKHEAD"}, {"BRICKHEAD"}, null},
        {{"BRICKHEAD", "BRICKHEAD", "BRICKHEAD"}, {"BRICKHEAD", "BRICKHEAD", "BRICKHEAD"}, {"BRICKHEAD", "BRICKHEAD"}, {"BRICKHEAD", "BRICKHEAD"}, {"BRICKHEAD"}, null},
        {{"BRICKHEAD", "BRICKHEAD", "BRICKHEAD"}, {"BRICKHEAD", "BRICKHEAD", "BRICKHEAD"}, {"BRICKHEAD", "BRICKHEAD", "BRICKHEAD"}, {"BRICKHEAD", "BRICKHEAD"}, {"BRICKHEAD", "BRICKHEAD"}, null},
        {{"BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD"}, {"BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD"}, {"BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD"}, {"BRICKHEAD", "BRICKHEAD"}, {"BRICKHEAD", "BRICKHEAD"}, null},
        {{"BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD"}, {"BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD"}, {"BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD"}, {"BRICKHEAD", "BRICKHEAD", "BRICKHEAD"}, {"BRICKHEAD", "BRICKHEAD", "BRICKHEAD"}, null},
        {{"BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD"}, {"BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD"}, {"BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD"}, {"BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD"}, {"BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD"}, null},
        {{"BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD"}, {"BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD"}, {"BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD"}, {"BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD"}, {"BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD"}, null},
        {{"BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD"}, {"BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD"}, {"BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD"}, {"BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD"}, {"BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD"}, null},
        {{"BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD"}, {"BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD"}, {"BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD"}, {"BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD"}, {"BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD", "BRICKHEAD"}, null}
    };

    public SeedPacket[] bank = {
        PlantFactory.createSeedPacket("PEASHOOTER"), 
        PlantFactory.createSeedPacket("PEASHOOTER"), 
        PlantFactory.createSeedPacket("BONKCHOY"), 
        null, null
    };
    
    public SeedBank seedbank = new SeedBank(bank);   
    public WaveManager level = new WaveManager(23500L, levelData, 15000L, true, 2, 5, 10);

    public Arena() {    
        super(1111, 698, 1, false); 
        refreshBackground();
        setupInitialZombies();
        AudioManager.playBGM("awooga.mp3"); 
    }

    private void setupInitialZombies() {
        addObject(new Basic(), 1176, 227);
        addObject(new Basic(), 1195, 322);
        addObject(new Basic(), 1129, 227);
        addObject(new Basic(), 1162, 325);
        addObject(new IdleCone(), 1183, 396);
    }

    public void act() {
        if (daveTalking) {
            handleDave();
        } else {
            count++;
            bgScrollAnimate();
        }
    }

    private void handleDave() {
        refreshBackground(); 
        GreenfootImage currentDave = daveGif.getCurrentImage();
        if (currentDave.getWidth() != 600) {
            currentDave.scale(600, 400);
        }
        getBackground().drawImage(currentDave, 0, getHeight() - 400);

        if (Greenfoot.mouseClicked(this)) {
            AudioManager.stopBGM(); 
            daveTalking = false;
            refreshBackground(); 
        }
    }

    public void bgScrollAnimate() {
        if (count > SCROLL_START_OUT && count < SCROLL_END_OUT) {
            location -= SCROLL_SPEED;
            scrollWorld(-SCROLL_SPEED);
        } 
        else if (count > SCROLL_START_IN && count < SCROLL_END_IN) {
            location += SCROLL_SPEED;
            scrollWorld(SCROLL_SPEED);
        } 
        else if (count == 450) {
            removeObjects(getObjects(IdleZombie.class));
        } 
        else if (count == TRANSITION_TICK) {
            AudioManager.stopBGM();
            Greenfoot.setWorld(new PlayScene(null, level, seedbank, this, null, true));
        }
    }
    
    private void scrollWorld(int speed) {
        getBackground().drawImage(backgroundMap, location, 0); 
        for (Actor a : getObjects(Actor.class)) {
            a.setLocation(a.getX() + speed, a.getY());
        } 
    }

    private void refreshBackground() {
        getBackground().drawImage(backgroundMap, location, 0);
    }
}