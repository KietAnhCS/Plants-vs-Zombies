public class LevelConfig {
    public static final String[][][] LEVEL_1_DATA = {
        // Wave 0: One Nutcracker in Row 0, One Excavator in Row 2
        {
            {"NUTCRACKER"}, // Row 0
            null,           // Row 1
            {"NUTCRACKER"},  // Row 2
            null,           // Row 3
            null            // Row 4
        },
        
        // Wave 1: Two Nutcrackers in Row 1
        {
            null,
            {"NUTCRACKER", "NUTCRACKER"}, 
            null,
            null,
            null
        },
        
        // Wave 2: Two Excavators in Row 3
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