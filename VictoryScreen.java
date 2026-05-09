import greenfoot.*;

public class VictoryScreen extends World {
    public VictoryScreen() {  
        super(1111, 698, 1); 
        
        // Tạo nền đen trước
        GreenfootImage bg = new GreenfootImage(1111, 698);
        bg.setColor(Color.BLACK);
        bg.fill();
        setBackground(bg);
        
        // Phát nhạc (Đảm bảo file tồn tại)
        try {
            AudioManager.getInstance().playSound(80, false, "victory.mp3");
        } catch (Exception e) {
            System.out.println("Không tìm thấy file nhạc thắng cuộc");
        }
        
        prepare();
    }

    private void prepare() {
        addObject(new VictoryGifActor(), getWidth() / 2, getHeight() / 2);
    }

    private class VictoryGifActor extends Actor {
        private GifImage gif = new GifImage("victorygif.gif");

        public void act() {
            // Đơn giản nhất là setImage liên tục để GIF chạy
            setImage(gif.getCurrentImage());
        }
    }
}