import greenfoot.*;

public enum PlantType {

    PEASHOOTER    (100, PlantDamageType.NORMAL,    35,   200,   1500L, 0),
    REPEATER      (125, PlantDamageType.NORMAL,    50,   200,   1700L, 2),
    GATLING_PEA   (300, PlantDamageType.NORMAL,    50,   200,   800L,  7),
    GATLING_PEA_2 (400, PlantDamageType.FIRE,      50,   200,   2000L, 5),
    CACTUS        (125, PlantDamageType.NEEDLE,    30,   200,   2400L, 0),
    CACTUS_2      (250, PlantDamageType.NEEDLE,    30,   200,   1500L, 0),
    CACTUS_3      (375, PlantDamageType.NEEDLE,    30,   200,   1500L, 0),
    BONK_CHOY     (175, PlantDamageType.NORMAL,    15,    300,   300L,  0),
    BONK_CHOY_2   (400, PlantDamageType.NORMAL,    15,   500,   200L,  0),
    BONK_CHOY_3   (600, PlantDamageType.NORMAL,    30,   900,   150L,  0);

    public final int cost;
    public final PlantDamageType damageType;
    public final int damage;
    public final int hp;
    public final long shootDelay;
    public final int burstCount;

    PlantType(int cost, PlantDamageType damageType, int damage, int hp,
              long shootDelay, int burstCount) {
        this.cost = cost;
        this.damageType = damageType;
        this.damage = damage;
        this.hp = hp;
        this.shootDelay = shootDelay;
        this.burstCount = burstCount;
    }
}