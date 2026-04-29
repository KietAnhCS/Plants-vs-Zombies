import greenfoot.*;

public class PeashooterPacket extends SeedPacket {
    public PeashooterPacket() {
        super(7500L, 100, "peashooterpacket"); 
    }

    @Override
    public Plant getPlant() { 
        return new Peashooter(); 
    }

    @Override
    public TransparentObject addImage() {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse == null) return null;
        TransparentObject temp = new TransparentPeashooter(false);
        if (getWorld() != null) {
            getWorld().addObject(temp, mouse.getX(), mouse.getY());
        }
        return temp;
    }
}