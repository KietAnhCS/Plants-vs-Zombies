import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * SeedPacket dành riêng cho Lilypad.
 */
public class LilypadPacket extends SeedPacket
{
    public LilypadPacket() {
        // Tham số: RechargeTime (36L), ban đầu chưa hồi (false), giá (25), tên ("Lilypad")
        super(36L, false, 25, "Lilypadpacket"); 
    }
    
    @Override
    public TransparentObject addImage() {
        // Tạo ảnh mờ Lilypad đi theo chuột khi người dùng chọn thẻ
        TransparentObject temp = new TransparentLilypad(false);
        // Kiểm tra chuột không null để tránh crash khi add vào thế giới
        if (Greenfoot.getMouseInfo() != null) {
            ((PlayScene)getWorld()).addObject(temp, Greenfoot.getMouseInfo().getX(), Greenfoot.getMouseInfo().getY());
        }
        return temp;
    }

    @Override
    public Plant getPlant() {
        // Trả về đúng đối tượng Lilypad để MyWorld thực hiện tryPlacePlant
        return new Lilypad();
    }
}