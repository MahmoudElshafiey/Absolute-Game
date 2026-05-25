package Main;

import javax.swing.*;
import java.awt.*;
import Objects.SuperObject;
import entity.Player;
import tile.TileManager;

// Basically handels everything that happens in the game
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
    public SuperObject object[] = new SuperObject[100];
    public AssetHandler ah = new AssetHandler(this);
    public UI ui= new UI(this);
    private boolean gameRunning = true;

    public GameFrame() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.darkGray);
        this.setFocusable(true);
        this.addKeyListener(input);

        player = new Player(this, input);
    }

    public void setupGame() {
        ah.setObject();
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

        while (gameRunning) {
            long currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }

        // Stop the thread when the game ends
        stopThread();
    }

    public void stopThread() {
        gameThread = null;
    }

    public void update() {
        player.update();

        // End the game if the player's hp reaches 0
        if (player.getHp() <= 0) {
            gameRunning = false;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Draw game elements
        tileManager.draw(g2);
        for (SuperObject superObject : object) {
            if (superObject != null) {
                superObject.draw(g2, this);
            }
        }
        player.draw(g2);
        ui.draw(g2);
        g2.dispose();
    }
}
