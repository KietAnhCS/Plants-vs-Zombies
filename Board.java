import greenfoot.*; 

public class Board extends Actor
{
    public Plant[][] Board = new Plant[5][9];
    public static final int xOffset = 290;
    public static final int yOffset = 135;
    public static final int xSpacing = 82;
    public static final int ySpacing =100;
    
    public Board() {
    
    }
    
    public void placePlant(int x, int y, Plant plant)
    {
        if (y >= 0 && y < 5 && x >= 0 && x < 9) {
            if (Board[y][x] == null ) {
            Board[y][x] =plant;
            
            
            getWorld().addObject(plant, x*xSpacing+xOffset, y*ySpacing+yOffset);
            AudioPlayer.play(80, "plant.mp3", "plant2.mp3");
            }
        }
    }
    public Plant getPlant(int x, int y) {
        return Board[y][x];
    }
    public void removePlant(int x, int y) {
        if (Board[y][x] !=null) {
            getWorld().removeObject(Board[y][x]);
            Board[y][x] = null;
        }
        AudioPlayer.play(80,"plant2.mp3");
    }
    public void updateBoard(){
        for (int i=0; i< Board.length;i++) {
            for (int k=0; k<Board[0].length;k++ ) {
                if (Board[i][k] !=null) {
                    World MyWorld = getWorld();
                    Plant temp = Board[i][k];
                    MyWorld.addObject(temp, k*xSpacing+xOffset, i*ySpacing+yOffset);
                    
                }
            }
        }
    }
    
    public void act(){
    
    }
}
