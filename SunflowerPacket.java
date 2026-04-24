import greenfoot.*;  

public class SunflowerPacket extends SeedPacket
{
    public SunflowerPacket() {
        super(5000L, true,0, "sunflowerpacket");
    }
 
    public TransparentObject addImage() {
        TransparentObject temp = new TransparentSunflower(false);
        ((PlayScene)getWorld()).addObject(temp, Greenfoot.getMouseInfo().getX(), Greenfoot.getMouseInfo().getY());
        return temp;
    }
    public Plant getPlant() {
        return new Sunflower();
    }
}
