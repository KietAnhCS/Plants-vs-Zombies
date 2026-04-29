import greenfoot.GreenfootImage;

public class ZombieAppearance {
    private Zombie host;
    
    private GreenfootImage[] wNormal, wD1, wD2, wBare, wArmless;
    
    private GreenfootImage[] eNormal, eD1, eD2, eBare, eArmless;

    public ZombieAppearance(Zombie host, String type) {
        this.host = host;
        loadSprites(type);
    }

    private void loadSprites(String type) {
        wNormal = host.importSprites(type, 7);
        wD1 = host.importSprites(type + "d", 7);
        wD2 = host.importSprites(type + "dd", 7);
        wBare = host.importSprites("zombiewalk", 7);
        wArmless = host.importSprites("armlesszombie", 7);

        eNormal = host.importSprites(type + "eat", 7);
        eD1 = host.importSprites(type + "eatd", 7);
        eD2 = host.importSprites(type + "eatdd", 7);
        eBare = host.importSprites("zombieeating", 7);
        eArmless = host.importSprites("armlesszombieeating", 7);
    }

    public GreenfootImage[] getCurrentWalk() {
        if (host.hp <= 100) return wArmless;
        if (host.hp <= 500) return wBare;
        if (host.hp <= 1000) return wD2;
        if (host.hp <= 1500) return wD1;
        return wNormal;
    }

    public GreenfootImage[] getCurrentEat() {
        if (host.hp <= 100) return eArmless;
        if (host.hp <= 500) return eBare;
        if (host.hp <= 1000) return eD2;
        if (host.hp <= 1500) return eD1;
        return eNormal;
    }
}