import greenfoot.*;  

public class Preloader extends World
{

    /**
     * Constructor for objects of class First.
     * 
     */
    public Preloader()
    {    
        super(1111, 602, 1, false); 
        setBackground(new GreenfootImage("transition.png"));
        addObject(new Transition(true, new PopCap(),10),288, 215);
        setPaintOrder(EndTransition.class, Transition.class);
    }
    public void act() {
        
    }
}
