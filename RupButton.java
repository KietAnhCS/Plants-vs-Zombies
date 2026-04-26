import greenfoot.*;
import java.util.*;

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

        if (currentLevel < MAX_LEVEL && world.seedbank.getSun() >= UPGRADE_COST) {
            world.seedbank.addSun(-UPGRADE_COST);
            currentLevel++;

            if (world.GridManager != null) {
                world.GridManager.playerLevel = currentLevel;
            }

            applyChanges();
            updateAppearance();
            AudioPlayer.play(80, "achievement.mp3");
        }
    }

    private void applyChanges() {
        for (RarityEntry entry : weightedPool) {
            if (entry.packetClass == RepeaterPacket.class && currentLevel >= 2) {
                entry.weight = 2;
            }
            
            if (currentLevel >= 4) {
                if (entry.packetClass == GatlingPeaPacket.class) entry.weight = 1;
                if (entry.packetClass == CactusPacket.class) entry.weight = 0;
            }
            
            if (currentLevel == 5) {
                if (entry.packetClass == RepeaterPacket.class) entry.weight = 7;
                if (entry.packetClass == GatlingPeaPacket.class) entry.weight = 3;
                if (entry.packetClass == PeashooterPacket.class) entry.weight = 0;
                if (entry.packetClass == PotatoPacket.class) entry.weight = 2;
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
        String txt = (currentLevel < MAX_LEVEL) ? "UP LV: " + currentLevel : "MAX LEVEL";
        bg.drawString(txt, 15, 22);
        
        if (currentLevel < MAX_LEVEL) {
            bg.setColor(Color.YELLOW);
            bg.setFont(new Font("Verdana", true, false, 14));
            bg.drawString("COST: $" + UPGRADE_COST, 15, 42);
        } else {
            bg.setColor(Color.GRAY);
            bg.setFont(new Font("Verdana", true, false, 14));
            bg.drawString("COMPLETED", 15, 42);
        }
        
        setImage(bg);
    }

    public int getCurrentLevel() {
        return currentLevel;
    }
}