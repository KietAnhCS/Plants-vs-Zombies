import greenfoot.*;

public class CactusPacket extends SeedPacket
{
    public CactusPacket() {
        super(40000L, false, 125, "cactuspacket");
        
    }
    
    public TransparentObject addImage() {
        TransparentObject temp = new TransparentCactus(false);
        ((PlayScene)getWorld()).addObject(temp, Greenfoot.getMouseInfo().getX(), Greenfoot.getMouseInfo().getY());
        return temp;
    }
    public Plant getPlant() {
        return new Cactus();
    }
}
