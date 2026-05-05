public class ZombieConfig {
    public final int maxHp;
    public final int damage;
    public final int[] thresholds;
    public final SpriteKey[] walkKeys;
    public final SpriteKey[] eatKeys;

    public ZombieConfig(int maxHp, int damage, int[] thresholds,
                        SpriteKey[] walkKeys, SpriteKey[] eatKeys) {
        this.maxHp      = maxHp;
        this.damage     = damage;
        this.thresholds = thresholds;
        this.walkKeys   = walkKeys;
        this.eatKeys    = eatKeys;
    }

    public static final ZombieConfig BASIC = new ZombieConfig(
        ZombieRegistry.BASIC_HP, ZombieRegistry.BASIC_DAMAGE,
        new int[] { ZombieRegistry.BASIC_ARMLESS },
        new SpriteKey[] { SpriteKey.BASIC_WALK,     SpriteKey.BASIC_WALK_ARMLESS },
        new SpriteKey[] { SpriteKey.BASIC_EAT,      SpriteKey.BASIC_EAT_ARMLESS  }
    );

    public static final ZombieConfig CONE = new ZombieConfig(
        ZombieRegistry.CONE_HP, ZombieRegistry.CONE_DAMAGE,
        new int[] { ZombieRegistry.CONE_D1, ZombieRegistry.CONE_D2,
                    ZombieRegistry.CONE_BARE, ZombieRegistry.CONE_ARMLESS },
        new SpriteKey[] { SpriteKey.CONE_WALK,    SpriteKey.CONE_WALK_D1,
                          SpriteKey.CONE_WALK_D2, SpriteKey.SHARED_WALK_BARE,
                          SpriteKey.SHARED_WALK_ARMLESS },
        new SpriteKey[] { SpriteKey.CONE_EAT,     SpriteKey.CONE_EAT_D1,
                          SpriteKey.CONE_EAT_D2,  SpriteKey.SHARED_EAT_BARE,
                          SpriteKey.SHARED_EAT_ARMLESS }
    );

    public static final ZombieConfig BUCKET = new ZombieConfig(
        ZombieRegistry.BUCKET_HP, ZombieRegistry.BUCKET_DAMAGE,
        new int[] { ZombieRegistry.BUCKET_D1, ZombieRegistry.BUCKET_D2,
                    ZombieRegistry.BUCKET_BARE, ZombieRegistry.BUCKET_ARMLESS },
        new SpriteKey[] { SpriteKey.BUCKET_WALK,    SpriteKey.BUCKET_WALK_D1,
                          SpriteKey.BUCKET_WALK_D2, SpriteKey.SHARED_WALK_BARE,
                          SpriteKey.SHARED_WALK_ARMLESS },
        new SpriteKey[] { SpriteKey.BUCKET_EAT,     SpriteKey.BUCKET_EAT_D1,
                          SpriteKey.BUCKET_EAT_D2,  SpriteKey.SHARED_EAT_BARE,
                          SpriteKey.SHARED_EAT_ARMLESS }
    );

    public static final ZombieConfig BRICK = new ZombieConfig(
        ZombieRegistry.BRICK_HP, ZombieRegistry.BRICK_DAMAGE,
        new int[] { ZombieRegistry.BRICK_D1, ZombieRegistry.BRICK_D2,
                    ZombieRegistry.BRICK_BARE, ZombieRegistry.BRICK_ARMLESS },
        new SpriteKey[] { SpriteKey.BRICK_WALK,    SpriteKey.BRICK_WALK_D1,
                          SpriteKey.BRICK_WALK_D2, SpriteKey.SHARED_WALK_BARE,
                          SpriteKey.SHARED_WALK_ARMLESS },
        new SpriteKey[] { SpriteKey.BRICK_EAT,     SpriteKey.BRICK_EAT_D1,
                          SpriteKey.BRICK_EAT_D2,  SpriteKey.SHARED_EAT_BARE,
                          SpriteKey.SHARED_EAT_ARMLESS }
    );
    
    public static final ZombieConfig PIANO = new ZombieConfig(
        ZombieRegistry.PIANO_HP, ZombieRegistry.PIANO_DAMAGE,
        new int[] { ZombieRegistry.PIANO_ARMLESS },
        new SpriteKey[] { SpriteKey.PIANO_WALK,     SpriteKey.SHARED_WALK_ARMLESS },
        new SpriteKey[] { SpriteKey.SHARED_EAT_BARE, SpriteKey.SHARED_EAT_ARMLESS }
    );
}