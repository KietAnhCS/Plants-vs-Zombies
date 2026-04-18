
public class Random  
{
    private int x;

    public Random()
    {
    }

    public static int Int(int min, int max) {
        return (int)((Math.random() * (max - min)) + min);
    }
    public static double Double(int min, int max) {
        return (((Math.random() * (max - min)) + min)/100);
    }
}
