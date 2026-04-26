import greenfoot.*;  

public class PeashooterPacket extends SeedPacket
{
    public PeashooterPacket() {
        super(1L, true, 100, "peashooterpacket");
        
    }
    
    public TransparentObject addImage() {
        TransparentObject temp = new TransparentPeashooter(false);
        ((PlayScene)getWorld()).addObject(temp, Greenfoot.getMouseInfo().getX(), Greenfoot.getMouseInfo().getY());
        return temp;
    }
    public Plant getPlant() {
        return new Peashooter();
    }
}
