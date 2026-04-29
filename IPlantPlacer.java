/** Abstraction để DragController không phụ thuộc trực tiếp vào GridManager */
public interface IPlantPlacer {
    int  getGridX(int worldX, int worldY);
    int  getGridY(int worldX, int worldY);
    boolean canPlace(int gx, int gy, Plant plant);
    boolean placePlant(int gx, int gy, Plant plant);
    int  getXCoord(int gx, int gy);
    int  getYCoord(int gx, int gy);
}