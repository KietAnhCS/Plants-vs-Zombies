public enum PlantType {

    PEASHOOTER    (PlantDamageType.NORMAL,    1120,  60,  1500L, 0),
    REPEATER      (PlantDamageType.NORMAL,    1120,  60,  1700L, 0),
    GATLING_PEA   (PlantDamageType.NORMAL,    1120,  80,   800L, 7),
    GATLING_PEA_2 (PlantDamageType.NORMAL,    1120, 100,  2000L, 5),

    CACTUS        (PlantDamageType.NEEDLE,      10, 100,  2400L, 0),
    CACTUS_2      (PlantDamageType.NEEDLE,      12,  60,  1500L, 0),
    CACTUS_3      (PlantDamageType.NEEDLE,      12,  60,  1500L, 0),

    BONK_CHOY     (PlantDamageType.NORMAL,       3, 636,   100L, 0),
    BONK_CHOY_2   (PlantDamageType.NORMAL,       5, 1000,  200L, 0),
    BONK_CHOY_3   (PlantDamageType.NORMAL,      25, 1200,  200L, 0),

    POTATO_MINE   (PlantDamageType.EXPLOSIVE,    0, 100,  3000L, 0),
    CHERRY_BOMB   (PlantDamageType.EXPLOSIVE,    0, 100,     0L, 0),

    SNOW_PEA      (PlantDamageType.NORMAL,    1120,  60,  1500L, 0),
    SUNFLOWER     (PlantDamageType.NORMAL,       0, 100,     0L, 0),
    WALL_NUT      (PlantDamageType.NORMAL,       0, 400,     0L, 0);

    public final PlantDamageType damageType;
    public final int damage;
    public final int hp;
    public final long shootDelay;
    public final int burstCount;

    PlantType(PlantDamageType damageType, int damage, int hp,
              long shootDelay, int burstCount) {
        this.damageType = damageType;
        this.damage     = damage;
        this.hp         = hp;
        this.shootDelay = shootDelay;
        this.burstCount = burstCount;
    }
}