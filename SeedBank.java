import greenfoot.*;
import java.util.*;


public class SeedBank extends Actor
{
    
    public static final int x1 = 252; 
    public static final int x2 = 994;
    public static final int y1 = 81;

    private PlayScene PlayScene;
    public SunCounter sunCounter = new SunCounter();
    private SeedPacket[] bank;
    private SeedPacket selectedPacket = null;
    private TransparentObject ghostImage = null;

    public SeedBank(SeedPacket[] bank) {
        this.bank = bank;
    }

    public void act() {
       
        if (PlayScene == null) {
            PlayScene = (PlayScene)getWorld();
            if (PlayScene == null) return; 
        }
        
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse != null) {
            
            if (Board.xSpacing > 0 && Board.ySpacing > 0) {
                handleGhostImage(mouse);
                handleMouseClick(mouse);
            }
        }
    }

    /**
     * Xử lý hiển thị ảnh mờ (Ghost Image) bám theo chuột hoặc hút vào ô đất
     */
    private void handleGhostImage(MouseInfo mouse) {
        if (ghostImage != null && selectedPacket != null) {
            
            int gridX = (mouse.getX() - Board.xOffset) / Board.xSpacing;
            int gridY = (mouse.getY() - Board.yOffset) / Board.ySpacing;

            Plant p = selectedPacket.getPlant();
            
            // Kiểm tra xem vị trí chuột có nằm trong bàn cờ và đặt được cây không
            if (PlayScene.board != null && p != null && PlayScene.board.canPlace(gridX, gridY, p)) {
                ghostImage.setTransparent(true); // Làm mờ nhẹ khi đặt được cây
                
                // "Hút" ảnh mờ vào tâm ô đất
                int posX = gridX * Board.xSpacing + Board.xOffset;
                int posY = gridY * Board.ySpacing + Board.yOffset;

                // Căn chỉnh riêng cho Lilypad trên mặt nước
                if (p.getClass().getSimpleName().equals("Lilypad")) {
                    posY += 10; 
                }
                
                ghostImage.setLocation(posX, posY);
            } else {
                // Nếu không đặt được cây, ảnh bám sát theo chuột và hiện rõ hơn
                ghostImage.setTransparent(false);
                ghostImage.setLocation(mouse.getX(), mouse.getY());
            }
        }
    }

    /**
     * Xử lý Click chuột: Chọn thẻ bài hoặc Đặt cây xuống sân
     */
    private void handleMouseClick(MouseInfo mouse) {
        if (Greenfoot.mouseClicked(null)) {
            if (PlayScene.hitbox == null) return;
            PlayScene.moveHitbox();
            
            // 1. Logic Đặt cây (Nếu đang cầm cây)
            if (selectedPacket != null && ghostImage != null) {
                int gridX = (mouse.getX() - Board.xOffset) / Board.xSpacing;
                int gridY = (mouse.getY() - Board.yOffset) / Board.ySpacing;

                if (PlayScene.board != null && PlayScene.board.placePlant(gridX, gridY, selectedPacket.getPlant())) {
                    // Đặt thành công
                    sunCounter.removeSun(selectedPacket.sunCost);
                    getWorld().removeObject(ghostImage);
                    ghostImage = null;
                    
                    selectedPacket.startRecharge();
                    selectedPacket.setSelected(false);
                    selectedPacket = null;
                    return; // Thoát để không chọn nhầm thẻ bài nằm dưới vị trí vừa click
                } else if (!isClickingAnotherPacket()) {
                    // Click ra ngoài hoặc chỗ không đặt được -> Hủy chọn cây
                    cancelSelection();
                }
            }
            
            // 2. Logic Chọn cây từ thanh SeedBank
            checkPacketSelection();
        }
    }

    /**
     * Hủy bỏ việc đang chọn cây (trả cây về túi)
     */
    private void cancelSelection() {
        if (selectedPacket != null) {
            if (ghostImage != null && ghostImage.getWorld() != null) {
                getWorld().removeObject(ghostImage);
            }
            ghostImage = null;
            selectedPacket.setSelected(false);
            selectedPacket = null;
        }
    }

    /**
     * Kiểm tra xem chuột có đang chạm vào một SeedPacket khác không
     */
    private boolean isClickingAnotherPacket() {
        if (PlayScene.hitbox == null) return false;
        List<Actor> touching = PlayScene.hitbox.getTouching();
        for (Actor a : touching) {
            if (a instanceof SeedPacket) return true;
        }
        return false;
    }

    /**
     * Quét các thẻ bài để xử lý việc chọn/đổi cây
     */
    private void checkPacketSelection() {
        if (PlayScene.hitbox == null) return;
        List<Actor> touching = PlayScene.hitbox.getTouching();
        for (Actor a : touching) {
            if (a instanceof SeedPacket) {
                SeedPacket clicked = (SeedPacket)a;
                
                // Nếu click vào thẻ đang chọn -> Hủy chọn
                if (selectedPacket == clicked) {
                    cancelSelection();
                } else {
                    // Kiểm tra điều kiện: Đủ mặt trời và Thẻ đã hồi chiêu xong
                    if (clicked.recharged && sunCounter.sun >= clicked.sunCost) {
                        cancelSelection(); // Xóa ảnh mờ cũ nếu có
                        
                        selectedPacket = clicked;
                        clicked.setSelected(true);
                        ghostImage = clicked.addImage();
                        if (ghostImage != null) {
                            getWorld().addObject(ghostImage, 0, 0);
                        }
                    }
                }
                break; // Chỉ xử lý một thẻ bài mỗi lần click
            }
        }
    }

    @Override
    public void addedToWorld(World world) {
        PlayScene = (PlayScene)world;
        
        // Thêm bảng đếm mặt trời vào góc trái thanh SeedBank
        PlayScene.addObject(sunCounter, 67, 50);
        
        // Tự động sắp xếp các thẻ bài theo chiều dọc
        for (int i = 0; i < bank.length; i++) {
            if (bank[i] != null) {
                PlayScene.addObject(bank[i], 67, 120 + i * 50);
            }
        }
        
        // Ẩn hình ảnh của chính SeedBank (nó chỉ là đối tượng quản lý)
        getImage().setTransparency(0);
    }
}