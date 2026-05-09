import greenfoot.*;
import java.util.*;

public class GridManager extends Actor implements IPlantPlacer {
    public Plant[][] Board = new Plant[6][9];
    public int currentRowCount = 6;
    public int playerLevel = 1;
    private int bonusSlots = 0;
    private GreenfootImage iuLogo;

    private final int HEX_R = 40;
    private final int COLS = 9;
    private final int ROWS = 6;
    private final int ORIGIN_X = 285;
    private final int ORIGIN_Y = 200;
    private final double HEX_W = HEX_R * Math.sqrt(3);
    private final double HEX_H = HEX_R * 2.0;
    private final double STEP_X = HEX_W;
    private final double STEP_Y = HEX_H * 0.75;

    public GridManager() {
        GreenfootImage img = new GreenfootImage(1111, 698);
        setImage(img);
        try {
            iuLogo = new GreenfootImage("iu_logo.png");
        } catch (Exception e) {
            iuLogo = new GreenfootImage(50, 50);
            iuLogo.setColor(new Color(0, 51, 102));
            iuLogo.fillOval(0, 0, 50, 50);
        }
    }

    public void addBonusSlots(int amount) {
        this.bonusSlots += amount;
    }

    @Override
    public void act() {
        GreenfootImage canvas = getImage();
        canvas.clear();
        drawStatus(canvas);
        Actor dragging = getDraggingActor();
        if (dragging != null) {
            drawFullGrid(canvas);
            drawGridHighlights(canvas, dragging);
        }
    }

    private void drawStatus(GreenfootImage canvas) {
        String status = "PLANTS: " + getCurrentPlantCount() + "/" + getMaxCapacity();
        int sx = 20, sy = 20;
        canvas.setColor(new Color(0, 0, 0, 160));
        canvas.fillRect(sx + 4, sy + 4, 180, 40);
        canvas.setColor(new Color(50, 50, 50));
        canvas.fillRect(sx, sy, 180, 40);
        canvas.setColor(Color.WHITE);
        canvas.drawRect(sx, sy, 180, 40);
        canvas.drawRect(sx + 1, sy + 1, 178, 38);
        canvas.setFont(new Font("Courier New", true, false, 20));
        canvas.setColor(Color.BLACK);
        canvas.drawString(status, sx + 12, sy + 28);
        canvas.setColor(new Color(255, 215, 0));
        canvas.drawString(status, sx + 10, sy + 26);
    }

    private void drawFullGrid(GreenfootImage canvas) {
        canvas.setColor(new Color(100, 180, 255, 60));
        for (int r = 0; r < ROWS; r++)
            for (int c = 0; c < COLS; c++)
                drawHexagon(canvas, getXCoord(c, r), getYCoord(c, r));
    }

    private void drawGridHighlights(GreenfootImage canvas, Actor dragging) {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse == null) return;
        int[] grid = getGridPos(mouse.getX(), mouse.getY());
        int gx = grid[0], gy = grid[1];
        if (gx < 0 || gy < 0) return;

        Plant plant = null;
        if (dragging instanceof Plant) plant = (Plant) dragging;
        else if (dragging instanceof SeedPacket) plant = ((SeedPacket) dragging).getPlant();
        
        if (plant == null) return;

        boolean can = canPlace(gx, gy, plant);
        int cx = getXCoord(gx, gy);
        int cy = getYCoord(gx, gy);
        int targetSize = 65;

        canvas.setColor(can ? new Color(0, 100, 255, 180) : new Color(255, 0, 0, 150));
        fillHexagon(canvas, cx, cy);

        canvas.setColor(new Color(0, 255, 255));
        drawHexagon(canvas, cx, cy);

        GreenfootImage logoCopy = new GreenfootImage(iuLogo);
        logoCopy.scale(targetSize, targetSize);
        logoCopy.setTransparency(60);
        canvas.drawImage(logoCopy, cx - (targetSize / 2), cy - (targetSize / 2));

        canvas.setColor(Color.WHITE);
        canvas.drawOval(cx - (targetSize / 2), cy - (targetSize / 2), targetSize, targetSize);
    }

    public int[] getGridPos(int mx, int my) {
        int bestX = -1, bestY = -1;
        double minD = 65.0; 
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                double d = Math.hypot(mx - getXCoord(c, r), my - getYCoord(c, r));
                if (d < minD) { minD = d; bestX = c; bestY = r; }
            }
        }
        return new int[] { bestX, bestY };
    }

    private void drawHexagon(GreenfootImage canvas, int cx, int cy) {
        int[] px = new int[6], py = new int[6];
        for (int i = 0; i < 6; i++) {
            double angle = Math.toRadians(60 * i - 90);
            px[i] = (int) (cx + HEX_R * Math.cos(angle));
            py[i] = (int) (cy + HEX_R * Math.sin(angle));
        }
        canvas.drawPolygon(px, py, 6);
    }

    private void fillHexagon(GreenfootImage canvas, int cx, int cy) {
        int[] px = new int[6], py = new int[6];
        for (int i = 0; i < 6; i++) {
            double angle = Math.toRadians(60 * i - 90);
            px[i] = (int) (cx + HEX_R * Math.cos(angle));
            py[i] = (int) (cy + HEX_R * Math.sin(angle));
        }
        canvas.fillPolygon(px, py, 6);
    }

    public int getXCoord(int col, int row) {
        double offset = (row % 2 == 1) ? HEX_W / 2.0 : 0;
        return (int) (ORIGIN_X + col * STEP_X + offset);
    }

    public int getYCoord(int col, int row) {
        return (int) (ORIGIN_Y + row * STEP_Y);
    }

    public int getMaxCapacity() {
        return playerLevel + 1 + bonusSlots;
    }

    public int getCurrentPlantCount() {
        int count = 0;
        for (int r = 0; r < 5; r++) // Chỉ đếm 5 hàng đầu
            for (int c = 0; c < COLS; c++)
                if (Board[r][c] != null) count++;
        return count;
    }

    public boolean canPlace(int x, int y, Plant plant) {
        if (x < 0 || x >= COLS || y < 0 || y >= ROWS) return false;
        Plant target = Board[y][x];
        
        if (target == null && y < 5) { 
            boolean alreadyInTop = false;
            for (int r = 0; r < 6; r++)
                for (int c = 0; c < COLS; c++)
                    if (Board[r][c] == plant) alreadyInTop = true;

            if (!alreadyInTop && getCurrentPlantCount() >= getMaxCapacity()) return false;
        }

        return target == null || target == plant;
    }

    @Override
    public boolean placePlant(int x, int y, Plant plant) {
        if (!canPlace(x, y, plant)) {
            returnBackToBench(plant);
            return false;
        }

        for (int r = 0; r < ROWS; r++)
            for (int c = 0; c < COLS; c++)
                if (Board[r][c] == plant) Board[r][c] = null;

        Board[y][x] = plant;
        plant.setGridPosition(x, y);
        int tx = getXCoord(x, y);
        int ty = getYCoord(x, y);

        if (plant.getWorld() == null) getWorld().addObject(plant, tx, ty);
        else plant.setLocation(tx, ty);

        if (getWorld() instanceof PlayScene) {
            PlantCombineHandler.checkAndCombine((PlayScene) getWorld(), plant);
        }
        return true;
    }

    private void returnBackToBench(Plant plant) {
        for (int r = 0; r < ROWS; r++)
            for (int c = 0; c < COLS; c++)
                if (Board[r][c] == plant) {
                    plant.setLocation(getXCoord(c, r), getYCoord(c, r));
                    return;
                }

        for (int c = 0; c < COLS; c++) {
            if (Board[5][c] == null) {
                Board[5][c] = plant;
                plant.setGridPosition(c, 5);
                int bx = getXCoord(c, 5), by = getYCoord(c, 5);
                if (plant.getWorld() == null) getWorld().addObject(plant, bx, by);
                else plant.setLocation(bx, by);
                return;
            }
        }
        
        if (plant.getWorld() != null) getWorld().removeObject(plant);
    }

    public void removePlant(int x, int y) {
        if (x >= 0 && x < COLS && y >= 0 && y < ROWS) {
            Board[y][x] = null;
        }
    }

    private Actor getDraggingActor() {
        if (getWorld() == null) return null;
        for (Plant p : getWorld().getObjects(Plant.class))
            if (p.isDragging) return p;
        for (SeedPacket s : getWorld().getObjects(SeedPacket.class)) {
            MouseInfo m = Greenfoot.getMouseInfo();
            if (m != null && Greenfoot.mouseDragged(s)) return s;
        }
        return null;
    }

    public int clampRow(int row) { return Math.max(0, Math.min(row, ROWS - 1)); }
    public int getGridX(int mx, int my) { return getGridPos(mx, my)[0]; }
    public int getGridY(int mx, int my) { return getGridPos(mx, my)[1]; }
}