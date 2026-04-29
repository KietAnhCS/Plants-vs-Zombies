import greenfoot.*;
import java.util.List;

public class DragController extends Actor {

    private final SunCounter  sunCounter;
    private final IPlantPlacer placer;

    private SeedPacket        selectedPacket = null;
    private TransparentObject ghostImage     = null;
    private boolean           isDragging     = false;
    private int               lastGx         = -1;
    private int               lastGy         = -1;

    public DragController(SunCounter sunCounter, IPlantPlacer placer) {
        this.sunCounter = sunCounter;
        this.placer     = placer;
        setImage((GreenfootImage) null);
    }

    // ── act ───────────────────────────────────────────────────
    @Override
    public void act() {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse == null) return;
        handleDrag(mouse);
    }

    // ── drag flow ─────────────────────────────────────────────
    private void handleDrag(MouseInfo mouse) {
        if (!isDragging && Greenfoot.mousePressed(null)) {
            tryPickUp(mouse);
            return; // không xử lý drop cùng frame với pick-up
        }

        if (isDragging && ghostImage != null) {
            updateGhostPosition(mouse);

            if (Greenfoot.mouseDragEnded(null) || Greenfoot.mouseClicked(null)) {
                tryPlace();   // confirm hoặc cancel tùy lastGx/lastGy
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
            if (!(a instanceof SeedPacket)) continue;
            SeedPacket packet = (SeedPacket) a;

            // ── dùng canBeSelected() thay vì isRecharged() + isUsed ──
            if (!packet.canBeSelected()) continue;

            // trySelect() chuyển packet sang SelectedState
            if (!packet.trySelect()) continue;

            ghostImage = packet.addImage();
            if (ghostImage != null) {
                getWorld().addObject(ghostImage, mouse.getX(), mouse.getY());
            }

            selectedPacket = packet;
            isDragging     = true;
            break;
        }
    }

    private void updateGhostPosition(MouseInfo mouse) {
        int mx = mouse.getX();
        int my = mouse.getY();
        int gx = placer.getGridX(mx, my);
        int gy = placer.getGridY(mx, my);

        if (gx >= 0 && gy >= 0
                && placer.canPlace(gx, gy, selectedPacket.getPlant())) {
            ghostImage.setLocation(placer.getXCoord(gx, gy),
                                   placer.getYCoord(gx, gy));
            ghostImage.setTransparent(true);
            lastGx = gx;
            lastGy = gy;
        } else {
            ghostImage.setLocation(mx, my);
            ghostImage.setTransparent(false);
            lastGx = lastGy = -1;
        }
    }

    private void tryPlace() {
        if (lastGx < 0 || lastGy < 0) {
            // thả ngoài grid → huỷ
            selectedPacket.cancelSelect();
            return;
        }

        if (placer.placePlant(lastGx, lastGy, selectedPacket.getPlant())) {
            sunCounter.removeSun(selectedPacket.sunCost);
            selectedPacket.confirmPlace();   // → CoolingState(rechargeTime)
        } else {
            // grid từ chối (ô đã có cây...) → huỷ
            selectedPacket.cancelSelect();
        }
    }

    private void cleanup() {
        if (ghostImage != null && ghostImage.getWorld() != null) {
            getWorld().removeObject(ghostImage);
        }
        ghostImage     = null;
        selectedPacket = null;
        isDragging     = false;
        lastGx         = lastGy = -1;
        // KHÔNG gọi setSelected/cancelSelect ở đây
        // tryPlace() đã gọi confirmPlace() hoặc cancelSelect() rồi
    }
}