import greenfoot.*;

public class PeashooterPacket extends SeedPacket {

    public PeashooterPacket() {
        super(40000L, 125, "peashooterpacket");
    }

    @Override
    public TransparentObject addImage() {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse == null) return null;

        TransparentObject temp = new TransparentPeashooter(false);
        World world = getWorld();
        if (world != null) {
            world.addObject(temp, mouse.getX(), mouse.getY());
            return temp;
        }
        return null;
    }

    @Override
    public Plant getPlant() { 
        return PlantFactory.createPlant(PlantFactory.PlantType.PEASHOOTER);
    }
}