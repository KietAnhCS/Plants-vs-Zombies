import greenfoot.*;

public class LevelSelect extends World
{
    Hitbox hitbox = new Hitbox();
    public GreenfootSound music;

    public LevelSelect(GreenfootSound music)
    {
        super(803, 602, 1, false);

        this.music = music;

        GreenfootImage bg = new GreenfootImage("levelmap.png");
        bg.scale(803, 602);
        setBackground(bg);

        addObject(hitbox, 0, 0);

        addObject(new LevelNode(1, this), 180, 300);
        addObject(new LevelNode(2, this), 400, 300);
        addObject(new LevelNode(3, this), 620, 300);

        Greenfoot.setSpeed(50);
    }

    public void act() {
        moveHitbox();
    }

    public void moveHitbox() {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse != null) {
            hitbox.setLocation(mouse.getX(), mouse.getY());
        }
    }

    public void startLevel(int level) {
    if (music != null) music.stop();
    switch (level) {
        case 1:
            addObject(new Transition(false, new Level1Preview(), 5), getWidth()/2, getHeight()/2);
            break;
        case 2:
            addObject(new Transition(false, new Level2Preview(), 5), getWidth()/2, getHeight()/2);
            break;
        case 3:
            addObject(new Transition(false, new Level3Preview(), 5), getWidth()/2, getHeight()/2);
            break;
    }
}

    public void started() {
        if (music != null && !music.isPlaying()) {
            music.setVolume(60);
            music.playLoop();
        }
    }

    public void stopped() {
        if (music != null) music.pause();
    }
}
