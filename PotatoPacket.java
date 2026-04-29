import greenfoot.*;

public class PotatoPacket extends SeedPacket {

    public PotatoPacket() {
        super(3000L, 25, "potatopacket");
    }

    @Override
    public TransparentObject addImage() {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse == null) return null;

        TransparentObject temp = new TransparentPotato(false);
        if (getWorld() != null) {
            getWorld().addObject(temp, mouse.getX(), mouse.getY());
        }
        return temp;
    }

    @Override
    public Plant getPlant() { 
        return new PotatoMine();
    }
}