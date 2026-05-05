public enum SpriteKey {
    BASIC_WALK("zombiewalk"),
    BASIC_WALK_ARMLESS("armlesszombie"),
    BASIC_EAT("zombieeating"),
    BASIC_EAT_ARMLESS("armlesszombieeating"),

    CONE_WALK("coneheadwalk"),
    CONE_WALK_D1("coneheadwalkd"),
    CONE_WALK_D2("coneheadwalkdd"),
    CONE_EAT("coneheadeat"),
    CONE_EAT_D1("coneheadeatd"),
    CONE_EAT_D2("coneheadeatdd"),

    BUCKET_WALK("bucketheadwalk"),
    BUCKET_WALK_D1("bucketheadwalkd"),
    BUCKET_WALK_D2("bucketheadwalkdd"),
    BUCKET_EAT("bucketheadeat"),
    BUCKET_EAT_D1("bucketheadeatd"),
    BUCKET_EAT_D2("bucketheadeatdd"),

    BRICK_WALK("brickhead"),
    BRICK_WALK_D1("brickheadd"),
    BRICK_WALK_D2("brickheaddd"),
    BRICK_EAT("brickheadeat"),
    BRICK_EAT_D1("brickheadeatd"),
    BRICK_EAT_D2("brickheadeatdd"),

    SHARED_WALK_BARE("zombiewalk"),
    SHARED_WALK_ARMLESS("armlesszombie"),
    SHARED_EAT_BARE("zombieeating"),
    SHARED_EAT_ARMLESS("armlesszombieeating"),
    SHARED_HEADLESS("zombieheadless"),
    SHARED_HEADLESS_EAT("headlesszombieeating"),
    SHARED_FALL("zombiefall"),
    
    PIANO_WALK("Piano_Playing");

    public final String path;

    SpriteKey(String path) {
        this.path = path;
    }
}