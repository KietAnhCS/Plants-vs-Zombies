import greenfoot.*;
import java.util.List;

public class PlantStateManager {
    private Plant plant;

    public PlantStateManager(Plant plant) {
        this.plant = plant;
    }

    public void update() {
        if (plant.getWorld() == null) return;

        // Xử lý logic chết: Bắt buộc chuyển sang DYING nếu hết máu
        if (plant.getHp() <= 0 && plant.getState() != PlantState.DYING) {
            plant.setState(PlantState.DYING);
        }
    }

    public boolean isBusy() {
        PlantState s = plant.getState();
        // Cây đang bận (không thể bắn/hành động) nếu đang được kéo, ghép hoặc đang chết
        return s == PlantState.DRAGGING || 
               s == PlantState.MERGING || 
               s == PlantState.DYING;
    }

    // Hàm quyết định xem hàm update() của PotatoMine có được chạy hay không
    public boolean canAct() {
        // 1. Kiểm tra an toàn cơ bản
        if (plant.getWorld() == null || plant.getHp() <= 0) return false;
        
        // 2. Nếu cây đang bận (kéo, ghép, chết) -> không hoạt động
        if (isBusy()) return false;

        PlayScene scene = (PlayScene) plant.getWorld();
        
        // 3. Kiểm tra Overlay (Cửa sổ UI, Menu...). Chỉ chặn khi thực sự có Overlay tồn tại.
        List<Overlay> overlays = scene.getObjects(Overlay.class);
        if (overlays != null && !overlays.isEmpty()) {
            return false;
        }

        // 4. Kiểm tra xem cây có nằm trên hàng chờ không (Giả sử Y = 5 là hàng chờ)
        // NẾU BẠN VẪN LỖI SAU KHI SỬA, HÃY THỬ COMMENT DÒNG DƯỚI ĐÂY LẠI
        if (plant.getYPos() == 5) {
            return false;
        }

        // Vượt qua tất cả -> Cho phép cây chạy logic riêng của nó!
        return true; 
    }
}