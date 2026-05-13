import greenfoot.*;

public class ExcavatorZombie extends Zombie {
    private GreenfootImage[] wNormal, shovel, chop, death;
    private long lastShovelTime = 0;

    public ExcavatorZombie() {
        super(new ZombieConfig(
            ZombieRegistry.EXCAVATOR_HP, ZombieRegistry.EXCAVATOR_DAMAGE, 
            ZombieRegistry.EXCAVATOR_SPEED, "Excavator",
            new int[]{}, null, null, null, 0
        ));
        
        wNormal = importSprites(ZombieAssets.EXCAVATOR_WALK.path, 20);
        shovel  = importSprites(ZombieAssets.EXCAVATOR_SHOVEL.path, 25);
        chop    = importSprites(ZombieAssets.EXCAVATOR_CHOP.path, 20);
        death   = importSprites(ZombieAssets.EXCAVATOR_DEATH.path, 20);

        if (wNormal != null && wNormal.length > 0) setImage(wNormal[0]);

        
        setState(new WalkingState(this));
    }

    @Override
    public void act() {
        if (isLiving() && currentState instanceof WalkingState) {
            if (checkEating()) {
                long now = System.currentTimeMillis();
                if (now - lastShovelTime > ZombieRegistry.EXCAVATOR_COOLDOWN) {
                    setState(new ExcavatorShovelState(this));
                    lastShovelTime = now;
                    return;
                } else {
                    setState(new EatingState(this));
                }
            }
        }
        super.act();
    }

    @Override
    public GreenfootImage[] getCurrentAnimation(boolean isEating) {
        return isEating ? chop : wNormal;
    }
    
    public GreenfootImage[] getShovelSprites() { return shovel; }
    
    @Override
    protected void handleThresholds() {}
}