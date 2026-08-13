package Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import Objects.SuperObject;
import entity.Player;
import tile.TileManager;

// Handles everything that happens in the game
public class GameFrame extends JPanel implements Runnable {
    // Screen settings
    private final int ogTileSize = 16;
    private final int scale = 3;
    public final int TileSize = ogTileSize * scale; // 48x48

    // FPS settings
    private final int FPS = 60;

    // Game screen dimensions
    public final int maxScreenX = 16;
    public final int maxScreenY = 12;
    public final int screenWidth = TileSize * maxScreenX; // 768 pixels
    public final int screenHeight = TileSize * maxScreenY; // 576 pixels

    public TileManager tileManager = new TileManager(this);
    private final InputHandler input = new InputHandler();
    private Thread gameThread;
    public final Player player;
    public CollisionChecker collisionChecker = new CollisionChecker(this);
    public SuperObject[] object = new SuperObject[100];
    public AssetHandler ah = new AssetHandler(this);
    public UI ui = new UI(this);

    // Current game state — starts at main menu
    public GameState gameState = GameState.MAIN_MENU;

    public GameFrame() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.darkGray);
        this.setFocusable(true);
        this.addKeyListener(input);

        // Mouse listener to handle menu/game-over button clicks
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ui.handleClick(e.getX(), e.getY());
            }
        });

        player = new Player(this, input);
    }

    public void setupGame() {
        ah.setObject();
    }

    /**
     * Resets the game to a clean state and transitions to PLAYING.
     * Called when the player clicks "Endless Mode" or returns from Game Over.
     */
    public void resetGame() {
        player.reset();
        ah.setObject();
        ui.clearNotification();
        gameState = GameState.PLAYING;
    }

    public void startThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double drawInterval = 1000000000.0 / FPS;
        double delta = 0;

        // Loop runs forever — state enum controls what gets updated/drawn
        while (gameThread != null) {
            long currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
    }

    public void stopThread() {
        gameThread = null;
    }

    public void update() {
        if (gameState == GameState.PLAYING) {
            player.update();

            // Transition to Game Over when HP reaches 0
            if (player.getHp() <= 0) {
                gameState = GameState.GAME_OVER;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Always enable anti-aliasing for smooth text/shapes
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        if (gameState == GameState.PLAYING || gameState == GameState.GAME_OVER) {
            // Draw game world
            tileManager.draw(g2);
            for (SuperObject superObject : object) {
                if (superObject != null) {
                    superObject.draw(g2, this);
                }
            }
            player.draw(g2);
        }

        // UI handles drawing the correct overlay for each state
        ui.draw(g2);

        g2.dispose();
    }
}
