import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * Write a description of class SeedBank here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class SeedBank extends Actor
{
    /**
     * Act - do whatever the SeedBank wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
   
    public MyWorld MyWorld;
    public SunCounter suncounter = new SunCounter();
    public SeedPacket[] bank;
    public SeedPacket selectedPacket = null;
    public TransparentObject image = null;
    public TransparentObject transparent = null;

    public static final int x1 = 252;
    public static final int x2 = 994;
    public static final int xSpacing = Board.xSpacing;
    public static final int y1 = 81;
    public static final int y2 = 581;
    public static final int ySpacing = Board.ySpacing;
    
    public SeedBank(SeedPacket[] bank) {
        
        this.bank = bank;
    }
    public void act() {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse != null) {
            if (image != null) {
                int x = (int)((mouse.getX() - x1) / xSpacing);
                int y = (int)((mouse.getY() - y1) / ySpacing);

                boolean insideBoard = (x >= 0 && x < 9 && y >= 0 && y < 5);
    
                if (!insideBoard || (mouse.getX() < x1 || mouse.getX() > x2 || mouse.getY() < y1 || mouse.getY() > y2) || MyWorld.board.Board[y][x] != null) {
        
                    image.setTransparent(false);
                    image.setLocation(mouse.getX(), mouse.getY());
            } else {
        image.setTransparent(true);
        image.setLocation(x * Board.xSpacing + Board.xOffset, y * Board.ySpacing + Board.yOffset);
    }
} else {
                
            }
            
            if (Greenfoot.mouseClicked(null)) {
                MyWorld.moveHitbox();
                //Debug: System.out.println(mouse.getX()+" "+ mouse.getY());
                
                if (image != null) {
                    if (mouse.getX() < x1 || mouse.getX() > x2 || mouse.getY() < y1 || mouse.getY() > y2) {
                        MyWorld.removeObject(image);
                        image = null;
                        boolean selected = false;
                        AudioPlayer.play(80,"tap.mp3", "tap2.mp3");
                        if (MyWorld.hitbox.getTouching().contains(selectedPacket)) {
                                selected = true;
                        }
                        if (!selected) {
                            selectedPacket.setSelected(false);
                            selectedPacket = null;
                        }
                        
                    } else {
                        int x = (int)((mouse.getX()-x1)/xSpacing);
                        int y = (int)((mouse.getY()-y1)/ySpacing);
                        if (MyWorld.board.Board[y][x] == null) {
                            MyWorld.board.placePlant(x, y, selectedPacket.getPlant());
                            suncounter.removeSun(selectedPacket.sunCost);
                            getWorld().removeObject(image);
                            image = null;
                            selectedPacket.startRecharge();
                            selectedPacket.setRecharged(false);
                            selectedPacket.setSelected(false);
                            selectedPacket = null;
                        }
                    }
                }
                
                
                
                
                
                
                for (Object i : MyWorld.hitbox.getTouching()) {
                    if (i instanceof SeedPacket) {
                        SeedPacket clicked = (SeedPacket)i;
                        
                        if (selectedPacket != clicked) {
                            if (clicked.recharged) {
                                if (selectedPacket != null) {
                                    selectedPacket.setSelected(false);
                                    selectedPacket = null;
                                }
                                selectedPacket = clicked;
                                clicked.setSelected(true);
                                AudioPlayer.play(80, "seedlift.mp3");
                                image = clicked.addImage();
                            } else {
                                AudioPlayer.play(80, "buzzer.mp3");
                            }
                        } else {
                            if (clicked.recharged) {
                                selectedPacket = null;
                                clicked.setSelected(false);
                                AudioPlayer.play(80, "seedlift.mp3");
                            }
                        }
                
                    }
            
                }
                
                
                
            }
            
            
        }
    }
    @Override
    public void addedToWorld(World world) {
        MyWorld = (MyWorld)getWorld();
        MyWorld.addObject(suncounter, 67, 50);
        for (int i = 0; i < bank.length; i++) {
            MyWorld.addObject(bank[i], 67, 120+i*50);
        }
    }
    
}
