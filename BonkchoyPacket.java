import greenfoot.*;

public class BonkchoyPacket extends SeedPacket {

    public BonkchoyPacket() {
        super(1000L, 175, "bonkchoypacket");
    }

    @Override
    public TransparentObject addImage() {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse == null) return null;
        TransparentObject temp = new TransparentBonkchoy(false);
        if (getWorld() != null) {
            getWorld().addObject(temp, mouse.getX(), mouse.getY());
        }
        return temp;
    }

    @Override
    public Plant getPlant() { 
        return new BonkChoy();
    }
}