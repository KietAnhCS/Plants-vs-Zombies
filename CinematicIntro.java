import greenfoot.*;  
import java.util.*;

public class CinematicIntro extends World
{
    public GreenfootSound CYS = new GreenfootSound("intro3.mp3");
    public int count = 0;
    public int scrollSpeed = 4;
    public int location = 0;
    public boolean started = false;
    public Zombie n = null;
    
    private GreenfootImage backgroundMap = new GreenfootImage("maptft.png");
    private GifImage daveGif = new GifImage("Dave3.gif");
    public GreenfootSound daveVoice = new GreenfootSound("awooga.mp3");
    private boolean daveTalking = true;

    
    public Zombie[][] level1 = 
    {

        
            {new BasicZombie(),null,null,null,null,null},
            {new Conehead(),new Conehead(),null,null,null,null},
           {new Conehead(),null,new Conehead(),null,null,null},
            {new Conehead(),new Conehead(),new Conehead(),new Conehead(),null,null},
           {new Conehead(),new Conehead(),null,new Conehead(),new Conehead(),null},
        
        {new Conehead(), new Conehead()}, 
        {null, new BasicZombie(), null, null},
        {n},
        {new BasicZombie(), null, null, null, null}, 
        {n},
        {null, new BasicZombie(), null, new BasicZombie()},
        {new BasicZombie()},
        {null, null, new Conehead(), null, null},
        {null, new BasicZombie(), null, null},
        {n},
        {new BasicZombie(), null, null, null, null}, 
        {n},
        {null, new BasicZombie(), null, new BasicZombie()},
        {new BasicZombie()},
        {null, null, new Conehead(), null, null},
        
    };
    
    public SeedPacket[] bank = {new SunflowerPacket(),new SunflowerPacket(),new SunflowerPacket(),new PeashooterPacket(), new PeashooterPacket()};
    public SeedBank seedbank = new SeedBank(bank);   
    public WaveManager level = new WaveManager(23500L, level1, 15000L, true, 1,5,10,15,20);

    public CinematicIntro()
    {    
        super(1111, 698, 1, false); 
        getBackground().drawImage(backgroundMap, 0, 0);
        
        addObject(new Basic(), 1176, 227);
        addObject(new Basic(), 1195, 322);
        addObject(new Basic(), 1129, 227);
        addObject(new Basic(), 1162, 325);
        addObject(new IdleCone(), 1183, 396);
         
    
        CYS.setVolume(70);
        daveVoice.setVolume(70);
    }

    public void act() {
        if (daveTalking) {
            handleDave();
        } else {
            if (count == 0) {
                getBackground().drawImage(backgroundMap, 0, 0);
            }

            if (!started) {
                started = true;
                CYS.playLoop();
            }
            
            count++;
            bgScrollAnimate();
        }
    }

    private void handleDave() {
        GreenfootImage bg = getBackground();
        bg.drawImage(backgroundMap, 0, 0);
        
        GreenfootImage currentDave = daveGif.getCurrentImage();
        
        if (currentDave.getWidth() != 600) {
            currentDave.scale(600, 400);
        }
        
        bg.drawImage(currentDave, 0, getHeight() - 400);
        
        if (!daveVoice.isPlaying()) daveVoice.playLoop();
        
        if (Greenfoot.mouseClicked(this)) {
            daveVoice.stop();
            daveTalking = false;
            bg.drawImage(backgroundMap, 0, 0);
        }
    }

    public void bgScrollAnimate()
    {
        if ((count > 100 && count < 160) || (count > 350 && count < 410)) {
            if (count > 100 && count < 160) location -= scrollSpeed;
            else location += scrollSpeed;
            scrollBGimage(location);
        }
        else if (count == 450) {
            removeObjects(getObjects(IdleZombie.class));
        }
        else if (count == 500) {
            CYS.stop(); 
            
            Greenfoot.setWorld(new PlayScene(CYS, level, seedbank, this,null, true));
        }
    }
    
    public void scrollBGimage(int offset)
    {
        GreenfootImage bg = getBackground(); 
        bg.drawImage(backgroundMap, offset, 0);  
        
        for (Actor a : getObjects(Actor.class)) {
            if (a.getWorld() != null) {
                if (count > 100 && count < 160) a.setLocation(a.getX() - scrollSpeed, a.getY());
                else a.setLocation(a.getX() + scrollSpeed, a.getY());
            }
        } 
    }
}