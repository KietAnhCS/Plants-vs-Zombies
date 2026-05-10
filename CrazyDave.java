import greenfoot.*;
import greenfoot.Color;
import greenfoot.Font;

public class CrazyDave extends Actor {
    private GifImage gif = new GifImage("Dave3.gif");
    private int talkStep = 0;
    private String[] sounds;
    private String[] scripts;
    private WaveManager waveManager;
    private GreenfootSound currentSound = null;

    public CrazyDave(String[] scripts, String[] sounds) {
        this(scripts, sounds, null);
    }

    public CrazyDave(WaveManager waveManager, String[] scripts, String[] sounds) {
        this(scripts, sounds, waveManager);
    }

    private CrazyDave(String[] scripts, String[] sounds, WaveManager waveManager) {
        this.scripts = scripts;
        this.sounds = sounds;
        this.waveManager = waveManager;
        setImage(new GreenfootImage(1111, 698));
    }

    @Override
    protected void addedToWorld(World world) {
        setLocation(world.getWidth() / 2, world.getHeight() / 2);
        playVoice();
    }

    public void act() {
        updateScreen();
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse != null && mouse.getButton() == 1 && Greenfoot.mouseClicked(this)) {
            handleInteraction();
        }
    }

    private void updateScreen() {
        GreenfootImage canvas = new GreenfootImage(1111, 698);
        GreenfootImage daveImg = new GreenfootImage(gif.getCurrentImage());
        if (daveImg.getWidth() != 600 || daveImg.getHeight() != 400) {
            daveImg.scale(600, 400);
        }
        canvas.drawImage(daveImg, 0, canvas.getHeight() - 400);
        if (talkStep < scripts.length) {
            drawDialogue(canvas, scripts[talkStep]);
        }
        setImage(canvas);
    }

    private void drawDialogue(GreenfootImage canvas, String text) {
        canvas.setFont(new Font("Impact", false, false, 22));
        canvas.setColor(Color.BLACK);
        int x = 220;
        int y = 360;
        for (String line : text.split("\n")) {
            canvas.drawString(line, x, y);
            y += 30;
        }
    }

    private void handleInteraction() {
        talkStep++;
        if (talkStep < scripts.length) {
            playVoice();
        } else {
            finish();
        }
    }

    private void stopCurrentSound() {
        if (currentSound != null) {
            currentSound.stop();
            currentSound = null;
        }
    }

    private void playVoice() {
        try {
            stopCurrentSound();
            currentSound = new GreenfootSound(sounds[talkStep]);
            currentSound.setVolume(80);
            currentSound.play();
        } catch (Exception e) {
            System.out.println("Sound error: " + sounds[talkStep]);
        }
    }

    private void finish() {
        World world = getWorld();
        if (world == null) return;
        stopCurrentSound();
        if (waveManager != null) {
            waveManager.onDaveFinished();
        } else if (world instanceof Arena) {
            ((Arena) world).startScrollSequence();
        }
        world.removeObject(this);
    }
}