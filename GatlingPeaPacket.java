import greenfoot.*; 

public class GatlingPeaPacket extends SeedPacket
{
    public GatlingPeaPacket() {
        super(3000L, false, 300, "gatlingpeapacket");
    }
 
    public TransparentObject addImage() {
        TransparentObject temp = new TransparentGatlingPea(false);
        ((PlayScene)getWorld()).addObject(temp, Greenfoot.getMouseInfo().getX(), Greenfoot.getMouseInfo().getY());
        return temp;
    }
    public Plant getPlant() {
        return new GatlingPea();
    }
}
