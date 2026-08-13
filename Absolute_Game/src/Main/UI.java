package Main;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class UI {
    GameFrame gf;

    // HUD assets
    BufferedImage pointIcon;
    BufferedImage heartIcon;

    // Fonts
    private final Font titleFont;
    private final Font subtitleFont;
    private final Font hudFont;
    private final Font buttonFont;
    private final Font scoreFont;
    private final Font effectFont;  // active-effect HUD labels

    // Colors — flat, no glow, no gradient
    private static final Color BG         = new Color(30, 32, 34);   // solid canvas
    private static final Color GREEN_TEXT  = new Color(80, 200, 100); // titles
    private static final Color BTN_SURFACE = new Color(38, 42, 46);   // button background
    private static final Color BTN_BORDER  = new Color(80, 200, 100); // button border
    private static final Color DIVIDER     = new Color(55, 60, 65);   // neutral rule

    // Button rectangles — used for click detection
    private Rectangle endlessModeBtn;
    private Rectangle mainMenuBtn;

    // Fading notification for instant LuckyBox effects (drawn bottom-right)
    private volatile String notification;
    private volatile Color notificationColor;
    private volatile long notificationStart;
    private static final long NOTIF_HOLD_MS = 1200; // full-opacity duration
    private static final long NOTIF_FADE_MS = 1800; // slow fade-out duration

    public UI(GameFrame gf) {
        this.gf = gf;

        // Derive button bounds from screen size
        int btnW = 240;
        int btnH = 55;
        int centerX = gf.screenWidth / 2 - btnW / 2;

        endlessModeBtn = new Rectangle(centerX, gf.screenHeight / 2 - 10, btnW, btnH);
        mainMenuBtn = new Rectangle(centerX, gf.screenHeight / 2 + 50, btnW, btnH);

        // Fonts — BitcountPropSingle is the main font everywhere.
        // Falls back to SansSerif if the resource cannot be found.
        Font base;
        try (InputStream is = getClass().getResourceAsStream("/Font/BitcountPropSingle.ttf")) {
            if (is != null) {
                base = Font.createFont(Font.TRUETYPE_FONT, is);
                GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(base);
            } else {
                System.err.println("[UI] Font resource not found, falling back to SansSerif.");
                base = new Font("SansSerif", Font.PLAIN, 12);
            }
        } catch (FontFormatException | IOException e) {
            System.err.println("[UI] Failed to load custom font: " + e.getMessage());
            base = new Font("SansSerif", Font.PLAIN, 12);
        }

        titleFont   = base.deriveFont(Font.BOLD, 62f);
        subtitleFont = base.deriveFont(Font.PLAIN, 16f);
        hudFont     = base.deriveFont(Font.BOLD, 30f);
        buttonFont  = base.deriveFont(Font.BOLD, 20f);
        scoreFont   = base.deriveFont(Font.BOLD, 28f);
        effectFont  = base.deriveFont(Font.BOLD, 24f);

        // HUD icons
        try {
            pointIcon = ImageIO.read(getClass().getResourceAsStream("/Objects/Point.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            heartIcon = ImageIO.read(getClass().getResourceAsStream("/Objects/Heart3.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // -----------------------------------------------------------------------
    // Main dispatch
    // -----------------------------------------------------------------------

    public void draw(Graphics2D g2) {
        switch (gf.gameState) {
            case MAIN_MENU -> drawMainMenu(g2);
            case PLAYING -> drawHUD(g2);
            case GAME_OVER -> drawGameOver(g2);
        }
    }

    // -----------------------------------------------------------------------
    // Mouse click handling
    // -----------------------------------------------------------------------

    public void handleClick(int x, int y) {
        switch (gf.gameState) {
            case MAIN_MENU -> {
                if (endlessModeBtn.contains(x, y)) {
                    gf.resetGame();
                }
            }
            case GAME_OVER -> {
                if (mainMenuBtn.contains(x, y)) {
                    gf.gameState = GameState.MAIN_MENU;
                }
            }
            default -> {
                /* no interactive buttons during PLAYING */ }
        }
    }

    // -----------------------------------------------------------------------
    // Fading notifications for instant effects
    // -----------------------------------------------------------------------

    public void showNotification(String text, Color color) {
        notification = text;
        notificationColor = color;
        notificationStart = System.nanoTime();
    }

    public void clearNotification() {
        notification = null;
    }

    /** Current alpha of the notification (0 = not visible). */
    private float getNotificationAlpha() {
        if (notification == null) return 0f;
        long elapsed = (System.nanoTime() - notificationStart) / 1_000_000;
        if (elapsed > NOTIF_HOLD_MS + NOTIF_FADE_MS) {
            notification = null;
            return 0f;
        }
        if (elapsed < NOTIF_HOLD_MS) return 1f;
        return Math.max(0f, 1f - (float) (elapsed - NOTIF_HOLD_MS) / NOTIF_FADE_MS);
    }

    // -----------------------------------------------------------------------
    // Main Menu screen
    // -----------------------------------------------------------------------

    private void drawMainMenu(Graphics2D g2) {
        int w = gf.screenWidth;
        int h = gf.screenHeight;

        // Solid background
        g2.setColor(BG);
        g2.fillRect(0, 0, w, h);

        // Title — solid green, no gradient, no shadow
        g2.setFont(titleFont);
        String title = "Absolute Game";
        FontMetrics fm = g2.getFontMetrics();
        int titleX = (w - fm.stringWidth(title)) / 2;
        int titleY = h / 4 + fm.getAscent() / 2;

        g2.setColor(GREEN_TEXT);
        g2.drawString(title, titleX, titleY);

        // Subtitle
        g2.setFont(subtitleFont);
        g2.setColor(Color.WHITE);
        String sub = "Collect points. Survive.";
        FontMetrics sfm = g2.getFontMetrics();
        g2.drawString(sub, (w - sfm.stringWidth(sub)) / 2, titleY + 30);

        // Divider — neutral gray, not green-tinted
        g2.setColor(DIVIDER);
        g2.setStroke(new BasicStroke(1f));
        int lineY = titleY + 50;
        g2.drawLine(w / 2 - 100, lineY, w / 2 + 100, lineY);

        // Button
        drawButton(g2, endlessModeBtn, "Endless Mode");

        // Bottom hint
        g2.setFont(subtitleFont);
        g2.setColor(new Color(160, 165, 170));
        String hint = "Use WASD or Arrow Keys to move";
        FontMetrics hfm = g2.getFontMetrics();
        g2.drawString(hint, (w - hfm.stringWidth(hint)) / 2, h - 20);
    }

    // -----------------------------------------------------------------------
    // HUD (during PLAYING)
    // -----------------------------------------------------------------------

    private void drawHUD(Graphics2D g2) {
        g2.setFont(hudFont);
        g2.setColor(Color.white);

        // Score — top-right
        String scoreStr = "" + gf.player.getPoints();
        g2.drawString(scoreStr, gf.TileSize * 15, 35);
        if (pointIcon != null) {
            g2.drawImage(pointIcon, gf.TileSize * 14, 0, gf.TileSize, gf.TileSize, null);
        }

        // Hearts — top-left
        for (int i = 0; i < gf.player.getHp(); i++) {
            if (heartIcon != null) {
                g2.drawImage(heartIcon, i * gf.TileSize, 0,
                        gf.TileSize * 6 / 7, gf.TileSize * 6 / 7, null);
            }
        }

        // Active LuckyBox effects — bottom-right, stacked bottom-up
        g2.setFont(effectFont);
        FontMetrics efm = g2.getFontMetrics();
        int ex = gf.screenWidth  - 12; // right-align anchor
        int ey = gf.screenHeight - 12; // start from bottom
        int lineH = efm.getHeight() + 4;

        // Reserve the bottom line while an instant-effect notification is fading
        if (getNotificationAlpha() > 0f) {
            ey -= lineH;
        }

        if (gf.player.isConfused) {
            drawLabel(g2, efm, "Confusion Active!", new Color(255, 90, 220), ex, ey); // bright magenta
            ey -= lineH;
        }
        if (gf.player.isRush) {
            drawLabel(g2, efm, "Rush Active!", new Color(110, 255, 130), ex, ey); // bright green
            ey -= lineH;
        }
        if (gf.player.isSlowMotion) {
            drawLabel(g2, efm, "Slow Motion Active!", new Color(90, 210, 255), ex, ey); // bright cyan-blue
            ey -= lineH;
        }
        if (gf.player.hasSecondChance) {
            drawLabel(g2, efm, "Second Chance Active!", new Color(255, 220, 90), ex, ey); // bright gold
        }

        // Draw the fading notification for instant effects on the bottom line
        float alpha = getNotificationAlpha();
        if (alpha > 0f && notification != null) {
            g2.setComposite(AlphaComposite.SrcOver.derive(alpha));
            drawLabel(g2, efm, notification, notificationColor, ex, gf.screenHeight - 12);
            g2.setComposite(AlphaComposite.SrcOver);
        }
    }

    /** Draws right-aligned text with a subtle dark drop shadow for contrast over tiles. */
    private void drawLabel(Graphics2D g2, FontMetrics fm, String label, Color color, int ex, int ey) {
        int x = ex - fm.stringWidth(label);
        g2.setColor(new Color(0, 0, 0, 160)); // dark translucent shadow
        g2.drawString(label, x + 2, ey + 2);
        g2.setColor(color);
        g2.drawString(label, x, ey);
    }

    // -----------------------------------------------------------------------
    // Game Over screen
    // -----------------------------------------------------------------------

    private void drawGameOver(Graphics2D g2) {
        int w = gf.screenWidth;
        int h = gf.screenHeight;

        // Solid background
        g2.setColor(BG);
        g2.fillRect(0, 0, w, h);

        // Title — solid green, no gradient, no shadow
        g2.setFont(titleFont);
        String title = "Game Over";
        FontMetrics fm = g2.getFontMetrics();
        int titleX = (w - fm.stringWidth(title)) / 2;
        int titleY = h / 3 + fm.getAscent() / 2;

        g2.setColor(GREEN_TEXT);
        g2.drawString(title, titleX, titleY);

        // Death message
        g2.setFont(subtitleFont);
        g2.setColor(Color.WHITE);
        String deathMsg = "You ran out of lives!";
        FontMetrics dfm = g2.getFontMetrics();
        g2.drawString(deathMsg, (w - dfm.stringWidth(deathMsg)) / 2, titleY + 32);

        // Score
        g2.setFont(scoreFont);
        g2.setColor(Color.WHITE);
        String scoreStr = "Total Points: " + gf.player.getPoints();
        FontMetrics sfm = g2.getFontMetrics();
        g2.drawString(scoreStr, (w - sfm.stringWidth(scoreStr)) / 2, titleY + 70);

        // Divider — neutral gray
        g2.setColor(DIVIDER);
        g2.setStroke(new BasicStroke(1f));
        g2.drawLine(w / 2 - 100, titleY + 90, w / 2 + 100, titleY + 90);

        // Button
        drawButton(g2, mainMenuBtn, "Main Menu");
    }

    // -----------------------------------------------------------------------
    // Helpers
    // -----------------------------------------------------------------------

    private void drawButton(Graphics2D g2, Rectangle bounds, String label) {
        // Flat dark surface — no translucent neon fill
        g2.setColor(BTN_SURFACE);
        g2.fill(new RoundRectangle2D.Float(
                bounds.x, bounds.y, bounds.width, bounds.height, 8, 8));

        // Crisp 1.5px green border — not thick/glowing
        g2.setColor(BTN_BORDER);
        g2.setStroke(new BasicStroke(1.5f));
        g2.draw(new RoundRectangle2D.Float(
                bounds.x, bounds.y, bounds.width, bounds.height, 8, 8));

        // White label
        g2.setFont(buttonFont);
        g2.setColor(Color.WHITE);
        FontMetrics fm = g2.getFontMetrics();
        int lx = bounds.x + (bounds.width - fm.stringWidth(label)) / 2;
        int ly = bounds.y + (bounds.height + fm.getAscent() - fm.getDescent()) / 2;
        g2.drawString(label, lx, ly);

        g2.setStroke(new BasicStroke(1f));
    }
}
