import greenfoot.*;

public class RupButton extends Actor {
    public static class RarityEntry {
        public Class packetClass;
        public int weight;
        public RarityEntry(Class packetClass, int weight) {
            this.packetClass = packetClass;
            this.weight = weight;
        }
    }

    private int currentLevel = 1;
    private final int MAX_LEVEL = 5;
    private final int UPGRADE_COST = 50;
    private int rollCount = 0;

    public RarityEntry[] weightedPool = {
        new RarityEntry(BonkchoyPacket.class, 1),
        new RarityEntry(PeashooterPacket.class, 3),
        new RarityEntry(CactusPacket.class, 3),
        new RarityEntry(PotatoPacket.class, 7),
        new RarityEntry(RepeaterPacket.class, 0)
    };

    public RupButton() {
        updateAppearance();
    }

    public void act() {
        if (Greenfoot.mouseClicked(this)) executeUpgrade();
    }

    public RarityEntry[] getPoolForRoll() {
        if (rollCount < 2) {
            rollCount++;
            return new RarityEntry[]{ new RarityEntry(PotatoPacket.class, 100) };
        }
        return weightedPool;
    }

    private void executeUpgrade() {
        PlayScene world = (PlayScene) getWorld();
        if (world == null) return;
        SunManager sm = world.getSunManager();

        if (currentLevel < MAX_LEVEL && sm.hasEnough(UPGRADE_COST)) {
            sm.spend(UPGRADE_COST);
            currentLevel++;
            if (world.GridManager != null) world.GridManager.playerLevel = currentLevel;
            
            applyChanges();
            updateAppearance();
            AudioManager.playSound(80, false, "achievement.mp3");
        }
    }

    private void applyChanges() {
        for (RarityEntry entry : weightedPool) {
            Class c = entry.packetClass;
            if (currentLevel == 2 && c == RepeaterPacket.class) entry.weight = 2;
            if (currentLevel >= 4) {
                if (c == CactusPacket.class) entry.weight = 0;
            }
            if (currentLevel == 5) {
                if (c == RepeaterPacket.class) entry.weight = 7;
                if (c == PeashooterPacket.class) entry.weight = 0;
                if (c == PotatoPacket.class) entry.weight = 2;
            }
        }
    }

    private void updateAppearance() {
        GreenfootImage bg = new GreenfootImage(120, 50);
        bg.setColor(new Color(40, 40, 40, 220));
        bg.fill();
        bg.setColor(Color.WHITE);
        bg.drawRect(0, 0, 119, 49);
        bg.setFont(new Font("Verdana", true, false, 16));
        bg.drawString(currentLevel < MAX_LEVEL ? "UP LV: " + currentLevel : "MAX LEVEL", 15, 22);
        bg.setColor(currentLevel < MAX_LEVEL ? Color.YELLOW : Color.GRAY);
        bg.drawString(currentLevel < MAX_LEVEL ? "COST: $" + UPGRADE_COST : "COMPLETED", 15, 42);
        setImage(bg);
    }
}