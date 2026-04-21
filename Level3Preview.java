import greenfoot.*;
import java.util.*;

public class Level3Preview extends World
{
    public GreenfootSound CYS = new GreenfootSound("intro3.mp3");
    public int count = 0;
    public int scrollSpeed = 4;
    public int location = 0;
    public boolean started = false;
    public Zombie n = null;

    public SeedPacket[] bank = {
        new SunflowerPacket(), 
        new SunflowerPacket(), 
        new PotatoPacket(), 
        new PotatoPacket(),
        new PeashooterPacket()
    };
    
    public SeedBank seedbank = new SeedBank(bank);   
    
    public Zombie[][] level1 = {
        
        { null, new BasicZombie(), null, null, null, null }, 
        { null, null, null, new BasicZombie(), null, null },
        { new BasicZombie(), new Conehead(), new BasicZombie(), new Conehead(), new BasicZombie(), null },
        { new BasicZombie(), null, null, null, new BasicZombie(), null },
        { null, new BasicZombie(), new BasicZombie(), null, null, null },
        { new BasicZombie(), new Conehead(), new BasicZombie(), new Conehead(), new BasicZombie(), null },
        { new BasicZombie(), null, new BasicZombie(), null, new BasicZombie(), null },
        { null, new BasicZombie(), null, new BasicZombie(), null, null },
        { new BasicZombie(), new BasicZombie(), new BasicZombie(), new BasicZombie(), new BasicZombie(), null },
        { null, null, null, null, null, null },
    
        
        { null, null, new Conehead(), null, null, null },
        { new BasicZombie(), null, null, null, new BasicZombie(), null },
        { null, new Conehead(), null, new Conehead(), null, null },
        { new BasicZombie(), new Conehead(), new BasicZombie(), new Conehead(), new BasicZombie(), null },
        { new BasicZombie(), new BasicZombie(), new Conehead(), new BasicZombie(), new BasicZombie(), null },
        { new Conehead(), new Conehead(), new Conehead(), new Conehead(), new Conehead(), null },
        { new BasicZombie(), new Conehead(), new BasicZombie(), new Conehead(), new BasicZombie(), null }, 
        { new BasicZombie(), new Conehead(), new BasicZombie(), new Conehead(), new BasicZombie(), null },
        { new Conehead(), new Conehead(), new Conehead(), new Conehead(), new Conehead(), null },
        { new BasicZombie(), new Conehead(), new BasicZombie(), new Conehead(), new BasicZombie(), null },
    
        
        { new BasicZombie(), new BasicZombie(), new BasicZombie(), new BasicZombie(), new BasicZombie(), null },
        { new Conehead(), new Conehead(), new Conehead(), new Conehead(), new Conehead(), null },
        { new BasicZombie(), new Conehead(), new BasicZombie(), new Conehead(), new BasicZombie(), null },
        { new BasicZombie(), null, new Conehead(), null, new BasicZombie(), null },
        { new Conehead(), new BasicZombie(), new Conehead(), new BasicZombie(), new Conehead(), null },
        { new BasicZombie(), new Conehead(), new BasicZombie(), new Conehead(), new BasicZombie(), null },
        { new BasicZombie(), new BasicZombie(), new BasicZombie(), new BasicZombie(), new BasicZombie(), null },
        { new Conehead(), new Conehead(), new Conehead(), new Conehead(), new Conehead(), null },
        { new BasicZombie(), new Conehead(), new BasicZombie(), new Conehead(), new BasicZombie(), null },
        { new BasicZombie(), new Conehead(), new BasicZombie(), new Conehead(), new BasicZombie(), null },
    
        
        { new BasicZombie(), new BasicZombie(), new BasicZombie(), new BasicZombie(), new BasicZombie(), null },
        { new Conehead(), new Conehead(), new Conehead(), new Conehead(), new Conehead(), null },
        { new BasicZombie(), new Conehead(), new BasicZombie(), new Conehead(), new BasicZombie(), null },        
        { new BasicZombie(), new Conehead(), new BasicZombie(), new Conehead(), new BasicZombie(), null },
        { new Conehead(), new Conehead(), new Conehead(), new Conehead(), new Conehead(), null },
        { new BasicZombie(), new Conehead(), new BasicZombie(), new Conehead(), new BasicZombie(), null },
        { new Conehead(), new Conehead(), new Conehead(), new Conehead(), new Conehead(), null }
    };
        
    public WaveManager level = new WaveManager(23500L, level1, 15000L, true);

    public Level3Preview()
    {    
        super(1111, 698, 1, false); 
        setBackground(new GreenfootImage("maptft.png"));
        addObject(new Basic(), 1176, 227);
        addObject(new Basic(), 1195, 322);
        addObject(new Basic(), 1129, 227);
        addObject(new Basic(), 1162, 325);
        addObject(new IdleCone(), 1183, 396);
        CYS.setVolume(70);
    }

    public void act() {
        if (!started) {
            started = true;
            CYS.playLoop();
        }
        count++;
        bgScrollAnimate();
    }

    public void bgScrollAnimate()
    {
        if (count > 100 && count < 160)
        {
            location -= scrollSpeed;
            scrollBGimage(location);
        }
        else if (count > 350 && count < 410)
        {
            location += scrollSpeed;
            scrollBGimage(location);
        }
        else if (count == 450) {
            List<IdleZombie> idleZombie = getObjects(IdleZombie.class);
            for (IdleZombie zombie : idleZombie) {
                removeObject(zombie);
            }
        }
        else if (count == 500)
        {
            Greenfoot.setWorld(new PlayScene(CYS, level, seedbank, new Level3Preview(), new WinRepeater(), false));
        }
    }
    
    public void scrollBGimage(int offset)
    {
        GreenfootImage bg = getBackground(); 
        GreenfootImage move = new GreenfootImage("maptft.png");
        bg.drawImage(move, offset, 0);  
        
        List<Actor> currentObjects = getObjects(null);
        for (Actor thisObject : currentObjects)
        {
            if (count > 100 && count < 160)
            {
                thisObject.setLocation(thisObject.getX() - scrollSpeed, thisObject.getY());
            }
            else if (count > 350 && count < 410)
            {
                thisObject.setLocation(thisObject.getX() + scrollSpeed, thisObject.getY());
            } 
        } 
    }
}