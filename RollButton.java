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
            for (RupButton.RarityEntry entry : currentPool) {
                if (entry != null) totalWeight += entry.weight;
            }

            if (totalWeight <= 0) return;

            sm.spend(ROLL_COST);
            AudioManager.playSound(80, false, "achievement.mp3");

            SeedPacket[] newBank = new SeedPacket[3];
            for (int i = 0; i < 3; i++) {
                newBank[i] = generateValidPacket(currentPool, totalWeight);
            }
            world.seedbank.updateBank(newBank);
        }
    }

    private SeedPacket generateValidPacket(RupButton.RarityEntry[] pool, int totalWeight) {
        while (true) {
            int randomNumber = Greenfoot.getRandomNumber(totalWeight);
            int cursor = 0;
            for (RupButton.RarityEntry entry : pool) {
                if (entry == null) continue;
                cursor += entry.weight;
                if (randomNumber < cursor) {
                    try {
                        SeedPacket packet = (SeedPacket) entry.packetClass.getDeclaredConstructor().newInstance();
                        if (packet != null) return packet;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
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