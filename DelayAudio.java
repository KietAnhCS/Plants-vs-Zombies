import greenfoot.*;


public class DelayAudio extends Actor
{
    private GreenfootSound music;
    private GreenfootSound stop;
    private int volume;
    private boolean loop;
    
    private long startTime;
    private long delayTime;
    private boolean started = false;
    
    DelayAudio(GreenfootSound music, int volume, boolean loop, long delayTime) {
        this(music, null, volume, loop , delayTime);
    }
    
    DelayAudio(GreenfootSound music, GreenfootSound stop, int volume, boolean loop, long delayTime) {
        this.music = music;
        this.stop = stop;
        this.volume = volume;
        this.loop = loop;
        this.delayTime = delayTime;
    }
    
    protected void addedToWorld(World world) {
        startTime = System.currentTimeMillis();
        started = true;
    }
    
    public void act() {
        if (!started) return;
        
        
        long elapsed = System.currentTimeMillis() - startTime;
        
        if(elapsed >= delayTime) {
            if (stop !=null && stop.isPlaying()) {
                stop.stop();
            }
            
            if (music !=null) {
                music.setVolume(volume);
                if (loop) music.playLoop();
                else music.play();
            }
            
            getWorld().removeObject(this);
        }
    }
    
}
