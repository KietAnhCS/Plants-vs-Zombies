import greenfoot.*;

public class PotatoPacket extends SeedPacket {

    public PotatoPacket() {
        super(1000L, 175, "potatopacket");
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
        if (playScene != null && playScene.getPlantFactory() != null) {
            return playScene.getPlantFactory().createPlant(PlantType.POTATOMINE);
        }

        return PlantFactory.getInstance().createPlant(PlantType.POTATOMINE);
    }
}