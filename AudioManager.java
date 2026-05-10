import greenfoot.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class AudioManager implements IAudioService {
    private static final AudioManager INSTANCE = new AudioManager();
    
    private static int bgmVolume = 50;
    private static int sfxVolume = 50;
    
    private static boolean isMuted = false;
    private static GreenfootSound currentBGM = null;
    private static List<GreenfootSound> activeSounds = new CopyOnWriteArrayList<>();

    private AudioManager() {}

    public static AudioManager getInstance() {
        return INSTANCE;
    }

    @Override
    public void playSound(int volume, boolean loop, String filePath) {
        playSound(volume, loop, new String[]{ filePath });
    }

    public static void playSound(int volume, boolean loop, String... files) {
        if (isMuted || files.length == 0) return;
        cleanUpSounds();
        int index = Greenfoot.getRandomNumber(files.length);
        String selectedFile = files[index];
        try {
            GreenfootSound s = new GreenfootSound(selectedFile);
            
            s.setVolume(sfxVolume); 
            
            if (loop) s.playLoop(); else s.play();
            activeSounds.add(s);
        } catch (Exception e) {
            System.err.println("Lỗi Audio: " + selectedFile);
        }
    }

    public static void playSound(String... files) {
        playSound(sfxVolume, false, files);
    }

    public static void playBGM(String file) {
        stopBGM();
        try {
            currentBGM = new GreenfootSound(file);
            currentBGM.setVolume(bgmVolume);
            if (!isMuted) currentBGM.playLoop();
        } catch (Exception e) {
            System.err.println("Error BGM: " + file);
        }
    }

    public static void stopBGM() {
        if (currentBGM != null) {
            if (currentBGM.isPlaying()) currentBGM.stop();
            currentBGM = null;
        }
    }

    public static void toggleMute() {
        isMuted = !isMuted;
        if (currentBGM != null) {
            if (isMuted) currentBGM.pause();
            else currentBGM.playLoop();
        }
        for (GreenfootSound s : activeSounds) {
            if (isMuted && s.isPlaying()) s.pause();
        }
        if (isMuted) cleanUpSounds();
    }

    public static void setBGMVolume(int volume) {
        bgmVolume = Math.max(0, Math.min(100, volume));
        if (currentBGM != null) currentBGM.setVolume(bgmVolume);
    }

    public static void setSFXVolume(int volume) {
        sfxVolume = Math.max(0, Math.min(100, volume));
        for (GreenfootSound s : activeSounds) {
            try {
                if (s.isPlaying()) s.setVolume(sfxVolume);
            } catch (Exception e) {
                activeSounds.remove(s);
            }
        }
    }

    public static int getBGMVolume() { return bgmVolume; }
    public static int getSFXVolume() { return sfxVolume; }

    private static void cleanUpSounds() {
        activeSounds.removeIf(s -> !s.isPlaying());
    }

    public static boolean isMuted() { return isMuted; }

    public static boolean isSoundPlaying(String file) {
        if (currentBGM != null && currentBGM.isPlaying()) return true;
        for (GreenfootSound s : activeSounds) {
            if (s.isPlaying()) return true;
        }
        return false;
    }
}