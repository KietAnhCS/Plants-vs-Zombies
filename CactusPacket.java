import greenfoot.*;

public class CactusPacket extends SeedPacket {

    public CactusPacket() {
        super(40000L, 125, "cactuspacket");
    }

    @Override
    public TransparentObject addImage() {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse == null) return null;

        TransparentObject temp = new TransparentCactus(false);
        if (getWorld() != null) {
            getWorld().addObject(temp, mouse.getX(), mouse.getY());
        }
        return temp;
    }

    @Override
    public Plant getPlant() { 
        return new Cactus();
    }
}