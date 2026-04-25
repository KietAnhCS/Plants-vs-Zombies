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
    private final int BASE_COST = 100;
    private int rollCount = 0;

    public RarityEntry[] weightedPool = {
        new RarityEntry(SunflowerPacket.class, 0),
        new RarityEntry(BonkchoyPacket.class, 1),   
        new RarityEntry(PeashooterPacket.class, 3),
        new RarityEntry(CactusPacket.class, 3),
        new RarityEntry(PotatoPacket.class, 7),
        new RarityEntry(RepeaterPacket.class, 0),
        new RarityEntry(GatlingPeaPacket.class, 0)
    };

    public RupButton() {
        updateAppearance();
    }

    public void act() {
        if (Greenfoot.mouseClicked(this)) {
            executeUpgrade();
        }
    }

    public RarityEntry[] getPoolForRoll() {
        if (rollCount < 2) {
            rollCount++;
            RarityEntry[] forcedPool = new RarityEntry[weightedPool.length];
            for (int i = 0; i < weightedPool.length; i++) {
                int weight = (weightedPool[i].packetClass == PotatoPacket.class) ? 100 : 0;
                forcedPool[i] = new RarityEntry(weightedPool[i].packetClass, weight);
            }
            return forcedPool;
        }
        return weightedPool;
    }

    private void executeUpgrade() {
        PlayScene world = (PlayScene) getWorld();
        if (world == null) return;

        int cost = 100;
        if (currentLevel < MAX_LEVEL && world.seedbank.getSun() >= cost) {
            world.seedbank.addSun(-cost);
            currentLevel++;
            applyChanges();
            updateAppearance();
            AudioPlayer.play(80, "achievement.mp3");
        }
    }

    private void applyChanges() {
        for (RarityEntry entry : weightedPool) {
            if (entry.packetClass == RepeaterPacket.class && currentLevel >= 2) {
                entry.weight = 1;
            }
            
            if (entry.packetClass == GatlingPeaPacket.class && currentLevel >= 4) {
                entry.weight = 1;
            }
            
            if (currentLevel >= 4) {
                if (entry.packetClass == CactusPacket.class) entry.weight = 0;
                if (entry.packetClass == SunflowerPacket.class) entry.weight = 1;
            
            }
            
            if (currentLevel == 5) {
                if (entry.packetClass == SunflowerPacket.class) entry.weight = 2;
                if (entry.packetClass == RepeaterPacket.class) entry.weight = 7;
                if (entry.packetClass == PeashooterPacket.class) entry.weight = 0;
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
        
        String txt = (currentLevel < MAX_LEVEL) ? "UP LV: " + currentLevel : "MAX LV";
        bg.drawString(txt, 10, 20);
        
        if (currentLevel < MAX_LEVEL) {
            bg.setColor(Color.YELLOW);
            bg.drawString("$" + (100), 10, 38);
        }
        setImage(bg);
    }
}