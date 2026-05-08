public interface IPlantEventListener
{
    void onPlantDied(Plant plant);
    
    void onPlantHit(Plant plant, int damage);
    
    void onPlantMerged(Plant source, Plant target);
    
    void onPlantStateChanged(Plant plant, PlantState newState);
}