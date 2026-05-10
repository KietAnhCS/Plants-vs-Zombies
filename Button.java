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
            onClick();
        }
    }

    protected void handleHover() {
        World world = getWorld();
        if (world == null) return;

        boolean isHovered = false;
        MouseInfo mouse = Greenfoot.getMouseInfo();
        
        if (mouse != null) {
            int mouseX = mouse.getX();
            int mouseY = mouse.getY();
            
            int width = getImage().getWidth();
            int height = getImage().getHeight();
            
            if (mouseX >= getX() - width / 2 && mouseX <= getX() + width / 2 && 
                mouseY >= getY() - height / 2 && mouseY <= getY() + height / 2) {
                isHovered = true; 
            }
        }

        setImage(isHovered ? hover : idle);
    }

    protected void onClick() {}
}