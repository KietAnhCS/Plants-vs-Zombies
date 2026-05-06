import greenfoot.*;

public class CactusPacket extends SeedPacket {

    public CactusPacket() {
        super(40000L, 125, "cactuspacket");
    }

    @Override
    public TransparentObject addImage() {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse == null) return null;

        TransparentObject temp = new TransparentCactus(false);
        World world = getWorld();
        if (world != null) {
            world.addObject(temp, mouse.getX(), mouse.getY());
            return temp;
        }
        return null;
    }

    @Override
    public Plant getPlant() { 
        return PlantFactory.createPlant(PlantType.CACTUS);
    }
}