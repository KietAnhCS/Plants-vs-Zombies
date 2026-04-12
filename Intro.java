import greenfoot.*;  
import java.util.*;

public class Intro extends World
{
    public GreenfootSound CYS = new GreenfootSound("intro3.mp3");
    public int count = 0;
    public int scrollSpeed = 4;
    public int location = 0;
    public boolean started = false;
    
    // Tạo 1 lần dùng mãi mãi, không tạo trong act()
    private GreenfootImage backgroundMap = new GreenfootImage("lawn367.png");
    private GifImage daveGif = new GifImage("Dave3.gif");
    public GreenfootSound daveVoice = new GreenfootSound("mixi.mp3");
    private boolean daveTalking = true;

    public Zombie[][] level1 = {
        {new BasicZombie(), new BasicZombie()},
        {new BasicZombie(), new BasicZombie()},
        {new BasicZombie(), new BasicZombie()},
        {
            new BossZombie(), new BossZombie(), new BossZombie(),
            new BossZombie(), new BossZombie(), new BossZombie(),
            new BossZombie(), new BossZombie(), new BossZombie(),
            new BossZombie(), new BossZombie(), new BossZombie(),
            new BossZombie(), new BossZombie(), new BossZombie()
        },
        {new BasicZombie(), new BasicZombie()},
        {new Conehead(), new Conehead()}
    };
    
    public SeedPacket[] bank = {new SunflowerPacket(), new PeashooterPacket(), new WalnutPacket(), new TorchwoodPacket()};
    public SeedBank seedbank = new SeedBank(bank);   
    public WaveManager level = new WaveManager(23500L, level1, 15000L, true, 8);

    public Intro()
    {    
        super(1111, 602, 1, false); 
        getBackground().drawImage(backgroundMap, 0, 0);
        
        addObject(new Basic(), 1176, 227);
        addObject(new Basic(), 1195, 322);
        addObject(new Basic(), 1129, 227);
        addObject(new Basic(), 1162, 325);
        addObject(new IdleCone(), 1183, 396);
    
        CYS.setVolume(70);
        daveVoice.setVolume(80);
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
            CYS.stop(); // Tắt nhạc intro trước khi vào game
            Greenfoot.setWorld(new MyWorld(CYS, level, seedbank, this, new WinPotato()));
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