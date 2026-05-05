import greenfoot.*; 

public class PopCap extends World
{
    public int counter = 0;
    public boolean hasTransitioned = false; // Biến cờ kiểm soát
    public GreenfootSound menutheme = new GreenfootSound("menutheme.mp3");

public PopCap()
    {   
        super(1111, 705, 1, false); 
        setPaintOrder(EndTransition.class, Transition.class);
        GreenfootImage bg = new GreenfootImage(1111, 705);
       
        bg.setColor(Color.BLACK);
        bg.fill();
        GreenfootImage logo = new GreenfootImage("ea.png"); 

        int centerX = (1111 - logo.getWidth()) / 2;
        int centerY = (705 - logo.getHeight()) / 2;
        
        bg.drawImage(logo, centerX, centerY);
        
        setBackground(bg);
    }

    public void act() {
        // Chạy nhạc
        if (!menutheme.isPlaying()) {
            menutheme.setVolume(80);
            menutheme.playLoop();
        }

        counter++;

        // Kiểm tra counter VÀ đảm bảo chưa chuyển cảnh lần nào
        if (counter > 100 && !hasTransitioned) {
            addObject(new Transition(true, new MainMenu(menutheme), 6), getWidth()/2, getHeight()/2);
            hasTransitioned = true; // Chặn không cho addObject thêm lần nào nữa
        }
    }
}
