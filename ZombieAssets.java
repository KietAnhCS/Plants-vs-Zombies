import greenfoot.*;

public enum ZombieAssets {
    BASIC_WALK("zombiewalk", 7),
    BASIC_WALK_ARMLESS("armlesszombie", 7),
    BASIC_EAT("zombieeating", 7),
    BASIC_EAT_ARMLESS("armlesszombieeating", 7),

    CONE_WALK("coneheadwalk", 7),
    CONE_WALK_D1("coneheadwalkd", 7),
    CONE_WALK_D2("coneheadwalkdd", 7),
    CONE_EAT("coneheadeat", 7),
    CONE_EAT_D1("coneheadeatd", 7),
    CONE_EAT_D2("coneheadeatdd", 7),

    BUCKET_WALK("bucketheadwalk", 7),
    BUCKET_WALK_D1("bucketheadwalkd", 7),
    BUCKET_WALK_D2("bucketheadwalkdd", 7),
    BUCKET_EAT("bucketheadeat", 7),
    BUCKET_EAT_D1("bucketheadeatd", 7),
    BUCKET_EAT_D2("bucketheadeatdd", 7),

    BRICK_WALK("brickhead", 7),
    BRICK_WALK_D1("brickheadd", 7),
    BRICK_WALK_D2("brickheaddd", 7),
    BRICK_EAT("brickheadeat", 7),
    BRICK_EAT_D1("brickheadeatd", 7),
    BRICK_EAT_D2("brickheadeatdd", 7),

    SHARED_WALK_BARE("zombiewalk", 7),
    SHARED_WALK_ARMLESS("armlesszombie", 7),
    SHARED_EAT_BARE("zombieeating", 7),
    SHARED_EAT_ARMLESS("armlesszombieeating", 7),
    SHARED_HEADLESS("zombieheadless", 7),
    SHARED_HEADLESS_EAT("headlesszombieeating", 7),
    SHARED_FALL("zombiefall", 7),
    
    PIANO_WALK("Piano_Playing", 15),
    PIANO_WALK_D1("Piano-Damaged", 15),
    PIANO_WALK_D2("Piano_Playing_Damaged", 15),
    PIANO_DEATH("Piano_Death", 15),
    
    RA_WALK("Ra_Idle", 10),
    RA_EAT("Ra_Eating", 7),
    RA_POWER_START("Ra_Power-Start", 10),
    RA_POWER_LOOP("Ra_Power-Loop", 10),
    RA_POWER_END("Ra_Power-End", 10),
    RA_DEATH("Ra_death", 12);

    public final String path;
    public final int count;
    private static final SpriteCache cache = new SpriteCache();

    ZombieAssets(String path, int count) {
        this.path = path;
        this.count = count;
    }

    public GreenfootImage[] getImages() {
        return cache.getSprites(this.path, this.count);
    }
}