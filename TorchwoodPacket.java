import greenfoot.*;  


public class TorchwoodPacket extends SeedPacket
{
    public TorchwoodPacket() {
        super(8L, true, 75, "firewoodtest");
        
    }
    
    public TransparentObject addImage() {
        TransparentObject temp = new TransparentTorchwood(false);
        ((PlayScene)getWorld()).addObject(temp, Greenfoot.getMouseInfo().getX(), Greenfoot.getMouseInfo().getY());
        return temp;
    }
    public Plant getPlant() {
        return new Torchwood();
    }
}
