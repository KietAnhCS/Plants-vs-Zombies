import greenfoot.*;
import java.util.*;

public class GridManager extends Actor {
    public Plant[][] Board = new Plant[6][9];
    public int currentRowCount = 6;
    
    private final double Ax = 320, Ay = 177;
    private final double Dx = 807, Dy = 177;
    private final double Bx = 249, By = 521;
    private final double Cx = 843, Cy = 521;

    public GridManager() {
        GreenfootImage img = new GreenfootImage(1111, 698);
        img.clear();
        setImage(img);
    }

    public void act() {
        drawGridHighlights();
    }

    public int getXCoord(int col, int row) {
        double u = col / 9.0; 
        double v = row / 6.0;
        return (int)((1.0-u)*(1.0-v)*Ax + u*(1.0-v)*Dx + (1.0-u)*v*Bx + u*v*Cx);
    }

    public int getYCoord(int col, int row) {
        double u = col / 9.0;
        double v = row / 6.0;
        return (int)((1.0-u)*(1.0-v)*Ay + u*(1.0-v)*Dy + (1.0-u)*v*By + u*v*Cy);
    }

    public int getGridX(int mx, int my) {
        int gx = -1;
        double minDist = 60;
        for(int r = 0; r < 6; r++) {
            for(int c = 0; c < 9; c++) {
                double d = Math.hypot(mx - (getXCoord(c, r) + (getXCoord(c+1, r) - getXCoord(c, r))/2), 
                                      my - (getYCoord(c, r) + (getYCoord(c, r+1) - getYCoord(c, r))/2));
                if (d < minDist) {
                    minDist = d; gx = c;
                }
            }
        }
        return gx;
    }

    public int getGridY(int mx, int my) {
        int gy = -1;
        double minDist = 60;
        for(int r = 0; r < 6; r++) {
            for(int c = 0; c < 9; c++) {
                double d = Math.hypot(mx - (getXCoord(c, r) + (getXCoord(c+1, r) - getXCoord(c, r))/2), 
                                      my - (getYCoord(c, r) + (getYCoord(c, r+1) - getYCoord(c, r))/2));
                if (d < minDist) {
                    minDist = d; gy = r;
                }
            }
        }
        return gy;
    }

    public int clampRow(int row) {
        if (row < 0) return 0;
        if (row >= currentRowCount) return currentRowCount - 1;
        return row;
    }

    private void drawGridHighlights() {
        GreenfootImage canvas = getImage();
        canvas.clear(); 
        
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse == null) return;

        Actor dragging = null;
        
        List<Plant> plants = getWorld().getObjects(Plant.class);
        for (Plant p : plants) {
            if (p.getImage().getTransparency() < 255) {
                dragging = p;
                break;
            }
        }
        
        if (dragging == null) {
            List<SeedPacket> packets = getWorld().getObjects(SeedPacket.class);
            for (SeedPacket s : packets) {
                if (Greenfoot.mouseDragged(s) || Greenfoot.mousePressed(s) || Greenfoot.mouseClicked(s)) {
                    dragging = s;
                    break;
                }
            }
        }

        if (dragging != null) {
            int gx = getGridX(mouse.getX(), mouse.getY());
            int gy = getGridY(mouse.getX(), mouse.getY());
            
            if (gx >= 0 && gx < 9 && gy >= 0 && gy < 6) {
                boolean can = false;
                if (dragging instanceof Plant) {
                    can = canPlace(gx, gy, (Plant)dragging);
                } else if (dragging instanceof SeedPacket) {
                    can = canPlace(gx, gy, ((SeedPacket)dragging).getPlant());
                }

                int x1 = getXCoord(gx, gy), y1 = getYCoord(gx, gy);
                int x2 = getXCoord(gx + 1, gy), y2 = getYCoord(gx + 1, gy);
                int x3 = getXCoord(gx + 1, gy + 1), y3 = getYCoord(gx + 1, gy + 1);
                int x4 = getXCoord(gx, gy + 1), y4 = getYCoord(gx, gy + 1);

                int[] px = {x1, x2, x3, x4};
                int[] py = {y1, y2, y3, y4};

                canvas.setColor(can ? new Color(0, 255, 0, 100) : new Color(255, 0, 0, 100));
                canvas.fillPolygon(px, py, 4);
                canvas.setColor(can ? Color.GREEN : Color.RED);
                canvas.drawPolygon(px, py, 4);
            }
        }
    }

    public boolean canPlace(int x, int y, Plant plant) {
        if (x < 0 || x >= 9 || y < 0 || y >= 6) return false;
        Plant target = Board[y][x];
        if (target == null || target == plant) return true;
        return UpgradeManager.canUpgrade(plant, target);
    }

    public void updateBoardData(int x, int y, Plant plant) {
        if (x >= 0 && x < 9 && y >= 0 && y < 6) {
            Board[y][x] = plant;
        }
    }

    public boolean placePlant(int x, int y, Plant plant) {
        if (!canPlace(x, y, plant)) return false;
        
        int tx = getXCoord(x, y) + (getXCoord(x+1, y) - getXCoord(x, y))/2;
        int ty = getYCoord(x, y) + (getYCoord(x, y+1) - getYCoord(x, y))/2;
        Plant target = Board[y][x];
    
        int oldR = -1, oldC = -1;
        for (int r = 0; r < 6; r++) {
            for (int c = 0; c < 9; c++) {
                if (Board[r][c] == plant) { oldR = r; oldC = c; }
            }
        }
    
        if (target != null && target != plant) {
            Plant special = UpgradeManager.getSpecialUpgrade(plant, target);
            if (special != null) {
                if (oldR >= 0) Board[oldR][oldC] = null;
                removePlant(x, y);
                if (plant.getWorld() != null) getWorld().removeObject(plant);
                getWorld().addObject(special, tx, ty);
                Board[y][x] = special;
                return true;
            }
            return false; 
        }
        
        if (oldR >= 0) Board[oldR][oldC] = null;
        Board[y][x] = plant;
        if (plant.getWorld() == null) {
            getWorld().addObject(plant, tx, ty);
        } else {
            plant.setLocation(tx, ty);
        }
        if (getWorld() instanceof PlayScene) ((PlayScene)getWorld()).checkAndCombine(plant);
        return true;
    }

    public void removePlant(int x, int y) {
        if (x >= 0 && x < 9 && y >= 0 && y < 6) {
            Plant p = Board[y][x];
            if (p != null) {
                if (p.getWorld() != null) {
                    getWorld().removeObject(p);
                }
                Board[y][x] = null;
            }
        }
    }
}