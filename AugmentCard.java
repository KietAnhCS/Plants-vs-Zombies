import greenfoot.*;

public class AugmentCard extends Actor {
    private WaveManager manager;
    private String type;

    public AugmentCard(WaveManager manager, String type) {
        this.manager = manager;
        this.type = type;
        setImage(type + ".png");
    }

    public void act() {
        if (Greenfoot.mouseClicked(this)) {
            applyAugmentEffect();
            manager.resumeAfterSelection();
        }
    }

    private void applyAugmentEffect() {
    PlayScene world = (PlayScene) getWorld();
    if (world == null || world.seedbank == null) return;

    if (type.equals("rerollcard")) {
        world.seedbank.addSun(150);
    } else if (type.equals("TD")) {
        world.seedbank.setTD(true); 
    } else if (type.equals("HM")) {
        
    }
}
}