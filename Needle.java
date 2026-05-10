import greenfoot.*;

public class Needle extends Projectile {

    public Needle(int yPos) {
        super("needle", 1, yPos, PlantType.CACTUS.damage, 8);
    }
}