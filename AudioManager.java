import greenfoot.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class AudioManager {
    private static int masterVolume = 80;
    private static boolean isMuted = false;
    private static GreenfootSound currentBGM = null;
    
    private static List<GreenfootSound> activeSounds = new CopyOnWriteArrayList<>();

    public static void playSound(int volume, boolean loop, String... files) {
        if (isMuted || files.length == 0) return;

        cleanUpSounds();
        int index = Greenfoot.getRandomNumber(files.length);
        String selectedFile = files[index];

        try {
            GreenfootSound s = new GreenfootSound(selectedFile);
            s.setVolume(volume >= 0 ? volume : masterVolume);
            
            if (loop) s.playLoop(); else s.play();
            activeSounds.add(s);
        } catch (Exception e) {
            System.err.println("Lỗi Audio: " + selectedFile);
        }
    }

    public static void playSound(String... files) {
        playSound(masterVolume, false, files);
    }
    public static void playBGM(String file) {
        stopBGM();
        try {
            currentBGM = new GreenfootSound(file);
            currentBGM.setVolume(masterVolume);
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
            if (isMuted) {
                if (s.isPlaying()) s.pause();
            } else {
            }
        }
        
        if (isMuted) cleanUpSounds(); 
    }

    public static void setMasterVolume(int volume) {
        masterVolume = Math.max(0, Math.min(100, volume));
        
        if (currentBGM != null) {
            currentBGM.setVolume(masterVolume);
        }
        
        for (GreenfootSound s : activeSounds) {
            try {
                if (s.isPlaying()) s.setVolume(masterVolume);
            } catch (Exception e) {
                activeSounds.remove(s);
            }
        }
    }

    private static void cleanUpSounds() {
        activeSounds.removeIf(s -> !s.isPlaying());
    }

    public static boolean isMuted() { return isMuted; }
    public static int getVolume() { return masterVolume; }
    
    public static boolean isSoundPlaying(String file) {
        if (currentBGM != null && currentBGM.isPlaying()) return true;
        for (GreenfootSound s : activeSounds) {
            if (s.isPlaying()) return true;
        }
        return false;
    }
}