import greenfoot.*;

public class PlantFood extends SmoothMover
{
    public boolean selected = false;
    
    public void addedToWorld(World world) {
        setImage("plantfood.png"); // Ảnh cái lá trên menu
        selected = false;
    }

    public void act() {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse != null) {
            if (Greenfoot.mouseClicked(null)) {
                PlayScene myWorld = (PlayScene)getWorld();
                myWorld.moveHitbox(); // Giữ nguyên cơ chế dùng hitbox của bạn
                
                if (intersects(myWorld.hitbox)) {
                    if (!selected) {
                        selected = true;
                        // AudioPlayer.play(80, "powerup_select.mp3"); 
                        // Tạo đối tượng cái lá bay theo chuột
                        myWorld.addObject(new clickPlantFood(this), mouse.getX(), mouse.getY());
                    }
                }
            }
        }
    }

    public void setSelected(boolean bool) {
        selected = bool;
        // Có thể đổi highlight ảnh ở đây nếu muốn
    }
}