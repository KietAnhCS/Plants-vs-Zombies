import greenfoot.*;
public interface IEntitySpawner {
    
    void spawn();
    
    void setEnabled(boolean enabled);
    
    boolean isEnabled();
    
    void resetSpawner();
}