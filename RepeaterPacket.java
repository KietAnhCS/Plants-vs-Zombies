import greenfoot.*;

public class RepeaterPacket extends SeedPacket {

    public RepeaterPacket() {
        super(40000L, 225, "repeaterpacket");
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
        if (playScene != null && playScene.getPlantFactory() != null) {
            return playScene.getPlantFactory().createPlant(PlantType.REPEATER);
        }

        return PlantFactory.getInstance().createPlant(PlantType.REPEATER);
    }
    
    
}