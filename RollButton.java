import greenfoot.*;

public class RollButton extends Actor {
    private final int ROLL_COST = 25;

    public RollButton() {
        updateAppearance();
    }

    public void act() {
        PlayScene world = (PlayScene) getWorld();
        if (world == null || world.level == null || world.level.choosingCard) return;

        if (Greenfoot.mouseClicked(this)) {
            executeRoll(world);
        }
    }

    private void executeRoll(PlayScene world) {
        if (world.seedbank == null || world.rupbutton == null) return;
        SunManager sm = world.getSunManager();

        if (sm.hasEnough(ROLL_COST)) {
            RupButton.RarityEntry[] currentPool = world.rupbutton.getPoolForRoll();
            int totalWeight = 0;
            for (RupButton.RarityEntry entry : currentPool) totalWeight += entry.weight;

            if (totalWeight <= 0) return;

            sm.spend(ROLL_COST);
            AudioManager.playSound(80, false, "achievement.mp3");

            SeedPacket[] newBank = new SeedPacket[3];
            for (int i = 0; i < 3; i++) {
                int randomNumber = Greenfoot.getRandomNumber(totalWeight);
                int cursor = 0;
                for (RupButton.RarityEntry entry : currentPool) {
                    cursor += entry.weight;
                    if (randomNumber < cursor) {
                        try {
                            newBank[i] = (SeedPacket) entry.packetClass.getDeclaredConstructor().newInstance();
                        } catch (Exception e) { e.printStackTrace(); }
                        break;
                    }
                }
            }
            world.seedbank.updateBank(newBank);
        }
    }

    private void updateAppearance() {
        GreenfootImage bg = new GreenfootImage(100, 45);
        bg.setColor(new Color(0, 0, 0, 180));
        bg.fill();
        bg.setColor(Color.WHITE);
        bg.drawRect(0, 0, 99, 44);
        bg.setFont(new Font("Arial", true, false, 14));
        bg.drawString("ROLL SEEDS", 6, 20);
        bg.setColor(Color.YELLOW);
        bg.drawString("$" + ROLL_COST, 10, 38);
        setImage(bg);
    }
}