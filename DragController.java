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
        setImage((GreenfootImage) null);
    }

    @Override
    public void act() {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse == null) return;
        handleDrag(mouse);
    }

    private void handleDrag(MouseInfo mouse) {
        if (!isDragging && Greenfoot.mousePressed(null)) {
            tryPickUp(mouse);
            return;
        }

        if (isDragging && ghostImage != null) {
            updateGhostPosition(mouse);

            if (Greenfoot.mouseClicked(null) || Greenfoot.mouseDragEnded(null)) {
                processPlacement();
                cleanup();
            }
        }
    }

    private void tryPickUp(MouseInfo mouse) {
        PlayScene scene = (PlayScene) getWorld();
        if (scene == null || scene.hitbox == null) return;

        scene.moveHitbox();
        List<Actor> touching = scene.hitbox.getTouching();

        for (Actor a : touching) {
            if (a instanceof SeedPacket) {
                SeedPacket packet = (SeedPacket) a;
                if (packet.canBePurchased() && packet.tryPurchase()) {
                    ghostImage = packet.addImage();
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

        if (gx >= 0 && gy >= 0 && plantToPlace != null && placer.canPlace(gx, gy, plantToPlace)) {
            ghostImage.setLocation(placer.getXCoord(gx, gy), placer.getYCoord(gx, gy));
            ghostImage.setTransparent(true);
            lastGx = gx;
            lastGy = gy;
        } else {
            ghostImage.setLocation(mx, my);
            ghostImage.setTransparent(false);
            lastGx = lastGy = -1;
        }
    }

    private void processPlacement() {
        if (lastGx >= 0 && lastGy >= 0) {
            if (executePlacement(lastGx, lastGy)) return;
        }

        if (autoPlaceInQueue()) return;

        System.out.println("Khong dat duoc: San da day!");
        if (selectedPacket != null) selectedPacket.cancelSelect();
    }

    private boolean autoPlaceInQueue() {
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
        if (placer.placePlant(x, y, plantToPlace)) {
            if (sunDisplay != null) sunDisplay.removeSun(selectedPacket.sunCost);
            
            selectedPacket.confirmPlace();
            
            PlayScene scene = (PlayScene) getWorld();
            if (scene != null) scene.checkAndCombine(plantToPlace);
            
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