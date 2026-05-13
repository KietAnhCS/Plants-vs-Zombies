import greenfoot.*;

public class ZombieConfig {
    public final int maxHp;
    public final int damage;
    public final double walkSpeed;
    public final int[] thresholds;
    public final ZombieAssets[] walkKeys;
    public final ZombieAssets[] eatKeys;
    public final String name;
    
    public final String deadAnimation;
    public final int deadFrames;

    public ZombieConfig(int maxHp, int damage, double walkSpeed, String name,
                        int[] thresholds, ZombieAssets[] walkKeys, ZombieAssets[] eatKeys,
                        String deadAnimation, int deadFrames) {
        this.maxHp      = maxHp;
        this.damage     = damage;
        this.walkSpeed  = walkSpeed;
        this.name       = name;
        this.thresholds = thresholds;
        this.walkKeys   = walkKeys;
        this.eatKeys    = eatKeys;
        this.deadAnimation = deadAnimation;
        this.deadFrames    = deadFrames;
    }
    public static final ZombieConfig SUN = new ZombieConfig(
        ZombieRegistry.SUN_HP, ZombieRegistry.SUN_DAMAGE,
        ZombieRegistry.SUN_SPEED, "Sun",new int[0],new ZombieAssets[0],new ZombieAssets[0],  
    "zombies/basic/die/die", 
    10
    );
    
    
    public static final ZombieConfig BASIC = new ZombieConfig(
        ZombieRegistry.BASIC_HP, ZombieRegistry.BASIC_DAMAGE,
        ZombieRegistry.BASIC_SPEED, "Basic",
        new int[] { ZombieRegistry.BASIC_ARMLESS },
        new ZombieAssets[] { ZombieAssets.BASIC_WALK, ZombieAssets.BASIC_WALK_ARMLESS },
        new ZombieAssets[] { ZombieAssets.BASIC_EAT,  ZombieAssets.BASIC_EAT_ARMLESS },
        "zombies/basic/die/die", 10
    );

    public static final ZombieConfig CONE = new ZombieConfig(
        ZombieRegistry.CONE_HP, ZombieRegistry.CONE_DAMAGE,
        ZombieRegistry.CONE_SPEED, "Cone",
        new int[] { ZombieRegistry.CONE_D1, ZombieRegistry.CONE_D2,
                    ZombieRegistry.CONE_BARE, ZombieRegistry.CONE_ARMLESS },
        new ZombieAssets[] { ZombieAssets.CONE_WALK,  ZombieAssets.CONE_WALK_D1,
                             ZombieAssets.CONE_WALK_D2, ZombieAssets.SHARED_WALK_BARE,
                             ZombieAssets.SHARED_WALK_ARMLESS },
        new ZombieAssets[] { ZombieAssets.CONE_EAT,   ZombieAssets.CONE_EAT_D1,
                             ZombieAssets.CONE_EAT_D2, ZombieAssets.SHARED_EAT_BARE,
                             ZombieAssets.SHARED_EAT_ARMLESS },
        "zombies/basic/die/die", 10
    );

    public static final ZombieConfig BUCKET = new ZombieConfig(
        ZombieRegistry.BUCKET_HP, ZombieRegistry.BUCKET_DAMAGE,
        ZombieRegistry.BUCKET_SPEED, "Bucket",
        new int[] { ZombieRegistry.BUCKET_D1, ZombieRegistry.BUCKET_D2,
                    ZombieRegistry.BUCKET_BARE, ZombieRegistry.BUCKET_ARMLESS },
        new ZombieAssets[] { ZombieAssets.BUCKET_WALK,  ZombieAssets.BUCKET_WALK_D1,
                             ZombieAssets.BUCKET_WALK_D2, ZombieAssets.SHARED_WALK_BARE,
                             ZombieAssets.SHARED_WALK_ARMLESS },
        new ZombieAssets[] { ZombieAssets.BUCKET_EAT,   ZombieAssets.BUCKET_EAT_D1,
                             ZombieAssets.BUCKET_EAT_D2, ZombieAssets.SHARED_EAT_BARE,
                             ZombieAssets.SHARED_EAT_ARMLESS },
        "zombies/basic/die/die", 10
    );

    public static final ZombieConfig BRICK = new ZombieConfig(
        ZombieRegistry.BRICK_HP, ZombieRegistry.BRICK_DAMAGE,
        ZombieRegistry.BRICK_SPEED, "Brick",
        new int[] { ZombieRegistry.BRICK_D1, ZombieRegistry.BRICK_D2,
                    ZombieRegistry.BRICK_BARE, ZombieRegistry.BRICK_ARMLESS },
        new ZombieAssets[] { ZombieAssets.BRICK_WALK,  ZombieAssets.BRICK_WALK_D1,
                             ZombieAssets.BRICK_WALK_D2, ZombieAssets.SHARED_WALK_BARE,
                             ZombieAssets.SHARED_WALK_ARMLESS },
        new ZombieAssets[] { ZombieAssets.BRICK_EAT,   ZombieAssets.BRICK_EAT_D1,
                             ZombieAssets.BRICK_EAT_D2, ZombieAssets.SHARED_EAT_BARE,
                             ZombieAssets.SHARED_EAT_ARMLESS },
        "zombies/basic/die/die", 10
    );

    public static final ZombieConfig PIANO = new ZombieConfig(
        ZombieRegistry.PIANO_HP, ZombieRegistry.PIANO_DAMAGE,
        ZombieRegistry.PIANO_SPEED, "Piano",
        new int[] { ZombieRegistry.PIANO_ARMLESS },
        new ZombieAssets[] { ZombieAssets.PIANO_WALK, ZombieAssets.SHARED_WALK_ARMLESS },
        new ZombieAssets[] { ZombieAssets.SHARED_EAT_BARE, ZombieAssets.SHARED_EAT_ARMLESS },
        "zombies/basic/die/die", 10
    );

    public static final ZombieConfig RA = new ZombieConfig(
        ZombieRegistry.RA_HP, ZombieRegistry.RA_DAMAGE,
        ZombieRegistry.RA_SPEED, "Ra",
        new int[] { ZombieRegistry.RA_BARE, ZombieRegistry.RA_ARMLESS },
        new ZombieAssets[] { ZombieAssets.RA_WALK, ZombieAssets.SHARED_WALK_BARE,
                             ZombieAssets.SHARED_WALK_ARMLESS },
        new ZombieAssets[] { ZombieAssets.RA_EAT,  ZombieAssets.SHARED_EAT_BARE,
                             ZombieAssets.SHARED_EAT_ARMLESS },
        "zombies/basic/die/die", 10
    );
}