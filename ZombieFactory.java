import greenfoot.*;

public class ZombieFactory {

    public static Zombie createZombie(ZombieType type) {
        if (type == null) return new BasicZombie();
        switch (type) {
            case SUN: return new SunZombie();
            case CONEHEAD: return new Conehead();
            case BUCKETHEAD: return new Buckethead();
            case BRICKHEAD: return new Brickhead();
            case PIANO: return new PianoZombie();
            case RA: return new RaZombie();
            case NUTCRACKER: return new NutcrackerZombie();
            case EXCAVATOR: return new ExcavatorZombie();
            default: return new BasicZombie();
        }
    }

    public static Zombie createZombie(String typeName) {
        if (typeName == null) return new BasicZombie();
        
        String name = typeName.trim()
                               .replace("\"", "")
                               .replace("{", "")
                               .replace("}", "")
                               .toUpperCase();
        
        if (name.equals("NUTCRACKER") || name.contains("NUTCRACKER")) {
            return new NutcrackerZombie();
        } else if (name.equals("RA") || name.contains("RA")) {
            return new RaZombie(); 
        } else if (name.equals("PIANO") || name.contains("PIANO")) {
            return new PianoZombie();
        } else if (name.equals("CONEHEAD") || name.contains("CONE")) {
            return new Conehead();
        } else if (name.equals("BUCKETHEAD") || name.contains("BUCKET")) {
            return new Buckethead();
        } else if (name.equals("BRICKHEAD") || name.contains("BRICK")) {
            return new Brickhead();
        } else if (name.contains("EXCAVATOR")) {
            return new ExcavatorZombie();
        }
        else if (name.contains("SUN")) {
            return new SunZombie();
        }

        return new BasicZombie();
    }
}