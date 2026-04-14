import greenfoot.*;

public class Lilypad extends Plant
{
    private GreenfootImage[] idle;
    // Độ lệch thẩm mỹ để lá sen trông như nằm dưới mặt nước
    public static final int Y_OFFSET = 30; 

    public Lilypad() {
        maxHp = 100; 
        hp = maxHp;
        idle = importSprites("Lilypad", 1); 
        setImage(idle[0]);
    }
   
    @Override
    public void hit(int dmg) {
        // PVZ Logic: Chỉ nhận sát thương khi không có cây nào che chở bên trên
        if (!isOccupied() && isLiving()) {
            hitFlash(idle, "Lilypad");
            hp = hp - dmg;
        }
    }

    @Override
    public void update() {
        if (getWorld() == null) return;
        MyWorld = (MyWorld)getWorld();

        // Đảm bảo Lilypad luôn vẽ dưới cây khác tại cùng một ô
        ensureLayering();
        
        animate(idle, 200, true);

        if (hp <= 0) {
            removePlantOnTop();
            getWorld().removeObject(this);
        }
    }

    private void ensureLayering() {
        // Có thể dùng logic z-order hoặc đơn giản là vẽ lại nếu cần
    }

    public Plant getPlantOnTop() {
        // Vì Lilypad bị hạ thấp, getOneIntersectingObject vẫn hoạt động tốt 
        // do khung va chạm (bounding box) vẫn chồng lấn
        Plant p = (Plant) getOneIntersectingObject(Plant.class);
        if (p != null && !(p instanceof Lilypad)) {
            return p;
        }
        return null;
    }

    public boolean isOccupied() {
        return getPlantOnTop() != null;
    }

    private void removePlantOnTop() {
        Plant p = getPlantOnTop();
        if (p != null) {
            getWorld().removeObject(p);
        }
    }
}