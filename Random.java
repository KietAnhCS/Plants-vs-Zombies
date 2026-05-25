public class Random {
    public static int Int(int min, int max) {
        return (int)((Math.random() * (max - min)) + min);
    }

    public static double Double(double min, double max) {
        return (Math.random() * (max - min)) + min;
    }
}