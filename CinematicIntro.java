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
    
    private GreenfootImage backgroundMap = new GreenfootImage("maptft2.png");
    private GifImage daveGif = new GifImage("Dave3.gif");
    public GreenfootSound daveVoice = new GreenfootSound("awooga.mp3");
    private boolean daveTalking = true;

    
    public Zombie[][][] level1 = {
        // --- GIAI ĐOẠN 1: KHỞI ĐẦU (Wave 1 - 9) ---
        {{new BasicZombie()}, null, null, null, null, null}, // 1
        {null, {new BasicZombie()}, null, {new BasicZombie()}, null, null}, // 2
        {{new BasicZombie()}, {new BasicZombie()}, {new BasicZombie()}, null, null, null}, // 3
        {{new Conehead()}, null, {new BasicZombie()}, null, {new BasicZombie()}, null}, // 4
        {{new BasicZombie()}, {new Conehead()}, {new BasicZombie()}, {new Conehead()}, null, null}, // 5
        {{new Conehead(), new Conehead()}, null, null, {new BasicZombie()}, null, null}, // 6
        {{new BasicZombie(), new BasicZombie()}, {new BasicZombie()}, {new Conehead()}, {new BasicZombie()}, null, null}, // 7
        {{new Conehead()}, {new Conehead()}, {new Conehead()}, null, null, null}, // 8
        {{new Buckethead()}, {new BasicZombie()}, {new BasicZombie()}, {new BasicZombie()}, {new BasicZombie()}, null}, // 9
    
        // --- GIAI ĐOẠN 2: TĂNG TỐC (Wave 10 - 18) ---
        {{new Buckethead(), new BasicZombie()}, {new Conehead()}, {new Conehead()}, null, null, null}, // 10
        {{new Buckethead(), new BasicZombie()}, {new Buckethead(), new BasicZombie()}, null, null, null, null}, // 11
        {{new Buckethead(), new Conehead()}, {new Buckethead(), new Conehead()}, {new Buckethead()}, null, null, null}, // 12
        {{new Buckethead(), new Buckethead(), new Conehead()}, {new Conehead()}, {new BasicZombie()}, {new BasicZombie()}, null, null}, // 13
        {{new Brickhead()}, {new Buckethead()}, {new Buckethead()}, null, {new Buckethead()}, null}, // 14
        {{new Buckethead(), new Buckethead(), new Buckethead()}, {new Buckethead()}, {new Conehead()}, {new Conehead()}, null, null}, // 15
        {{new Brickhead(), new BasicZombie()}, {new Brickhead()}, {new Buckethead()}, {new BasicZombie()}, null, null}, // 16
        {{new Brickhead(), new Conehead(), new BasicZombie()}, {new Brickhead()}, null, null, null, null}, // 17
        {{new Buckethead(), new Buckethead(), new Buckethead(), new Conehead()}, {new Buckethead()}, {new Buckethead()}, null, null, null}, // 18
    
        // --- GIAI ĐOẠN 3: CĂNG THẲNG (Wave 19 - 24) ---
        {{new Brickhead(), new Brickhead()}, {new Brickhead()}, {new Buckethead(), new Buckethead()}, null, null, null}, // 19
        {{new Brickhead(), new Brickhead(), new Conehead()}, null, null, {new Brickhead(), new Buckethead()}, null, null}, // 20
        {{new Brickhead(), new Brickhead()}, {new Brickhead(), new Brickhead()}, {new Brickhead()}, {new Brickhead()}, null, null}, // 21
        {{new Brickhead(), new Buckethead(), new Buckethead()}, {new Brickhead(), new Brickhead()}, {new Buckethead()}, {new Buckethead()}, null, null}, // 22
        {{new Brickhead(), new Brickhead(), new Buckethead()}, {new Buckethead(), new Buckethead(), new Conehead()}, {new Conehead()}, null, null, null}, // 23
        {{new Brickhead(), new Brickhead(), new Brickhead(), new Buckethead()}, null, {new Brickhead(), new Buckethead()}, null, null, null}, // 24
    
        // --- GIAI ĐOẠN 4: ĐỘ KHÓ CỰC ĐẠI (Wave 25 - 36) ---
        // Từ đây mỗi hàng có tối đa 6 Zombie
        {{new Brickhead(), new Brickhead(), new Buckethead(), new Buckethead(), new Conehead(), new BasicZombie()}, {new Brickhead(), new Brickhead()}, null, null, null, null}, // 25
        {{new Brickhead(), new Brickhead(), new Brickhead(), new Buckethead(), new Buckethead(), new Buckethead()}, {new Brickhead()}, {new Brickhead()}, null, null, null}, // 26
        {{new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead()}, {new Brickhead()}, {new Brickhead()}, {new Brickhead()}, null, null}, // 27
        {{new Brickhead(), new Brickhead(), new Brickhead(), new Buckethead(), new Buckethead(), new Conehead()}, {new Brickhead(), new Brickhead(), new Brickhead(), new Buckethead(), new Buckethead(), new Conehead()}, null, null, null, null}, // 28
        {{new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead()}, {new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead()}, {new Brickhead()}, null, null, null}, // 29
        {{new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead()}, {new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead()}, {new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead()}, null, null, null}, // 30
        {{new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead()}, {new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead()}, {new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead()}, {new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead()}, null, null}, // 31
        {{new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead()}, {new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead()}, {new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead()}, {new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead()}, {new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead()}, null}, // 32
        {{new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead()}, {new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead()}, {new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead()}, {new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead()}, {new BasicZombie()}, null}, // 33
        {{new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead()}, {new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead()}, {new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead()}, {new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead()}, {new Buckethead(), new Buckethead()}, null}, // 34
        {{new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead()}, {new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead()}, {new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead()}, {new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead()}, {new Brickhead(), new Brickhead(), new Brickhead()}, null}, // 35
        // WAVE 36: TẤT CẢ CÁC HÀNG ĐỀU CÓ 6 BRICKHEAD (MỨC ĐỘ KHÓ NHẤT)
        {{new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead()}, 
         {new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead()}, 
         {new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead()}, 
         {new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead()}, 
         {new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead(), new Brickhead()}, null} // 36
    };
        
    public SeedPacket[] bank = {new TwinSunflowerPacket(),new SunflowerPacket(),new BonkchoyPacket(),null,null};
    public SeedBank seedbank = new SeedBank(bank);   
    public WaveManager level = new WaveManager(23500L, level1, 15000L, true,2,5,10,15);

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