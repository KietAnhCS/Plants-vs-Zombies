import greenfoot.*;

public class HealthBar extends Actor {
    private int width;
    private int height = 8; 
    private Actor owner;
    private double displayHp = -1; 
    private long lastDamageTime;
    private final int DELAY_MS = 750; 
    private int oldHp;
    private int lastDrawnHp = -1;
    private double lastDrawnDisplayHp = -1;

    public HealthBar(Actor owner, int width) {
        this.owner = owner;
        this.width = width;
    }

    public void act() {
        if (owner == null || owner.getWorld() == null || getWorld() == null) {
            if (getWorld() != null) getWorld().removeObject(this);
            return;
        }
        
        int xOffset = (owner instanceof BonkChoy) ? 45 : 0;
        setLocation(owner.getX() - xOffset, owner.getY() - 30);
        update();
    }

    public void update() {
        int hp = 0;
        int maxHp = 1;

        if (owner instanceof Plant) {
            Plant p = (Plant) owner;
            hp = p.getHp(); 
            maxHp = p.getMaxHp();
        } else if (owner instanceof Zombie) {
            Zombie z = (Zombie) owner;
            hp = z.getHp(); 
            maxHp = z.config.maxHp;
        } else {
            return;
        }

        if (maxHp <= 0) maxHp = 1;
        if (hp < 0) hp = 0;

        if (displayHp < 0) {
            displayHp = hp;
            oldHp = hp;
        }

        if (hp < oldHp) {
            lastDamageTime = System.currentTimeMillis();
            oldHp = hp;
        }

        if (System.currentTimeMillis() - lastDamageTime > DELAY_MS) {
            displayHp = hp;
            oldHp = hp;
        }

        if (hp == lastDrawnHp && displayHp == lastDrawnDisplayHp) {
            return;
        }

        lastDrawnHp = hp;
        lastDrawnDisplayHp = displayHp;

        GreenfootImage img = new GreenfootImage(width + 2, height + 2);
        
        img.setColor(new Color(30, 30, 30)); 
        img.fillRect(1, 1, width, height);

        int redWidth = (int) ((displayHp / maxHp) * width);
        if (redWidth > 0) {
            img.setColor(new Color(220, 30, 30)); 
            img.fillRect(1, 1, Math.min(redWidth, width), height);
        }

        int greenWidth = (int) (((double)hp / maxHp) * width);
        if (greenWidth > 0) {
            int drawGreen = Math.min(greenWidth, width);
            img.setColor(new Color(170, 220, 70)); 
            img.fillRect(1, 1, drawGreen, height);
            
            img.setColor(new Color(140, 190, 50));
            img.fillRect(1, height / 2, drawGreen, height / 2);
            
            img.setColor(new Color(110, 160, 40));
            img.fillRect(1, (height / 4) * 3, drawGreen, height / 4);
        }

        img.setColor(new Color(0, 0, 0, 160));
        for (int i = 1; i < 3; i++) {
            int segX = (width / 3) * i;
            img.drawLine(segX + 1, 1, segX + 1, height);
        }

        img.setColor(Color.BLACK);
        img.drawRect(0, 0, width + 1, height + 1);
        setImage(img);
    }
}