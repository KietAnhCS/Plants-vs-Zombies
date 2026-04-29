import greenfoot.*;

public class RepeaterPacket extends SeedPacket {

    public RepeaterPacket() {
        super(40000L, 125, "repeaterpacket");
    }

    @Override
    public TransparentObject addImage() {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse == null) return null;

        TransparentObject temp = new TransparentRepeater(false);
        World world = getWorld();
        if (world != null) {
            world.addObject(temp, mouse.getX(), mouse.getY());
            return temp;
        }
        return null;
    }

    @Override
    public Plant getPlant() { 
        return PlantFactory.createPlant(PlantFactory.PlantType.REPEATER);
    }
}