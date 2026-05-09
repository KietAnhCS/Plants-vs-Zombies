import greenfoot.*;
import java.util.List;

public class DragController extends Actor {
    private final SunDisplay sunDisplay;
    private final IPlantPlacer placer;
    private SeedPacket selectedPacket = null;
    private Plant plantToPlace = null;
    private boolean isDragging = false;
    private boolean wasDragging = false;
    private int lastMouseX = -1;
    private int lastMouseY = -1;
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

        if (!isDragging && Greenfoot.mousePressed(null) && mouse.getButton() == 1) {
            tryPickUp(mouse);
            return;
        }

        if (isDragging && plantToPlace != null) {
            int mx = mouse.getX();
            int my = mouse.getY();

            if (mx != lastMouseX || my != lastMouseY) {
                lastMouseX = mx;
                lastMouseY = my;
                updatePlantPosition(mx, my);
                wasDragging = true;
            }

            if (Greenfoot.mouseClicked(null)) {
                if (wasDragging) {
                    processDrop(mouse);
                } else {
                    autoPlaceOrCancel();
                }
            }

            if (Greenfoot.mouseDragEnded(null)) {
                processDrop(mouse);
            }
        }
    }

    private void tryPickUp(MouseInfo mouse) {
        PlayScene scene = (PlayScene) getWorld();
        if (scene == null) return;

        List<SeedPacket> packets = scene.getObjectsAt(mouse.getX(), mouse.getY(), SeedPacket.class);
        for (SeedPacket packet : packets) {
            if (packet.tryPurchase()) {
                plantToPlace = packet.getPlant();
                if (plantToPlace == null) break;
                selectedPacket = packet;
                isDragging = true;
                wasDragging = false;
                lastMouseX = mouse.getX();
                lastMouseY = mouse.getY();
                plantToPlace.isDragging = true;
                plantToPlace.setState(PlantState.DRAGGING);
                scene.addObject(plantToPlace, mouse.getX(), mouse.getY());
                break;
            }
        }
    }

    private void updatePlantPosition(int mx, int my) {
        int gx = placer.getGridX(mx, my);
        int gy = placer.getGridY(mx, my);

        if (gx >= 0 && gy >= 0 && plantToPlace != null && placer.canPlace(gx, gy, plantToPlace)) {
            plantToPlace.setLocation(placer.getXCoord(gx, gy), placer.getYCoord(gx, gy));
            lastGx = gx;
            lastGy = gy;
        } else {
            plantToPlace.setLocation(mx, my);
            lastGx = lastGy = -1;
        }
    }

    private void processDrop(MouseInfo mouse) {
        if (plantToPlace == null) { cleanup(); return; }
        PlayScene scene = (PlayScene) getWorld();
        if (scene == null) { cleanup(); return; }

        plantToPlace.isDragging = false;

        int gx = lastGx >= 0 ? lastGx : placer.getGridX(mouse.getX(), mouse.getY());
        int gy = lastGy >= 0 ? lastGy : placer.getGridY(mouse.getX(), mouse.getY());

        if (gx >= 0 && gy >= 0 && placer.placePlant(gx, gy, plantToPlace)) {
            confirmPlacement(scene);
        } else if (autoPlace(scene)) {
            confirmPlacement(scene);
        } else {
            cancelPlacement();
            return;
        }
        cleanup();
    }

    private void autoPlaceOrCancel() {
        PlayScene scene = (PlayScene) getWorld();
        if (scene == null) { cancelPlacement(); return; }

        plantToPlace.isDragging = false;

        if (autoPlace(scene)) {
            confirmPlacement(scene);
            cleanup();
        } else {
            cancelPlacement();
        }
    }

    private boolean autoPlace(PlayScene scene) {
        if (plantToPlace == null) return false;
        for (int x = 0; x < 9; x++) {
            if (placer.canPlace(x, 5, plantToPlace)) {
                if (placer.placePlant(x, 5, plantToPlace)) {
                    plantToPlace.syncGridPosition();
                    plantToPlace.setState(PlantState.IDLE);
                    return true;
                }
            }
        }
        return false;
    }

    private void confirmPlacement(PlayScene scene) {
        plantToPlace.syncGridPosition();
        plantToPlace.setState(PlantState.IDLE);
        if (sunDisplay != null) sunDisplay.removeSun(selectedPacket.sunCost);
        selectedPacket.used();
        PlantCombineHandler.checkAndCombine(scene, plantToPlace);
    }

    private void cancelPlacement() {
        if (plantToPlace != null && plantToPlace.getWorld() != null) {
            getWorld().removeObject(plantToPlace);
        }
        if (selectedPacket != null) selectedPacket.cancelSelect();
        cleanup();
    }

    private void cleanup() {
        selectedPacket = null;
        plantToPlace = null;
        isDragging = false;
        wasDragging = false;
        lastGx = lastGy = -1;
        lastMouseX = lastMouseY = -1;
    }
}