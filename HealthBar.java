import greenfoot.*;

public class HealthBar extends Actor {
    private int width;
    private int height = 7;
    private Actor owner;

    public HealthBar(Actor owner, int width) {
        this.owner = owner;
        this.width = width;
    }

    public void act() {
        if (owner == null || owner.getWorld() == null || getWorld() == null) {
            if (getWorld() != null) {
                getWorld().removeObject(this);
            }
            return;
        }

        int targetX = owner.getX();
        int targetY = owner.getY() - 60;

        if (owner instanceof BonkChoy) {
            targetX -= 45;
        }

        setLocation(targetX, targetY);
        update();
    }

    public void update() {
        if (owner == null || owner.getWorld() == null) return;

        int hp = 0;
        int maxHp = 1;

        if (owner instanceof Plant) {
            Plant p = (Plant) owner;
            hp = p.hp;
            maxHp = p.maxHp;
        } else if (owner instanceof Zombie) {
            Zombie z = (Zombie) owner;
            hp = z.hp;
            maxHp = z.maxHp;
        } else {
            return;
        }

        if (maxHp <= 0) maxHp = 1;
        if (hp < 0) hp = 0;

        GreenfootImage img = new GreenfootImage(width + 2, height + 2);
        img.setColor(Color.BLACK);
        img.drawRect(0, 0, width + 1, height + 1);
        img.setColor(Color.RED);
        img.fillRect(1, 1, width, height);
        
        double ratio = (double) hp / maxHp;
        int healthWidth = (int) (ratio * width);
        
        if (healthWidth > width) healthWidth = width;
        if (healthWidth < 0) healthWidth = 0;

        img.setColor(Color.GREEN);
        img.fillRect(1, 1, healthWidth, height);

        setImage(img);
    }
}