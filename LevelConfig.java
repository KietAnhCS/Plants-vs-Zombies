public class LevelConfig {
    public static final String[][][] LEVEL_1_DATA = {
        {
            {"EXCAVATOR"},
            null,          
            {"EXCAVATOR"},  
            null,          
            null           
        },
        
        {
            null,
            {"EXCAVATOR", "EXCAVATOR"}, 
            null,
            null,
            null
        },
        
        {
            null,
            null,
            null,
            {"NUTCRACKER", "NUTCRACKER"},
            null
        },

        // Wave 3: Heavy mix for final testing
        {
            {"NUTCRACKER"},
            {"NUTCRACKER"},
            {"NUTCRACKER"},
            {"NUTCRACKER"},
            {"NUTCRACKER"}
        }
        
    };
}