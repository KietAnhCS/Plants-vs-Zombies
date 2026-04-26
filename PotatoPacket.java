import greenfoot.*;  

public class PotatoPacket extends SeedPacket
{
    public PotatoPacket() {
        super(3000L, false, 25, "potatopacket");
    }
 
    public TransparentObject addImage() {
        TransparentObject temp = new TransparentPotato(false);
        ((PlayScene)getWorld()).addObject(temp, Greenfoot.getMouseInfo().getX(), Greenfoot.getMouseInfo().getY());
        return temp;
    }
    public Plant getPlant() {
        return new PotatoMine();
    }
}
