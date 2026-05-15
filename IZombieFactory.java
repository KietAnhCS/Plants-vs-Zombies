import greenfoot.*;

public interface IZombieFactory {
    Zombie createZombie(ZombieType type);
    Zombie createZombie(String typeName);
}