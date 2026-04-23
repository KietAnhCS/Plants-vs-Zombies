import greenfoot.*;

public class HealthBar extends Actor {
    private int width = 50;
    private int height = 7;
    private Actor owner;

    public HealthBar(Actor owner, int width) {
        this.owner = owner;
        this.width = width;
        update();
    }

    public void act() {
        if (owner == null || owner.getWorld() == null) {
            getWorld().removeObject(this);
            return;
        }
        
        setLocation(owner.getX(), owner.getY() - owner.getImage().getHeight()/2 - 10);
        update();
    }

    public void update() {
        int hp = 0;
        int maxHp = 1;

        if (owner instanceof Plant) {
            hp = ((Plant) owner).hp;
            maxHp = ((Plant) owner).maxHp;
        } else if (owner instanceof Zombie) {
            hp = ((Zombie) owner).hp;
            maxHp = ((Zombie) owner).maxHp;
        }

        if (hp < 0) hp = 0;

        GreenfootImage img = new GreenfootImage(width + 2, height + 2);
        
        img.setColor(Color.BLACK);
        img.drawRect(0, 0, width + 1, height + 1);
        
        img.setColor(Color.RED);
        img.fillRect(1, 1, width, height);
        
        int healthWidth = (int)((double)hp / maxHp * width);
        img.setColor(Color.GREEN);
        img.fillRect(1, 1, healthWidth, height);

        setImage(img);
    }
}