import greenfoot.*;

/**
 * PrepCountdown — a compact HUD actor that displays a labelled countdown timer.
 *
 * Used by WaveManager to show:
 *   • "PREP PHASE  5" before each wave launches.
 *   • "NEXT WAVE IN 5" after a wave is cleared and the reward sun has appeared.
 *
 * WaveManager calls setSeconds(n) every act() tick so the display stays current.
 * The actor removes itself when WaveManager calls removeObject on it.
 */
public class PrepCountdown extends Actor {

    private static final int W = 200;
    private static final int H = 60;

    private final String label;
    private int seconds;

    public PrepCountdown(String label, int initialSeconds) {
        this.label   = label;
        this.seconds = initialSeconds;
        redraw();
    }

    /** Called every act by WaveManager to keep the number current. */
    public void setSeconds(int s) {
        if (s != seconds) {
            seconds = s;
            redraw();
        }
    }

    @Override
    public void act() {
        // Rendering is fully driven by WaveManager; nothing to do here.
    }

    // ─────────────────────────────────────────────────────────────────────────
    private void redraw() {
        GreenfootImage img = new GreenfootImage(W, H);

        // ── background pill ──────────────────────────────────────────────────
        img.setColor(new Color(0, 0, 0, 170));
        fillRoundRect(img, 0, 0, W, H, 16);

        // ── thin accent border ───────────────────────────────────────────────
        img.setColor(new Color(255, 210, 50, 220));   // gold tint
        drawRoundRect(img, 1, 1, W - 2, H - 2, 14);

        // ── label ────────────────────────────────────────────────────────────
        img.setFont(new Font("Courier New", true, false, 13));
        img.setColor(new Color(200, 200, 200));
        img.drawString(label, 12, 20);

        // ── big countdown number ─────────────────────────────────────────────
        img.setFont(new Font("Courier New", true, false, 30));
        // shadow
        img.setColor(new Color(0, 0, 0, 180));
        img.drawString(String.valueOf(seconds), W - 52, H - 11);
        // main – colour shifts to red when 2 seconds or fewer remain
        img.setColor(seconds <= 2 ? new Color(255, 80, 80) : new Color(255, 215, 0));
        img.drawString(String.valueOf(seconds), W - 54, H - 13);

        setImage(img);
    }
    
    private void fillRoundRect(GreenfootImage img, int x, int y, int w, int h, int r) {
        img.fillRect(x + r, y,     w - 2 * r, h);
        img.fillRect(x,     y + r, r,         h - 2 * r);
        img.fillRect(x + w - r, y + r, r,     h - 2 * r);
        img.fillOval(x,             y,             2 * r, 2 * r);
        img.fillOval(x + w - 2 * r, y,             2 * r, 2 * r);
        img.fillOval(x,             y + h - 2 * r, 2 * r, 2 * r);
        img.fillOval(x + w - 2 * r, y + h - 2 * r, 2 * r, 2 * r);
    }

    private void drawRoundRect(GreenfootImage img, int x, int y, int w, int h, int r) {
        img.drawLine(x + r, y,         x + w - r, y);
        img.drawLine(x + r, y + h,     x + w - r, y + h);
        img.drawLine(x,     y + r,     x,         y + h - r);
        img.drawLine(x + w, y + r,     x + w,     y + h - r);
        img.drawOval(x,             y,             2 * r, 2 * r);
        img.drawOval(x + w - 2 * r, y,             2 * r, 2 * r);
        img.drawOval(x,             y + h - 2 * r, 2 * r, 2 * r);
        img.drawOval(x + w - 2 * r, y + h - 2 * r, 2 * r, 2 * r);
    }
}
