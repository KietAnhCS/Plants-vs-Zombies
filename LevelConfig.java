public class LevelConfig {
    public static final String[][][] LEVEL_1_DATA = {
        
        {{"EXCAVATOR"}, null, null, {"PIANO"}, null},
        {{"SUN"}, {"SUN"}, {"SUN"}, {"SUN"}, {"SUN"}},
        
        {{"NORMAL"}, null, {"EXCAVATOR"}, null, {"NORMAL"}},      
        {null, {"NORMAL", "NORMAL"}, null, {"EXCAVATOR"}, {"EXCAVATOR"}},
        
        {{"CONEHEAD"}, {"EXCAVATOR"}, {"CONEHEAD"}, {"NORMAL"}, {"CONEHEAD"}},
        {{"NORMAL", "NORMAL"}, {"NUTCRACKER"}, null, {"EXCAVATOR"}, {"NORMAL"}},
        {{"CONEHEAD"}, {"NUTCRACKER"}, {"CONEHEAD"}, {"CONEHEAD"}, {"CONEHEAD"}},
        {{"BUCKETHEAD"}, {"EXCAVATOR"}, {"BUCKETHEAD"}, {"EXCAVATOR"}, {"NUTCRACKER"}},
        {{"CONEHEAD", "CONEHEAD"}, {"BUCKETHEAD"}, {"CONEHEAD", "CONEHEAD"}, {"EXCAVATOR"}, {"CONEHEAD"}},
        {{"BUCKETHEAD"}, {"PIANO"}, {"BUCKETHEAD"}, {"BUCKETHEAD"}, {"BUCKETHEAD"}},
        {{"BRICKHEAD"}, {"EXCAVATOR"}, {"BUCKETHEAD"}, {"BUCKETHEAD"}, {"BRICKHEAD"}},
        {
            {"EXCAVATOR"}, 
            {"BRICKHEAD", "BUCKETHEAD", "BRICKHEAD", "BUCKETHEAD", "BRICKHEAD", "BUCKETHEAD", "NUTCRACKER"}, 
            null, 
            {"BUCKETHEAD", "BUCKETHEAD", "BRICKHEAD", "BRICKHEAD", "BUCKETHEAD", "BUCKETHEAD", "BRICKHEAD"}, 
            {"EXCAVATOR"}
        }
        
    };
}