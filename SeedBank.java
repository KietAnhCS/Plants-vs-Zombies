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
            // Kiểm tra thông số từ đối tượng board cụ thể, KHÔNG dùng static
            if (PlayScene.board != null && PlayScene.board.xSpacing > 0) {
                handleGhostImage(mouse);
                handleMouseClick(mouse);
            }
        }
    }

    private void handleGhostImage(MouseInfo mouse) {
        if (ghostImage != null && selectedPacket != null) {
            // SỬA LỖI: Sử dụng double và Math.round để tính grid chính xác
            double calcX = (double)(mouse.getX() - PlayScene.board.xOffset) / PlayScene.board.xSpacing;
            double calcY = (double)(mouse.getY() - PlayScene.board.yOffset) / PlayScene.board.ySpacing;
            
            int gridX = (int)Math.round(calcX);
            int gridY = (int)Math.round(calcY);

            Plant p = selectedPacket.getPlant();
            
            if (PlayScene.board.canPlace(gridX, gridY, p)) {
                ghostImage.setTransparent(true); 
                
                // Tọa độ đặt cây phải lấy từ đối tượng board hiện tại
                int posX = gridX * PlayScene.board.xSpacing + PlayScene.board.xOffset;
                int posY = gridY * PlayScene.board.ySpacing + PlayScene.board.yOffset;

                if (p.getClass().getSimpleName().equals("Lilypad")) {
                    posY += 10; 
                }
                
                ghostImage.setLocation(posX, posY);
            } else {
                ghostImage.setTransparent(false);
                ghostImage.setLocation(mouse.getX(), mouse.getY());
            }
        }
    }

    private void handleMouseClick(MouseInfo mouse) {
        if (Greenfoot.mouseClicked(null)) {
            if (PlayScene.hitbox == null) return;
            PlayScene.moveHitbox();
            
            if (selectedPacket != null && ghostImage != null) {
                // SỬA LỖI: Đồng bộ logic tính toán grid với hàm hiển thị ghost image
                double calcX = (double)(mouse.getX() - PlayScene.board.xOffset) / PlayScene.board.xSpacing;
                double calcY = (double)(mouse.getY() - PlayScene.board.yOffset) / PlayScene.board.ySpacing;
                
                int gridX = (int)Math.round(calcX);
                int gridY = (int)Math.round(calcY);

                if (PlayScene.board.placePlant(gridX, gridY, selectedPacket.getPlant())) {
                    sunCounter.removeSun(selectedPacket.sunCost);
                    getWorld().removeObject(ghostImage);
                    ghostImage = null;
                    
                    selectedPacket.startRecharge();
                    selectedPacket.setSelected(false);
                    selectedPacket = null;
                    return; 
                } else if (!isClickingAnotherPacket()) {
                    cancelSelection();
                }
            }
            checkPacketSelection();
        }
    }

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

    private boolean isClickingAnotherPacket() {
        if (PlayScene.hitbox == null) return false;
        List<Actor> touching = PlayScene.hitbox.getTouching();
        for (Actor a : touching) {
            if (a instanceof SeedPacket) return true;
        }
        return false;
    }

    private void checkPacketSelection() {
        if (PlayScene.hitbox == null) return;
        List<Actor> touching = PlayScene.hitbox.getTouching();
        for (Actor a : touching) {
            if (a instanceof SeedPacket) {
                SeedPacket clicked = (SeedPacket)a;
                if (selectedPacket == clicked) {
                    cancelSelection();
                } else {
                    if (clicked.recharged && sunCounter.sun >= clicked.sunCost) {
                        cancelSelection(); 
                        selectedPacket = clicked;
                        clicked.setSelected(true);
                        ghostImage = clicked.addImage();
                        if (ghostImage != null) {
                            getWorld().addObject(ghostImage, 0, 0);
                        }
                    }
                }
                break; 
            }
        }
    }

    @Override
    public void addedToWorld(World world) {
        PlayScene = (PlayScene)world;
        PlayScene.addObject(sunCounter, 67, 50);
        for (int i = 0; i < bank.length; i++) {
            if (bank[i] != null) {
                PlayScene.addObject(bank[i], 67, 120 + i * 50);
            }
        }
        getImage().setTransparency(0);
    }
}