import greenfoot.*;  

public class TwinSunflowerPacket extends SeedPacket
{
    public TwinSunflowerPacket() {
        super(16000L, true, 175, "twinsunflowerpacket");
    }
 
    public TransparentObject addImage() {
        TransparentObject temp = new TransparentTwinSunflower(false);
        ((PlayScene)getWorld()).addObject(temp, Greenfoot.getMouseInfo().getX(), Greenfoot.getMouseInfo().getY());
        return temp;
    }
    public Plant getPlant() {
        return new TwinSunflower();
    }
}
