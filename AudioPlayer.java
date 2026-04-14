import greenfoot.GreenfootSound;

public class AudioPlayer  
{
   
    public AudioPlayer()
    {
        
    }

    public static void play(String... audio)
    {
        int index = (int)(Math.random()*audio.length);
        GreenfootSound temp = new GreenfootSound(audio[index]);
        temp.play();
    }
    public static void play(int volume,String... audio)
    {
        int index = (int)(Math.random()*audio.length);
        GreenfootSound temp = new GreenfootSound(audio[index]);
        temp.setVolume(volume);
        temp.play();
    }
    public static void play(boolean loop, String... audio)
    {
        int index = (int)(Math.random()*audio.length);
        GreenfootSound temp = new GreenfootSound(audio[index]);
        if (loop) {
            temp.playLoop();
        } else {
            temp.play();
        }
    }
    public static void stop(GreenfootSound... audio) {
        for (int i = 0; i < audio.length; i++) {
            audio[i].stop();
        }
    }
}
