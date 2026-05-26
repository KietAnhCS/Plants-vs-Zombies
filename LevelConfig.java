public class LevelConfig {
    public static final String[][][] LEVEL_1_DATA = {
        
        {null, null, {"SUN"}, null, {"SUN"}},
        {{"SUN", "SUN"},{"SUN", "SUN"}, null, {"NORMAL", "NORMAL"}, null},
        
        {{"PIANO","NORMAL"}, null, {"EXCAVATOR","NORMAL","NORMAL"}, null, {"NORMAL", "NORMAL","NORMAL"}},      
        {null, {"BRICKHEAD", "CONEHEAD"}, null, {"EXCAVATOR", "CONEHEAD", "CONEHEAD"}, {"EXCAVATOR", "CONEHEAD","BRICKHEAD"}},
        
        {{"PIANO","NORMAL"}, null, {"EXCAVATOR","BUCKETHEAD","NORMAL"}, null, {"NORMAL", "NORMAL","BUCKETHEAD"}},
        
    
        {   {"NORMAL", "PIANO","NORMAL","NORMAL","NORMAL","NORMAL"},
            {"EXCAVATOR", "NORMAL","NORMAL","NORMAL","NORMAL","NORMAL"}, 
            {"NORMAL", "NORMAL","NORMAL","NORMAL","NORMAL","NORMAL"}, 
            {"NORMAL", "PIANO","NORMAL","NORMAL","NORMAL","NORMAL"}, 
            {"NORMAL", "NORMAL","NORMAL","PIANO","NORMAL","NORMAL"}
        },
        
    };
}