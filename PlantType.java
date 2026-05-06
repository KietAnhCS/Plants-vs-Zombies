import greenfoot.*;

public enum PlantType {

    PEASHOOTER    (100, PlantDamageType.NORMAL,    10,  200,   1500L, 0),
    REPEATER      (125, PlantDamageType.NORMAL,    30,    200,   1700L, 0),
    GATLING_PEA   (300, PlantDamageType.NORMAL,    60,    200,   800L,  7),
    GATLING_PEA_2 (400, PlantDamageType.FIRE,      80,    200,  2000L, 5),
    CACTUS        (125, PlantDamageType.NEEDLE,    12,    200,  2400L, 0),
    CACTUS_2      (250, PlantDamageType.NEEDLE,    25,    200,   1500L, 0),
    CACTUS_3      (375, PlantDamageType.NEEDLE,    40,    200,   1500L, 0),
    BONK_CHOY     (175, PlantDamageType.NORMAL,    20,    400,  100L,  0),
    BONK_CHOY_2   (400, PlantDamageType.NORMAL,    40,    650, 200L,  0),
    BONK_CHOY_3   (600, PlantDamageType.NORMAL,    60,    800, 200L,  0),
    POTATO_MINE   (25,  PlantDamageType.EXPLOSIVE, 150,   100000,  10000L, 0);

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