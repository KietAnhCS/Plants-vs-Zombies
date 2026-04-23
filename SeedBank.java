import greenfoot.*;
import java.util.*;

public class SeedBank extends Actor {
    private PlayScene playScene;
    public SunCounter sunCounter = new SunCounter();
    private SeedPacket[] bank;
    private SeedPacket selectedPacket = null;
    private TransparentObject ghostImage = null;
    private boolean isDragging = false;
    private int lastGx = -1, lastGy = -1;

    private final int START_X = 400;
    private final int START_Y = 666;
    private final int SPACING_X = 105;
    private boolean isTDActive = false;

    public SeedBank(SeedPacket[] bank) {
        this.bank = bank;
    }

    public void act() {
        if (playScene == null) {
            playScene = (PlayScene) getWorld();
            if (playScene == null) return;
        }
        
        MouseInfo mouse = Greenfoot.getMouseInfo();
        handleDrag(mouse);
    }

    private void handleDrag(MouseInfo mouse) {
        if (mouse == null) return;

        if (!isDragging && Greenfoot.mousePressed(null)) {
            if (playScene.hitbox == null) return;
            playScene.moveHitbox();
            List<Actor> touching = playScene.hitbox.getTouching();
            
            for (Actor a : touching) {
                if (a instanceof SeedPacket) {
                    SeedPacket clicked = (SeedPacket) a;
                    // Kiểm tra điều kiện sử dụng
                    if (clicked.isUsed && !clicked.name.toLowerCase().contains("lilypad")) continue;
                    if (clicked.recharged && sunCounter.sun >= clicked.sunCost) {
                        selectedPacket = clicked;
                        selectedPacket.setSelected(true);
                        ghostImage = selectedPacket.addImage();
                        if (ghostImage != null) {
                            getWorld().addObject(ghostImage, mouse.getX(), mouse.getY());
                        }
                        isDragging = true;
                        break;
                    }
                }
            }
        }

        if (isDragging && ghostImage != null) {
            int mx = mouse.getX();
            int my = mouse.getY();
            
            int gx = playScene.board.getGridX(mx, my);
            int gy = playScene.board.getGridY(mx, my);

            if (gx >= 0 && gy >= 0 && playScene.board.canPlace(gx, gy, selectedPacket.getPlant())) {
                
                int tx = playScene.board.getXCoord(gx, gy); 
                int ty = playScene.board.getYCoord(gx, gy);
                
                ghostImage.setLocation(tx, ty);
                ghostImage.setTransparent(true); 
                lastGx = gx;
                lastGy = gy;
            } else {
                
                ghostImage.setLocation(mx, my);
                ghostImage.setTransparent(false);
                lastGx = -1;
                lastGy = -1;
            }

            if (Greenfoot.mouseClicked(null)) {
                if (lastGx >= 0 && lastGy >= 0) {
                    if (playScene.board.placePlant(lastGx, lastGy, selectedPacket.getPlant())) {
                        sunCounter.removeSun(selectedPacket.sunCost);
                        selectedPacket.startRecharge();
                    }
                }
                cleanup();
            }
        }
    }

    private void cleanup() {
        if (ghostImage != null && ghostImage.getWorld() != null) {
            getWorld().removeObject(ghostImage);
        }
        if (selectedPacket != null) {
            selectedPacket.setSelected(false);
        }
        ghostImage = null;
        selectedPacket = null;
        isDragging = false;
        lastGx = -1;
        lastGy = -1;
    }

    public void addSun(int amount) {
        if (amount >= 0) sunCounter.addSun(amount);
        else sunCounter.removeSun(Math.abs(amount));
    }

    public void updateBank(SeedPacket[] newBank) {
        for (SeedPacket oldPacket : this.bank) {
            if (oldPacket != null && oldPacket.getWorld() != null) {
                getWorld().removeObject(oldPacket);
            }
        }
        if (isTDActive) {
            newBank[0] = new BonkchoyPacket();
            isTDActive = false;
        }
        this.bank = newBank;
        for (int i = 0; i < bank.length; i++) {
            if (bank[i] != null) {
                getWorld().addObject(bank[i], START_X + (i * SPACING_X), START_Y);
            }
        }
    }

    @Override
    public void addedToWorld(World world) {
        playScene = (PlayScene) world;
        playScene.addObject(sunCounter, 606, 616);
        for (int i = 0; i < bank.length; i++) {
            if (bank[i] != null) {
                playScene.addObject(bank[i], START_X + (i * SPACING_X), START_Y);
            }
        }
        getImage().setTransparency(0); 
    }
    
    public void setTD(boolean active) { this.isTDActive = active; }
    public int getSun() { return sunCounter.sun; }
}