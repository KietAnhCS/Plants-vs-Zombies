import greenfoot.*;

public class ZombieFactory implements IZombieFactory {

    @Override
    public Zombie createZombie(ZombieType type) {
        if (type == null) return new BasicZombie();
        
        switch (type) {
            case SUN:        return new SunZombie();
            case CONEHEAD:   return new Conehead();
            case BUCKETHEAD: return new Buckethead();
            case BRICKHEAD:  return new Brickhead();
            case PIANO:      return new PianoZombie();
            case RA:         return new RaZombie();
            case NUTCRACKER: return new NutcrackerZombie();
            case EXCAVATOR:  return new ExcavatorZombie();
            default:         return new BasicZombie();
        }
    }

    @Override
    public Zombie createZombie(String typeName) {
        if (typeName == null || typeName.isEmpty()) {
            return new BasicZombie();
        }

        String name = typeName.trim()
                              .replaceAll("[\"{}]", "")
                              .toUpperCase();

        if (name.contains("NUTCRACKER")) return new NutcrackerZombie();
        if (name.contains("RA"))         return new RaZombie();
        if (name.contains("PIANO"))      return new PianoZombie();
        if (name.contains("CONE"))       return new Conehead();
        if (name.contains("BUCKET"))     return new Buckethead();
        if (name.contains("BRICK"))      return new Brickhead();
        if (name.contains("EXCAVATOR"))  return new ExcavatorZombie();
        if (name.contains("SUN"))        return new SunZombie();

        return new BasicZombie();
    }
}