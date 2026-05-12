import greenfoot.*;

public class ZombieDeathHandler implements IDeathHandler {
    private IAudioService audio;
    private ZombieEventBus eventBus;

    public ZombieDeathHandler(IAudioService audio, ZombieEventBus eventBus) {
        this.audio = audio;
        this.eventBus = eventBus;
    }

    @Override
    public void handleDeath(Zombie zombie) {
        if (audio != null) {
            audio.playSound(80, false, ZombieAssets.SHARED_HEADLESS_EAT.path);
        }
        if (eventBus != null) {
            eventBus.publishDeath(zombie);
        }
        World world = zombie.getWorld();
        if (world != null) {
            world.addObject(new Head(), zombie.getX(), zombie.getY());
            world.addObject(new fallingZombie(zombie.getDeadSprites()), zombie.getX(), zombie.getY());
        }
        zombie.setState(new DeadState(zombie));
    }
}