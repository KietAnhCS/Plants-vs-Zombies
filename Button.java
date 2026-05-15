import greenfoot.*;

public abstract class Button extends SpriteAnimator {
    protected GreenfootImage idleImage;
    protected GreenfootImage hoverImage;

    public Button(String idlePath, String hoverPath) {
        this.idleImage = new GreenfootImage(idlePath);
        this.hoverImage = new GreenfootImage(hoverPath);
        setImage(idleImage);
    }

    @Override
    public void update() {
        handleHover();
        checkClick();
    }

    protected void handleHover() {
        if (getWorld() == null) return;

        boolean isHovered = false;
        MouseInfo mouse = Greenfoot.getMouseInfo();
        
        if (mouse != null) {
            int mouseX = mouse.getX();
            int mouseY = mouse.getY();
            
            int width = getImage().getWidth();
            int height = getImage().getHeight();
            
            // Giữ nguyên logic tính toán tọa độ thủ công từ code cũ của bạn
            if (mouseX >= getX() - width / 2 && mouseX <= getX() + width / 2 && 
                mouseY >= getY() - height / 2 && mouseY <= getY() + height / 2) {
                isHovered = true; 
            }
        }

        // Cập nhật image dựa trên trạng thái hover
        setImage(isHovered ? hoverImage : idleImage);
    }

    private void checkClick() {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse != null && mouse.getButton() == 1 && Greenfoot.mouseClicked(this)) {
            onClick();
        }
    }

    protected abstract void onClick();
}