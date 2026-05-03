import greenfoot.*;  

public class Preloader extends World
{
    public Preloader()
    {    
        super(1111, 705, 1, false); 
        setBackground(new GreenfootImage("transition.png"));
        
        addObject(new Transition(true, new PopCap(), 10), 555, 301); 
        
        setPaintOrder(EndTransition.class, Transition.class);
    }
}