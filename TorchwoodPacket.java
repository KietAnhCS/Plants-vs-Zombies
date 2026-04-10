import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)


public class TorchwoodPacket extends SeedPacket
{
    /**
     * Act - do whatever the RepeaterPacket wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public TorchwoodPacket() {
        super(11000L, true, 225, "firewoodtest");
        
    }
    
    public TransparentObject addImage() {
        TransparentObject temp = new TransparentRepeater(false);
        ((MyWorld)getWorld()).addObject(temp, Greenfoot.getMouseInfo().getX(), Greenfoot.getMouseInfo().getY());
        return temp;
    }
    public Plant getPlant() {
        return new Torchwood();
    }
}
