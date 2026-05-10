import greenfoot.*;

public class WaveNotification extends Actor
{
    public int counter = 0;
    public boolean finalWave = false;
    
    public WaveNotification(boolean finalWave) {
        this.finalWave = finalWave;
    }
    public void act()
    {
        counter++;
        
        if (!finalWave) {
            handleNormalWave();
        } else {
            handleFinalWave();
        }
    }
    
    private void handleNormalWave() {
        if (counter == 300) {
            AudioManager.getInstance().playSound(70,false, "siren.mp3");
        }
        if (counter >300) {
            getWorld().removeObject(this);
        }
    }
    
    public void handleFinalWave() {
        if (counter == 300) {
            AudioManager.getInstance().playSound(70,false, "siren.mp3");
            
            AudioManager.getInstance().playSound(70,false, "finalwave.mp3");
        }
        
        if (counter > 450) {
            getWorld().removeObject(this);
        }
    }
}
