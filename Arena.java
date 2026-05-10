import greenfoot.*;
import java.util.*;

public class Arena extends World {
    private static final int SCROLL_SPEED = 4;
    private GameState currentState = GameState.DAVE_TALKING;
    
    private int count = 0;
    private int location = 0;
    
    private GreenfootImage backgroundMap = new GreenfootImage("maptft1.png");
    private WaveManager level;
    private SeedBank seedbank;

    private boolean isMenuOpen = false;
    public static boolean isPaused = false; 

public Arena() {    
        super(1111, 698, 1, false); 
        
        setPaintOrder(SettingsResumeButton.class, SliderKnob.class, SliderBar.class, SettingsMenuPanel.class, 
                      CrazyDave.class, SeedPacket.class, IdleZombie.class);
                      
        initComponents();
        refreshBackground();
        setupInitialZombies();
        
        isPaused = false; 
    }

    private void initComponents() {
        PlantFactory factory = PlantFactory.getInstance();
        
        SeedPacket[] bank = {
            factory.createSeedPacket("PEASHOOTER"), 
            factory.createSeedPacket("PEASHOOTER"), 
            factory.createSeedPacket("BONKCHOY"), 
            null, null
        };
        
        this.seedbank = new SeedBank(bank);
        this.level = new WaveManager(23500L, LevelConfig.LEVEL_1_DATA, 15000L, true, 2, 2, 5, 10, 12, 14, 15);

        String[] introScripts = {
            "Greetings, neighbor!\nWelcome to my... uh... GAME!",
            "They call me Crazy Dave.\nBut you can just call me... Crazy Dave!\nI own this joint.",
            "Expect the unexpected!\nI've cooked up some brand-new,\nbrain-protecting madness just for you!",
            "Let's get crackin'!\nWabby Wabbo!"
        };
        String[] introSounds = {
            "crazydavecrazy.mp3", 
            "crazydaveextralong1.mp3", 
            "crazydaveextralong2.mp3", 
            "crazydaveextralong3.mp3"
        };
        addObject(new CrazyDave(introScripts, introSounds), 555, 349);
    }

    public void act() {
        String key = Greenfoot.getKey();
        if ("escape".equals(key) && !isMenuOpen) {
            openSettingsMenu();
        }

        if (isPaused) {
            return; 
        }

        switch (currentState) {
            case DAVE_TALKING:
                break;
            case SCROLLING:
                count++;
                runScrollSequence();
                break;
        }
    }

    public void openSettingsMenu() {
        if (!isMenuOpen) {
            isPaused = true;
            
            int centerX = getWidth() / 2;
            int centerY = getHeight() / 2;
            
            SettingsMenuPanel panel = new SettingsMenuPanel();
            addObject(panel, centerX, centerY);
            
            int panelHeight = panel.getImage().getHeight();
            int panelTopY = centerY - panelHeight / 2; 

            int bgmDrawY = 120; 
            int sfxDrawY = 220; 
            
            int bgmAbsY = panelTopY + bgmDrawY;
            int sfxAbsY = panelTopY + sfxDrawY;

            addObject(new SliderBar("BGM"), centerX + 40, bgmAbsY);
            addObject(new SliderBar("SFX"), centerX + 40, sfxAbsY);
            addObject(new SettingsResumeButton(), centerX, centerY + 190); 
            
            isMenuOpen = true;
        }
    }

    public void closeSettingsMenu() {
        if (isMenuOpen) {
            removeObjects(getObjects(SettingsMenuPanel.class));
            removeObjects(getObjects(SliderBar.class));
            removeObjects(getObjects(SliderKnob.class)); 
            removeObjects(getObjects(SettingsResumeButton.class));
            
            isPaused = false; 
            isMenuOpen = false;
        }
    }

    public void startScrollSequence() {
        currentState = GameState.SCROLLING;
        AudioManager.getInstance().playBGM("awooga.mp3");
    }

    private void runScrollSequence() {
        if (count > 100 && count < 160) {
            applyScroll(-SCROLL_SPEED);
        } else if (count > 350 && count < 410) {
            applyScroll(SCROLL_SPEED);
        } else if (count == 450) {
            removeObjects(getObjects(IdleZombie.class));
        } else if (count == 500) {
            transitionToPlayScene();
        }
    }

    private void applyScroll(int speed) {
        location += speed;
        refreshBackground();
        for (Actor a : getObjects(Actor.class)) {
            if (!(a instanceof CrazyDave) && !(a instanceof SettingsMenuPanel) && !(a instanceof SliderBar) && !(a instanceof SliderKnob) && !(a instanceof SettingsResumeButton)) {
                a.setLocation(a.getX() + speed, a.getY());
            }
        }
    }

    private void transitionToPlayScene() {
        AudioManager.getInstance().stopBGM();
        Greenfoot.setWorld(new PlayScene(null, level, seedbank, this, null, true));
    }

    private void refreshBackground() {
        getBackground().drawImage(backgroundMap, location, 0);
    }

    private void setupInitialZombies() {
        addObject(new Basic(), 1171, 212);
        addObject(new IdleBrickhead(), 1258, 22);
        addObject(new IdleCone(), 1150, 252);
        addObject(new IdleBucket(), 1199, 265);
    }
}