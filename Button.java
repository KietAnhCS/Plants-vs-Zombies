// ═══════════════════════════════════════════════════════
// Button.java
// ═══════════════════════════════════════════════════════
import greenfoot.*;

public class Button extends SpriteAnimator {
    public GreenfootImage idle;
    public GreenfootImage hover;

    public Button(String idle, String hover) {
        this.idle  = new GreenfootImage(idle);
        this.hover = new GreenfootImage(hover);
        setImage(this.idle);
    }

    public void act() {
        handleHover();
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse != null && mouse.getButton() == 1 && Greenfoot.mouseClicked(this)) {
            AudioManager.playSound(80, false, "gravebutton.mp3");
            onClick();
        }
    }

    protected void handleHover() {
        World world = getWorld();
        if (world == null) return;

        boolean isHovered = false;
        if (world instanceof MainMenu) {
            isHovered = this.intersects(((MainMenu) world).hitbox);
        } else if (world instanceof ResultScreen) {
            isHovered = this.intersects(((ResultScreen) world).hitbox);
        } else {
            isHovered = Greenfoot.mouseMoved(this);
        }

        setImage(isHovered ? hover : idle);
    }

    protected void onClick() {}
}