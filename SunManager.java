public class SunManager {
    private int sun = 10000;

    public boolean hasEnough(int cost) {
        return sun >= cost;
    }

    public void add(int amount) {
        sun += amount;
    }

    public void spend(int cost) {
        if (hasEnough(cost)) {
            sun -= cost;
        }
    }

    public int getSun() {
        return sun;
    }
}