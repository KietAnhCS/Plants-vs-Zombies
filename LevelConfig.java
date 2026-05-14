public class LevelConfig {
    public static final String[][][] LEVEL_1_DATA = {
        
        {{"SUN"}, null, {"SUN"}, null, null},
        {null, {"SUN"}, null, {"SUN"}, null},
        
        {{"NORMAL"}, null, {"SUN"}, null, {"NORMAL"}},      
        {null, {"NORMAL", "NORMAL"}, null, {"EXCAVATOR"}, null},
        
        {{"CONEHEAD"}, {"NORMAL"}, {"CONEHEAD"}, {"NORMAL"}, {"CONEHEAD"}},
        {{"NORMAL", "NORMAL"}, {"CONEHEAD"}, null, {"CONEHEAD"}, {"NORMAL"}},
        {{"CONEHEAD"}, {"CONEHEAD"}, {"CONEHEAD"}, {"CONEHEAD"}, {"CONEHEAD"}},
        {{"BUCKETHEAD"}, {"CONEHEAD"}, {"BUCKETHEAD"}, {"CONEHEAD"}, {"BUCKETHEAD"}},
        {{"CONEHEAD", "CONEHEAD"}, {"BUCKETHEAD"}, {"CONEHEAD", "CONEHEAD"}, {"BUCKETHEAD"}, {"CONEHEAD"}},
        {{"BUCKETHEAD"}, {"BUCKETHEAD"}, {"NORMAL"}, {"BUCKETHEAD"}, {"BUCKETHEAD"}},
        {{"BRICKHEAD"}, {"BUCKETHEAD"}, {"BUCKETHEAD"}, {"BUCKETHEAD"}, {"BRICKHEAD"}},
        {
            {"BRICKHEAD"}, 
            {"BRICKHEAD", "BUCKETHEAD", "BRICKHEAD", "BUCKETHEAD", "BRICKHEAD", "BUCKETHEAD", "BRICKHEAD"}, 
            null, 
            {"BUCKETHEAD", "BUCKETHEAD", "BRICKHEAD", "BRICKHEAD", "BUCKETHEAD", "BUCKETHEAD", "BRICKHEAD"}, 
            {"BRICKHEAD"}
        }
        
    };
}