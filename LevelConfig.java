public class LevelConfig {
    public static final String[][][] LEVEL_1_DATA = {
        
        {{"PIANO"}, {"PIANO"}, {"RA"}, {"RA"}, null},
        {null, {"SUN"}, null, {"SUN"}, null},
        
        {{"NORMAL"}, null, {"SUN"}, null, {"NORMAL"}},      
        {null, {"NORMAL", "NORMAL"}, null, {"EXCAVATOR"}, {"EXCAVATOR"}},
        
        {{"CONEHEAD"}, {"EXCAVATOR"}, {"CONEHEAD"}, {"NORMAL"}, {"CONEHEAD"}},
        {{"NORMAL", "NORMAL"}, {"CONEHEAD"}, null, {"EXCAVATOR"}, {"NORMAL"}},
        {{"CONEHEAD"}, {"NUTCRACKER"}, {"CONEHEAD"}, {"CONEHEAD"}, {"CONEHEAD"}},
        {{"BUCKETHEAD"}, {"EXCAVATOR"}, {"BUCKETHEAD"}, {"EXCAVATOR"}, {"NUTCRACKER"}},
        {{"CONEHEAD", "CONEHEAD"}, {"BUCKETHEAD"}, {"CONEHEAD", "CONEHEAD"}, {"EXCAVATOR"}, {"CONEHEAD"}},
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