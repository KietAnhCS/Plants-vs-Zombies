import greenfoot.*;

public class GatlingPeaPacket extends SeedPacket {

    public GatlingPeaPacket() {
        super(3000L, 300, "gatlingpeapacket");
    }

    @Override
    public TransparentObject addImage() {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse == null) return null;
        TransparentObject temp = new TransparentGatlingPea(false);
        if (getWorld() != null) {
            getWorld().addObject(temp, mouse.getX(), mouse.getY());
        }
        return temp;
    }

    @Override
    public Plant getPlant() { 
        return new GatlingPea();
    }
}