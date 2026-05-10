public interface IZombieEventListener {
    void onZombieDied(Zombie zombie);
    void onZombieHit(Zombie zombie, int damage);
    void onZombieAteTarget(Zombie zombie, IEatable target);
    void onZombieStateChanged(Zombie zombie, IZombieState newState);
}