public class LevelConfig {
    public static final String[][][] LEVEL_1_DATA = {
        
        {{"SUN"}, null, {"SUN"}, null, null},
        {null, {"SUN"}, null, {"SUN"}, null},
        
        {{"NORMAL"}, null, {"SUN"}, null, {"NORMAL"}},      
        {null, {"NORMAL", "NORMAL"}, null, {"EXCAVATOR"}, null},
        
        {{"CONEHEAD"}, {"EXCAVATOR"}, {"CONEHEAD"}, {"NORMAL"}, {"CONEHEAD"}},
        {{"NORMAL", "NORMAL"}, {"CONEHEAD"}, null, {"CEXCAVATOR"}, {"NORMAL"}},
        {{"CONEHEAD"}, {"NUTCRACKER"}, {"CONEHEAD"}, {"CONEHEAD"}, {"CONEHEAD"}},
        {{"BUCKETHEAD"}, {"CONEHEAD"}, {"BUCKETHEAD"}, {"EXCAVATOR"}, {"NUTCRACKER"}},
        {{"CONEHEAD", "CONEHEAD"}, {"BUCKETHEAD"}, {"CONEHEAD", "CONEHEAD"}, {"BUCKETHEAD"}, {"CONEHEAD"}},
        {{"EXCAVATOR"}, {"BUCKETHEAD"}, {"EXCAVATOR"}, {"BUCKETHEAD"}, {"BUCKETHEAD"}},
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