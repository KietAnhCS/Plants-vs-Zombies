import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class PeashooterPacket here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class PeashooterPacket extends SeedPacket
{
    /**
     * Act - do whatever the PeashooterPacket wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
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
