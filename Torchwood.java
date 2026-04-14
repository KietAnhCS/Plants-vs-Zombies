import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Torchwood: Biến Pea thường thành FirePea khi đi xuyên qua.
 */
public class Torchwood extends Plant
{
    private GreenfootImage[] idle;
    
    public Torchwood() {
        idle = importSprites("firewood", 5);
        maxHp = 100; // Torchwood thường trâu hơn Sunflower một chút
        hp = maxHp;
    }

    /**
     * Update được gọi liên tục từ lớp cha hoặc phương thức act
     */
    public void update() {
        animate(idle, 200, true);
        checkAndUpgradePea(); // Logic biến đổi đạn
    }

        /**
         * Kiểm tra xem có viên đạn thường nào chạm vào không
         */
        private void checkAndUpgradePea() {
        
        Pea p = (Pea) getOneIntersectingObject(Pea.class);
        
        if (p != null && p.getWorld() != null) {
            int curX = p.getX();
            int curY = p.getY();
            int rowIdx = p.getYPos(); 
            
            getWorld().removeObject(p);
            
            
            FirePea fp = new FirePea(rowIdx); 
            getWorld().addObject(fp, curX, curY);
        }
    }

    public void hit(int dmg) {
        if (isLiving()) {
            
            hitFlash(idle, "firewood");
            hp = hp - dmg;
            
            if (hp <= 0) {
                getWorld().removeObject(this);
            }
        }
    }
}