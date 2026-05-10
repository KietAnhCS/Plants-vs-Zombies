public class LevelConfig {
    public static final String[][][] LEVEL_1_DATA = {
        {{"NORMAL"}, null, null, null, null},
        {null, {"NORMAL"}, null, {"NORMAL"}, null},
        
        {{"NORMAL"}, null, {"NORMAL"}, null, {"NORMAL"}},      
        {null, {"NORMAL", "NORMAL"}, null, {"NORMAL"}, null},
        
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