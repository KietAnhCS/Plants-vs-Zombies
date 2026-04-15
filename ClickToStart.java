import greenfoot.*;

public class ClickToStart extends Actor
{
    private GifImage gif = new GifImage("click_to_start.gif");

    public void act() 
    {
        setImage(gif.getCurrentImage());
        
        if(Greenfoot.mouseClicked(this))
        {
            // 1. Dừng nhạc ở StartScreen trước khi đi
            StartScreen world = (StartScreen)getWorld();
            
            
            // 2. Chuyển thẳng sang Preloader
            // Không dùng addObject ở đây vì Preloader là một World, không phải Actor
            Greenfoot.setWorld(new Preloader());
        }
    }    
}