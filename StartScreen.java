import greenfoot.*;

public class StartScreen extends World
{
    
    
    public StartScreen()
    {    
        super(960, 640, 1); 
        prepare();
    }
    
    
    
    private void prepare()
    {  
        ClickToStart start = new ClickToStart();
        addObject(start, 500, 534);   
    }
}