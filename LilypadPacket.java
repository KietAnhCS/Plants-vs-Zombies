import greenfoot.*; 

/**
 * Write a description of class CactusPacket here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class LilypadPacket extends SeedPacket
{
    public LilypadPacket() {
        super(4000L, false, 125, "Lilypadpacket");
        
    }
    
    public TransparentObject addImage() {
        TransparentObject temp = new TransparentLilypad(false);
        ((PlayScene)getWorld()).addObject(temp, Greenfoot.getMouseInfo().getX(), Greenfoot.getMouseInfo().getY());
        return temp;
    }
    public Plant getPlant() {
        return new Lilypad();
    }
}
