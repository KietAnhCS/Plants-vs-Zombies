import greenfoot.*;

public class PotatoPacket extends SeedPacket {

    public PotatoPacket() {
        super(40000L, 25, "potatopacket");
    }

    @Override
    public TransparentObject addImage() {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse == null) return null;
        TransparentObject temp = new TransparentPotato(false);
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
            return playScene.getPlantFactory().createPlant(PlantType.POTATO_MINE);
        }

        return PlantFactory.getInstance().createPlant(PlantType.POTATO_MINE);
    }
}