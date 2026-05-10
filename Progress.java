import greenfoot.*;

public class Progress extends Actor {
    private WaveManager waveMgr;
    private GreenfootImage baseImage;
    private final int BAR_WIDTH = 428;
    private final int BAR_HEIGHT = 14;
    private final int X_OFFSET = 14;
    private final int Y_OFFSET = 14;

    public Progress(WaveManager waveMgr) {
        this.waveMgr = waveMgr;
        this.baseImage = new GreenfootImage("progress.png"); 
        setImage(new GreenfootImage(baseImage));
    }

    @Override
    public void addedToWorld(World world) {
        if (waveMgr == null || waveMgr.levelData == null) return;
        
        int totalWaves = waveMgr.levelData.length;
        for (int waveIdx : waveMgr.hugeWaves) {
            int flagX;
            if (waveIdx >= totalWaves - 1) {
                flagX = getX() - (BAR_WIDTH / 2) + 15;
            } else {
                double ratio = (double) (waveIdx + 1) / totalWaves;
                flagX = getX() + (BAR_WIDTH / 2) - (int)(ratio * BAR_WIDTH);
            }
            world.addObject(new Flag(), flagX, getY());
        }
    }

    @Override
    public void act() {
        if (waveMgr == null || waveMgr.wave == -1) return;
        updateBar();
    }

    private void updateBar() {
        GreenfootImage currentImg = new GreenfootImage(baseImage);
        int totalWaves = waveMgr.levelData.length;
        
        double progress = (double) (waveMgr.wave + 1) / totalWaves;
        int fillWidth = (int) (BAR_WIDTH * progress);
        
        if (fillWidth > 0) {
            int startX = BAR_WIDTH - fillWidth;

            drawGradientRect(currentImg, startX, fillWidth);
        }
        
        setImage(currentImg);
    }

    private void drawGradientRect(GreenfootImage img, int startX, int width) {
        int[] colorsY = {0, 5, 7, 9, 11};
        Color[] colors = {
            new Color(240, 240, 128), 
            new Color(192, 224, 96),
            new Color(152, 200, 80),
            new Color(128, 184, 64),
            new Color(80, 160, 32)    
        };

        for (int i = 0; i < colors.length; i++) {
            img.setColor(colors[i]);
            img.fillRect(X_OFFSET + startX, Y_OFFSET + colorsY[i], width, BAR_HEIGHT - colorsY[i]);
        }
    }
}