public class SunManager {
    private int sun = 50;

    public boolean hasEnough(int cost) {
        return sun >= cost;
    }

    public void add(int amount) {
        sun += amount;
    }

    public void spend(int cost) {
        sun -= cost;
    }

    public int getSun() {
        return sun;
    }
}