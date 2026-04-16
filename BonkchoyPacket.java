import greenfoot.*;  


public class BonkchoyPacket extends SeedPacket
{
    public BonkchoyPacket() {
        super(1L, true, 100, "peashooterpacket");
        
    }
    
    public TransparentObject addImage() {
        TransparentObject temp = new TransparentPeashooter(false);
        ((PlayScene)getWorld()).addObject(temp, Greenfoot.getMouseInfo().getX(), Greenfoot.getMouseInfo().getY());
        return temp;
    }
    public Plant getPlant() {
        return new BonkChoy();
    }
}
