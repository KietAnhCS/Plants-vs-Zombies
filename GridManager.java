import greenfoot.*;
import java.util.*;

public class GridManager extends Actor implements IPlantPlacer {
    public Plant[][] Board = new Plant[6][9];
    public int currentRowCount = 6;
    public int playerLevel = 1;

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
        img.clear();
        setImage(img);
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
        String status = "Plants: " + getCurrentPlantCount() + "/" + getMaxCapacity();
        canvas.setColor(Color.WHITE);
        canvas.drawString(status, 10, 20);
    }

    private void drawFullGrid(GreenfootImage canvas) {
        canvas.setColor(new Color(100, 180, 255, 60));
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                int cx = getXCoord(c, r);
                int cy = getYCoord(c, r);
                drawHexagon(canvas, cx, cy);
            }
        }
    }

    private void drawGridHighlights(GreenfootImage canvas, Actor dragging) {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse == null) return;
        int gx = getGridX(mouse.getX(), mouse.getY());
        int gy = getGridY(mouse.getX(), mouse.getY());
        if (gx < 0 || gy < 0) return;

        Plant plant = dragging instanceof Plant ? (Plant) dragging
                    : dragging instanceof SeedPacket ? ((SeedPacket) dragging).getPlant()
                    : null;
        if (plant == null) return;

        boolean can = canPlace(gx, gy, plant);
        int cx = getXCoord(gx, gy);
        int cy = getYCoord(gx, gy);

        canvas.setColor(new Color(100, 180, 255, 100));
        fillHexagon(canvas, cx, cy);
        canvas.setColor(new Color(0, 0, 200));
        drawHexagon(canvas, cx, cy);
        
        int circleR = (int)(HEX_R * 0.6); 
        canvas.setColor(new Color(255, 255, 255, 80));
        canvas.fillOval(cx - circleR, cy - circleR, circleR * 2, circleR * 2);

        if (!can) {
            canvas.setColor(new Color(255, 0, 0, 100));
            fillHexagon(canvas, cx, cy);
        }
    }

    private void drawHexagon(GreenfootImage canvas, int cx, int cy) {
        int[] px = new int[6];
        int[] py = new int[6];
        for (int i = 0; i < 6; i++) {
            double angle = Math.toRadians(60 * i - 90);
            px[i] = (int)(cx + HEX_R * Math.cos(angle));
            py[i] = (int)(cy + HEX_R * Math.sin(angle));
        }
        canvas.drawPolygon(px, py, 6);
    }

    private void fillHexagon(GreenfootImage canvas, int cx, int cy) {
        int[] px = new int[6];
        int[] py = new int[6];
        for (int i = 0; i < 6; i++) {
            double angle = Math.toRadians(60 * i - 90);
            px[i] = (int)(cx + HEX_R * Math.cos(angle));
            py[i] = (int)(cy + HEX_R * Math.sin(angle));
        }
        canvas.fillPolygon(px, py, 6);
    }

    public int getXCoord(int col, int row) {
        double offset = (row % 2 == 1) ? HEX_W / 2.0 : 0;
        return (int)(ORIGIN_X + col * STEP_X + offset);
    }

    public int getYCoord(int col, int row) {
        return (int)(ORIGIN_Y + row * STEP_Y);
    }

    public int getGridX(int mx, int my) {
        int best = -1;
        double minD = HEX_R * 1.1;
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                double d = Math.hypot(mx - getXCoord(c, r), my - getYCoord(c, r));
                if (d < minD) { minD = d; best = c; }
            }
        }
        return best;
    }

    public int getGridY(int mx, int my) {
        int best = -1;
        double minD = HEX_R * 1.1;
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                double d = Math.hypot(mx - getXCoord(c, r), my - getYCoord(c, r));
                if (d < minD) { minD = d; best = r; }
            }
        }
        return best;
    }

    public int getMaxCapacity() {
        return playerLevel + 3;
    }

    public int getCurrentPlantCount() {
        int count = 0;
        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < COLS; c++) {
                if (Board[r][c] != null) count++;
            }
        }
        return count;
    }

    public boolean canPlace(int x, int y, Plant plant) {
        if (x < 0 || x >= COLS || y < 0 || y >= ROWS) return false;
        Plant target = Board[y][x];
        if (y < 5) {
            boolean isAlreadyInTopFive = false;
            for (int r = 0; r < 5; r++) {
                for (int c = 0; c < COLS; c++) {
                    if (Board[r][c] == plant) isAlreadyInTopFive = true;
                }
            }
            if (target == null && !isAlreadyInTopFive) {
                if (getCurrentPlantCount() >= getMaxCapacity()) return false;
            }
        }
        if (target == null || target == plant) return true;
        return UpgradeManager.canUpgrade(plant, target);
    }

    @Override
    public boolean placePlant(int x, int y, Plant plant) {
        if (!canPlace(x, y, plant)) {
            returnBackToBench(plant);
            return false;
        }
        int tx = getXCoord(x, y);
        int ty = getYCoord(x, y);
        Plant target = Board[y][x];
        int oldR = -1, oldC = -1;
        for (int r = 0; r < ROWS; r++)
            for (int c = 0; c < COLS; c++)
                if (Board[r][c] == plant) { oldR = r; oldC = c; }

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
            returnBackToBench(plant);
            return false;
        }

        if (oldR >= 0) Board[oldR][oldC] = null;
        Board[y][x] = plant;
        if (plant.getWorld() == null) getWorld().addObject(plant, tx, ty);
        else plant.setLocation(tx, ty);
        if (getWorld() instanceof PlayScene) ((PlayScene) getWorld()).checkAndCombine(plant);
        return true;
    }

    private void returnBackToBench(Plant plant) {
        for (int r = 0; r < ROWS; r++)
            for (int c = 0; c < COLS; c++)
                if (Board[r][c] == plant) return;
        List<Integer> emptyCols = new ArrayList<>();
        for (int c = 0; c < COLS; c++)
            if (Board[5][c] == null) emptyCols.add(c);
        if (!emptyCols.isEmpty()) {
            int randomCol = emptyCols.get(Greenfoot.getRandomNumber(emptyCols.size()));
            int bx = getXCoord(randomCol, 5);
            int by = getYCoord(randomCol, 5);
            if (plant.getWorld() == null) getWorld().addObject(plant, bx, by);
            else plant.setLocation(bx, by);
            Board[5][randomCol] = plant;
        } else {
            if (plant.getWorld() != null) getWorld().removeObject(plant);
        }
    }

    public void removePlant(int x, int y) {
        if (x >= 0 && x < COLS && y >= 0 && y < ROWS) {
            Plant p = Board[y][x];
            if (p != null) {
                if (p.getWorld() != null) getWorld().removeObject(p);
                Board[y][x] = null;
            }
        }
    }

    private Actor getDraggingActor() {
        for (Plant p : getWorld().getObjects(Plant.class))
            if (p.getImage().getTransparency() < 255) return p;
        for (SeedPacket s : getWorld().getObjects(SeedPacket.class))
            if (Greenfoot.mouseDragged(s) || Greenfoot.mousePressed(s)) return s;
        return null;
    }

    public int clampRow(int row) {
        return Math.max(0, Math.min(row, ROWS - 1));
    }
}