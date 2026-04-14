import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * IntroLevel3: Màn giới thiệu cho Level 3
 */
public class IntroLevel3 extends World
{
    public GreenfootSound CYS = new GreenfootSound("intro3.mp3");
    public int count = 0;
    public int scrollSpeed = 4;
    public int location = 0;
    public boolean started = false;
    public Zombie n = null;
    
    // Bank hạt giống cho màn 3
    public SeedPacket[] bank = {
        new SunflowerPacket(), 
        new PeashooterPacket(), 
        new WalnutPacket(), 
        new PotatoPacket(),
        new LilypadPacket()
    };
    
    public SeedBank seedbank = new SeedBank(bank);   
    
    // Định nghĩa Zombie cho Level 3 (Bạn có thể tùy chỉnh lại mảng này cho khó hơn)
    public Zombie[][] level1 = {
                {null, new BasicZombie(), null, null},
                {n},
                {new BasicZombie(), null, null, null, null}, 
                {n},
                {null, new BasicZombie(), null, new BasicZombie()},
                {new BasicZombie()},
                {null, null, new Conehead(), null, null},
                {n},
                {new BasicZombie(), new Conehead(), new BasicZombie(), new BasicZombie(), new BasicZombie(), n,new BasicZombie()}, 
                {n},
                {new Conehead(), n, null, new BasicZombie(), null, null, new BasicZombie()},
                {new BasicZombie(),n,n, new BasicZombie(), null, new BasicZombie(), new BasicZombie()},
                {null, null, null, new Buckethead(), null},
                {n,new BasicZombie(),n,n,new Conehead(), n, n, new BasicZombie()},
                {null, new BasicZombie(), null, null, new Conehead(),n,n,new BasicZombie()},
                {new BasicZombie(), new BasicZombie(), new BasicZombie(),  null, new Conehead()}, 
                {null, null, new BasicZombie(), null, null},
                {n},
                {new Conehead(), new Conehead(), new Conehead(), new BasicZombie(), new BasicZombie(), new Buckethead(), null, new BasicZombie(), new Conehead(), new Buckethead()}
    };
    
    // WaveManager khởi tạo (Tham số cuối là số hàng, 18 là giá trị từ code gốc của bạn)
    public WaveManager level = new WaveManager(23500L, level1, 15000L, true, 8, 18);

    public IntroLevel3()
    {    
        super(1111, 602, 1, false); 
        // Thiết lập hình nền ban đầu (lawn367 cho map cỏ)
        setBackground(new GreenfootImage("lawn367.png"));
        
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
        if ( count > 100 && count < 160)
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
            List<IdleZombie> idleZombie = getObjects(IdleZombie.class );
            for ( IdleZombie zombie : idleZombie ) {
                removeObject(zombie);
            }
        }
        else if ( count == 500 )
        {
            /**
             * CẬP NHẬT: 
             * 1. restartWorld: truyền new IntroLevel3() để khi thua sẽ chơi lại đúng màn này.
             * 2. winPlant: thay bằng cây bạn muốn tặng khi thắng (VD: new WinCherryBomb()).
             * 3. isWater: để false nếu là map cỏ, true nếu là map nước.
             */
            Greenfoot.setWorld(new MyWorld(CYS, level, seedbank, new IntroLevel3(), new WinRepeater(), false));
        }
    }
    
    public void scrollBGimage(int offset)
    {
         GreenfootImage bg = getBackground(); 
         GreenfootImage move = new GreenfootImage("lawn367.png"); // Map cỏ
         bg.drawImage(move, offset, 0);  
        
        List<Actor> currentObjects = getObjects(null);
        for ( Actor thisObject : currentObjects )
        {
            if ( count > 100 && count < 160)
            {
                thisObject.setLocation(thisObject.getX() - scrollSpeed , thisObject.getY() );
            }
            else if ( count > 350 && count < 410)
            {
                thisObject.setLocation(thisObject.getX() + scrollSpeed , thisObject.getY() );
            } 
        } 
    }
}