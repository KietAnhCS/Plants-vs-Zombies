import greenfoot.*;
import java.util.List;

public class DragController extends Actor {
    private final SunDisplay sunDisplay;
    private final IPlantPlacer placer;
    private SeedPacket selectedPacket = null;
    private TransparentObject ghostImage = null;
    private Plant plantToPlace = null; 
    private boolean isDragging = false;
    private int lastGx = -1;
    private int lastGy = -1;

    public DragController(SunDisplay sunDisplay, IPlantPlacer placer) {
        this.sunDisplay = sunDisplay;
        this.placer = placer;
        setImage((GreenfootImage) null); // Actor điều khiển không cần hình ảnh riêng
    }

    @Override
    public void act() {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse == null) return;
        handleDrag(mouse);
    }

    private void handleDrag(MouseInfo mouse) {
        // Nhấn chuột trái để nhặt túi hạt giống
        if (!isDragging && Greenfoot.mousePressed(null) && mouse.getButton() == 1) {
            tryPickUp(mouse);
            return;
        }

        if (isDragging && ghostImage != null) {
            updateGhostPosition(mouse);

            // Thả chuột hoặc click để đặt cây
            if (Greenfoot.mouseClicked(null) || Greenfoot.mouseDragEnded(null)) {
                processPlacement();
                cleanup();
            }
        }
    }

    private void tryPickUp(MouseInfo mouse) {
        PlayScene scene = (PlayScene) getWorld();
        if (scene == null || scene.hitbox == null) return;

        scene.moveHitbox(); // Cập nhật vị trí hitbox theo chuột
        List<Actor> touching = scene.hitbox.getTouching();

        for (Actor a : touching) {
            if (a instanceof SeedPacket) {
                SeedPacket packet = (SeedPacket) a;
                // Kiểm tra xem đủ tiền và hồi chiêu chưa
                if (packet.tryPurchase()) {
                    ghostImage = packet.addImage(); // Lấy ảnh mờ của cây
                    plantToPlace = packet.getPlant(); 
                    
                    if (ghostImage != null && getWorld() != null) {
                        getWorld().addObject(ghostImage, mouse.getX(), mouse.getY());
                    }

                    selectedPacket = packet;
                    isDragging = true;
                    break;
                }
            }
        }
    }

    private void updateGhostPosition(MouseInfo mouse) {
        int mx = mouse.getX();
        int my = mouse.getY();
        int gx = placer.getGridX(mx, my);
        int gy = placer.getGridY(mx, my);

        // Nếu chuột đang nằm trong một ô có thể đặt được
        if (gx >= 0 && gy >= 0 && plantToPlace != null && placer.canPlace(gx, gy, plantToPlace)) {
            // Hút Ghost Image vào giữa ô lục giác
            ghostImage.setLocation(placer.getXCoord(gx, gy), placer.getYCoord(gx, gy));
            ghostImage.setTransparent(true);
            lastGx = gx;
            lastGy = gy;
        } else {
            // Bay tự do theo chuột nếu không ở trong ô hợp lệ
            ghostImage.setLocation(mx, my);
            ghostImage.setTransparent(false);
            lastGx = lastGy = -1;
        }
    }

    private void processPlacement() {
        if (lastGx >= 0 && lastGy >= 0) {
            if (executePlacement(lastGx, lastGy)) return;
        }

        // Nếu thả chuột ra ngoài Grid, tự động tìm chỗ trống ở hàng Bench (hàng 5)
        if (autoPlaceInQueue()) return;

        // Nếu không đặt được đâu cả thì hủy chọn
        if (selectedPacket != null) selectedPacket.cancelSelect();
    }

    private boolean autoPlaceInQueue() {
        if (plantToPlace == null) return false;
        // Ưu tiên hàng 5 (hàng chờ/bench) từ dưới lên
        for (int y = 5; y >= 0; y--) { 
            for (int x = 0; x < 9; x++) { 
                if (placer.canPlace(x, y, plantToPlace)) {
                    return executePlacement(x, y);
                }
            }
        }
        return false;
    }

    private boolean executePlacement(int x, int y) {
        if (selectedPacket == null) return false;
        if (placer.placePlant(x, y, plantToPlace)) {
            if (sunDisplay != null) {
                sunDisplay.removeSun(selectedPacket.sunCost);
            }
            selectedPacket.used();
            PlayScene scene = (PlayScene) getWorld();
            if (scene != null) {
                PlantCombineHandler.checkAndCombine(scene, plantToPlace);
            }
            return true;
        }
        return false;
    }

    private void cleanup() {
        if (ghostImage != null && ghostImage.getWorld() != null) {
            getWorld().removeObject(ghostImage);
        }
        ghostImage = null;
        selectedPacket = null;
        plantToPlace = null; 
        isDragging = false;
        lastGx = lastGy = -1;
    }
}