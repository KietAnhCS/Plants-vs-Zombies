public interface IEatable {
    boolean canBeEatenBy(Zombie zombie);
    void hit(int damage);
}