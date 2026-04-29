import greenfoot.*;

public class GatlingPeaPacket extends SeedPacket {

    public GatlingPeaPacket() {
        super(3000L, 300, "gatlingpacket");
    }

    @Override
    public TransparentObject addImage() {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse == null) return null;

        TransparentObject temp = new TransparentGatlingPea(false);
        World world = getWorld();
        if (world != null) {
            world.addObject(temp, mouse.getX(), mouse.getY());
            return temp;
        }
        return null;
    }

    @Override
    public Plant getPlant() { 
        return PlantFactory.createPlant(PlantFactory.PlantType.GATLINGPEA);
    }
}