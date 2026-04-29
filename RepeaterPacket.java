import greenfoot.*;

public class RepeaterPacket extends SeedPacket {

    public RepeaterPacket() {
        super(1000L, 225, "repeaterpacket");
    }

    @Override
    public TransparentObject addImage() {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse == null) return null;

        TransparentObject temp = new TransparentRepeater(false);
        if (getWorld() != null) {
            getWorld().addObject(temp, mouse.getX(), mouse.getY());
        }
        return temp;
    }

    @Override
    public Plant getPlant() { 
        return new Repeater();
    }
}