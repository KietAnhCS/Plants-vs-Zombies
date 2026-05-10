import greenfoot.*;

public class Retry extends Button {
    private final World restartWorld;

    public Retry(World restartWorld) {
        super("retry1.png", "retry2.png");
        this.restartWorld = restartWorld;
    }

    @Override
    protected void onClick() {
        Greenfoot.setWorld(restartWorld);
    }
}