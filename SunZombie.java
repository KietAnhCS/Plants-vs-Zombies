import greenfoot.*;

public class SunZombie extends Zombie {
    public GreenfootImage[] wNormal, eNormal;
    private boolean sunSpawned = false;

    public SunZombie() {
        super(ZombieConfig.SUN);
        this.walkSpeed = (Greenfoot.getRandomNumber(6) + 25) / 100.0;
        
        wNormal = importSprites(ZombieAssets.SUN_WALK.path, ZombieAssets.SUN_WALK.count, 0.45);
        eNormal = importSprites(ZombieAssets.SUN_WALK.path, ZombieAssets.SUN_WALK.count, 0.45);
        
        setState(new WalkingState(this));
    }

    private void spawnSuns() {
        if (sunSpawned || getWorld() == null) return;
        sunSpawned = true;
    
        int count = 1; 
        for (int i = 0; i < count; i++) {
            SecretSun s = new SecretSun(); 
            getWorld().addObject(s, getX(), getY());
        }
        
        AudioManager.getInstance().playSound(80, false, "achievement.mp3");
}

    @Override
    public void hit(int dmg) {
        super.hit(dmg);
        if (getHp() <= 0 && !sunSpawned) {
            spawnSuns();
        }

        if (isLiving() || !finalDeath) {
            AudioManager.getInstance().playSound(80, false, "splat.mp3");
        }
    }

    @Override
    protected void handleThresholds() {}

    @Override
    public GreenfootImage[] getCurrentAnimation(boolean isEating) {
        return isEating ? eNormal : wNormal;
    }
}