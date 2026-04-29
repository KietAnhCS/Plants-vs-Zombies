import greenfoot.*;

public class DelayAudio extends Actor
{
    private String musicFile;
    private long startTime;
    private long delayTime;
    private boolean started = false;
    private int volume;
    private boolean loop;

    public DelayAudio(String musicFile, int volume, boolean loop, long delayTime) {
        this.musicFile = musicFile;
        this.volume = volume;
        this.loop = loop;
        this.delayTime = delayTime;
        setImage(new GreenfootImage(1, 1)); 
    }

    protected void addedToWorld(World world) {
        startTime = System.currentTimeMillis();
        started = true;
    }

    public void act() {
        if (!started) return;

        long elapsed = System.currentTimeMillis() - startTime;

        if (elapsed >= delayTime) {
            AudioManager.playSound(volume, loop, musicFile);
            
            getWorld().removeObject(this);
        }
    }
}