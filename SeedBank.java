import greenfoot.*;
import java.util.*;

public class SeedBank extends Actor
{
    public static final int x1 = 400; 
    public static final int x2 = 666;
    public static final int y1 = 32;
    
    private PlayScene playScene;
    public SunCounter sunCounter = new SunCounter();
    private SeedPacket[] bank;
    private SeedPacket selectedPacket = null;
    private TransparentObject ghostImage = null;

    private final int START_X = 400;
    private final int START_Y = 666;
    private final int SPACING_X = 105;

    public SeedBank(SeedPacket[] bank) {
        this.bank = bank;
    }

    public void act() {
        if (playScene == null) {
            playScene = (PlayScene)getWorld();
            if (playScene == null) return; 
        }
        
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse != null) {
            if (playScene.board != null && playScene.board.xSpacing > 0) {
                handleGhostImage(mouse);
                handleMouseClick(mouse);
            }
        }
    }

    public int getSun() {
        return sunCounter.sun;
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
        
        this.bank = newBank;
        
        for (int i = 0; i < bank.length; i++) {
            if (bank[i] != null) {
                getWorld().addObject(bank[i], START_X + (i * SPACING_X), START_Y);
            }
        }
    }

    private void handleGhostImage(MouseInfo mouse) {
        if (ghostImage != null && selectedPacket != null) {
            double calcX = (double)(mouse.getX() - playScene.board.xOffset) / playScene.board.xSpacing;
            double calcY = (double)(mouse.getY() - playScene.board.yOffset) / playScene.board.ySpacing;
            
            int gridX = (int)Math.round(calcX);
            int gridY = (int)Math.round(calcY);

            Plant p = selectedPacket.getPlant();
            
            if (playScene.board.canPlace(gridX, gridY, p)) {
                ghostImage.setTransparent(true); 
                int posX = gridX * playScene.board.xSpacing + playScene.board.xOffset;
                int posY = gridY * playScene.board.ySpacing + playScene.board.yOffset;
                if (p.getClass().getSimpleName().equals("Lilypad")) posY += 10; 
                ghostImage.setLocation(posX, posY);
            } else {
                ghostImage.setTransparent(false);
                ghostImage.setLocation(mouse.getX(), mouse.getY());
            }
        }
    }

    private void handleMouseClick(MouseInfo mouse) {
        if (Greenfoot.mouseClicked(null)) {
            if (playScene.hitbox == null) return;
            playScene.moveHitbox();
            
            if (selectedPacket != null && ghostImage != null) {
                double calcX = (double)(mouse.getX() - playScene.board.xOffset) / playScene.board.xSpacing;
                double calcY = (double)(mouse.getY() - playScene.board.yOffset) / playScene.board.ySpacing;
                
                int gridX = (int)Math.round(calcX);
                int gridY = (int)Math.round(calcY);

                if (playScene.board.placePlant(gridX, gridY, selectedPacket.getPlant())) {
                    sunCounter.removeSun(selectedPacket.sunCost);
                    selectedPacket.startRecharge(); 
                    
                    getWorld().removeObject(ghostImage);
                    ghostImage = null;
                    
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

    private void checkPacketSelection() {
        if (playScene.hitbox == null) return;
        List<Actor> touching = playScene.hitbox.getTouching();
        for (Actor a : touching) {
            if (a instanceof SeedPacket) {
                SeedPacket clicked = (SeedPacket)a;
                
                if (clicked.isUsed && !clicked.name.toLowerCase().contains("lilypad")) return; 

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
        List<Actor> touching = playScene.hitbox.getTouching();
        for (Actor a : touching) {
            if (a instanceof SeedPacket) return true;
        }
        return false;
    }

    @Override
    public void addedToWorld(World world) {
        playScene = (PlayScene)world;
        playScene.addObject(sunCounter, 606, 616);
        for (int i = 0; i < bank.length; i++) {
            if (bank[i] != null) {
                playScene.addObject(bank[i], START_X + (i * SPACING_X), START_Y);
            }
        }
        getImage().setTransparency(0);
    }
}