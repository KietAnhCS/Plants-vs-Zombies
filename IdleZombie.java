import greenfoot.*;

public class IdleZombie extends SpriteAnimator
{
    public IdleZombie() {
    }

    @Override
    public void addedToWorld(World world) {

        frame = Random.Int(1, 4);
    }

    @Override
    public void update() {
    }
}