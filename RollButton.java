import greenfoot.*;
import java.util.List;

public class RollButton extends Actor
{
    public RollButton() {
        GreenfootImage img = new GreenfootImage(" ROLL SEEDS (25) ", 24, Color.WHITE, new Color(0, 0, 0, 150));
        setImage(img);
    }

    public void act() {
        PlayScene world = (PlayScene)getWorld();
        if (world == null) return;
        
        if (world.level.choosingCard) return;

        if (Greenfoot.mouseClicked(this)) {
            executeRoll(world);
        }
    }

    private void executeRoll(PlayScene world) {
        RupButton rup = world.rupbutton;
        if (world.seedbank.getSun() >= 25) {
            
            RupButton.RarityEntry[] currentPool = rup.getPoolForRoll();
            
            int totalWeight = 0;
            for (RupButton.RarityEntry entry : currentPool) {
                if (entry.weight > 0) totalWeight += entry.weight;
            }
            
            if (totalWeight <= 0) return; 
    
            world.seedbank.addSun(-25); 
            AudioPlayer.play(80, "achievement.mp3");
    
            SeedPacket[] newBank = new SeedPacket[3]; 
            for (int i = 0; i < 3; i++) {
                int randomNumber = Greenfoot.getRandomNumber(totalWeight);
                int cursor = 0;
                for (RupButton.RarityEntry entry : currentPool) {
                    if (entry.weight <= 0) continue;
                    cursor += entry.weight;
                    if (randomNumber < cursor) {
                        try {
                            newBank[i] = (SeedPacket) entry.packetClass.getDeclaredConstructor().newInstance();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
            }
            world.seedbank.updateBank(newBank);
        }
    }
}