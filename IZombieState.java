import greenfoot.GreenfootImage;

public interface IZombieState 
{
    void enter();
    void update();
    void exit();
    GreenfootImage[] getAnimation();
}