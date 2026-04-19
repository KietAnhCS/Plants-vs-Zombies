import greenfoot.*;

public class LevelNode extends Actor
{
    private int level;
    private LevelSelect world;
    private boolean hovered = false;

    private GreenfootImage normalImg;
    private GreenfootImage hoverImg;

    private static final int W_NORMAL = 150;
    private static final int H_NORMAL = 190;
    private static final int W_HOVER  = 165;
    private static final int H_HOVER  = 208;

    private static final String[] IMAGES = {
            "packetwatermap.png",   // Level 1
            "packetnormalmap.png",  // Level 2
            "packetnormalmap.png"   // Level 3
    };

    public LevelNode(int level, LevelSelect world) {
        this.level = level;
        this.world = world;

        String imgFile = IMAGES[level - 1];

        normalImg = new GreenfootImage(imgFile);
        normalImg.scale(W_NORMAL, H_NORMAL);

        hoverImg = new GreenfootImage(imgFile);
        hoverImg.scale(W_HOVER, H_HOVER);

        setImage(normalImg);
    }

    public void act() {
        boolean nowHovered = this.intersects(world.hitbox);

        if (nowHovered != hovered) {
            hovered = nowHovered;
            setImage(hovered ? hoverImg : normalImg);
        }

        if (hovered && Greenfoot.mouseClicked(this)) {
            AudioPlayer.play(100, "gravebutton.mp3");
            world.startLevel(level);
        }
    }
}