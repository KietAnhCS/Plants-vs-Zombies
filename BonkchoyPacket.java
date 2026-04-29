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
        World world = getWorld();
        if (world != null) {
            world.addObject(temp, mouse.getX(), mouse.getY());
            return temp;
        }
        return null;
    }

    @Override
    public Plant getPlant() { 
        return PlantFactory.createPlant(PlantFactory.PlantType.BONKCHOY);
    }
}