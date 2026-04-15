import greenfoot.*;

public class clickPlantFood extends Actor
{
    private PlantFood master;

    public clickPlantFood(PlantFood master) {
        this.master = master;
        setImage("shovel1.png"); // Ảnh cái lá nhỏ theo chuột
    }

    public void act() {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse != null) {
            // Cho cái lá đi theo chuột
            setLocation(mouse.getX(), mouse.getY());

            // Nếu click chuột lần nữa
            if (Greenfoot.mouseClicked(null)) {
                // Kiểm tra xem có click trúng cây nào không
                Plant targetPlant = (Plant) getOneIntersectingObject(Plant.class);
                
                if (targetPlant != null) {
                    // THAY VÌ XÓA CÂY NHƯ XẺNG, TA GỌI HÀM BUFF
                    targetPlant.activatePlantFood(); 
                    
                    // Sau khi dùng xong thì biến mất
                    master.setSelected(false);
                    getWorld().removeObject(this);
                } else {
                    // Nếu click ra ngoài (không trúng cây), hủy chọn
                    master.setSelected(false);
                    getWorld().removeObject(this);
                }
            }
        }
    }
}