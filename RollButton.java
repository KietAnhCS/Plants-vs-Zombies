import greenfoot.*;

public class RollButton extends Actor
{
    public RollButton() {
    
        GreenfootImage img = new GreenfootImage("roll button", 24, Color.WHITE, new Color(0, 0, 0, 150));
        setImage(img);
    }

    public void act() {
        PlayScene world = (PlayScene)getWorld();
        if (world == null) return;
        if (world.level.choosingCard) return;
        if (Greenfoot.mouseClicked(this)) {
            world.rollPackets();
            AudioPlayer.play(80, "achievement.mp3");
        }
    }
}