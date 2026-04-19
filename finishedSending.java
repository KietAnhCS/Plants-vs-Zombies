import greenfoot.*;

class finishedSending extends Actor
{
    public long deltaTime;
    public long lastFrame = System.nanoTime();
    public long currentFrame;
    public long delayTime;
    public WaveManager level;
    
    finishedSending(WaveManager level, long delayTime) {
        this.delayTime = delayTime;
        this.level = level;
        setImage(new GreenfootImage(1, 1)); 
    }

    public void act() {
        if (level != null && level.choosingCard) {
            lastFrame = System.nanoTime();
            return;
        }

        currentFrame = System.nanoTime();
        deltaTime = (currentFrame - lastFrame) / 1000000;

        if (deltaTime > delayTime) {
            if (level != null) {
                level.finishedSending = true;
            }
            if (getWorld() != null) {
                getWorld().removeObject(this);
            }
        }
    }
}