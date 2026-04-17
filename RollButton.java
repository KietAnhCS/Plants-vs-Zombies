import greenfoot.*;

public class RollButton extends Actor
{
    public RollButton() {
    
        GreenfootImage img = new GreenfootImage("roll button", 24, Color.WHITE, new Color(0, 0, 0, 150));
        setImage(img);
    }

    public void act() {
        // Kiểm tra nếu người dùng click vào nút
        if (Greenfoot.mouseClicked(this)) {
            World world = getWorld();
            if (world instanceof PlayScene) {
                PlayScene ps = (PlayScene) world;
                ps.rollPackets(); 
            }
        }
    }
}