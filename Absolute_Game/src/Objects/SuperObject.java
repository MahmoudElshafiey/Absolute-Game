package Objects;

import Main.GameFrame;
import entity.Player;

import java.awt.*;
import java.awt.Point;
import java.awt.image.BufferedImage;

public class SuperObject {
    public GameFrame gf;
    public BufferedImage image;
    public String name;
    public boolean collision=false;
    public int x;
    public int y;
    public Rectangle collisionarea= new Rectangle(0,0,35,35);

    public void draw(Graphics2D g2, GameFrame gf) {
        if (image != null) {
            g2.drawImage(image, x, y, gf.TileSize, gf.TileSize, null);
        }
        collisionarea.x=5;

    }
    public void interact(Player player, int index) {
       //Method that will be overwritten by the Subclasses

    }
    public boolean hasAvailableSpace() {
        for (int i = 0; i < gf.object.length; i++) {
            if (gf.object[i] == null) {
                return true; // Space is available
            }
        }
        return false; // No space available
    }
    public java.awt.Point getValidPosition() {
        java.awt.Point position;
        do {
            int newX = (int) (Math.random() * gf.screenWidth / gf.TileSize) * gf.TileSize;
            int newY = (int) (Math.random() * gf.screenHeight / gf.TileSize) * gf.TileSize;
            position = new Point(newX, newY);
        } while (isWallTile(position.x, position.y) || isTileOccupied(position.x, position.y));
        return position;
    }
    public boolean isTileOccupied(int x, int y) {
        for (SuperObject obj : gf.object) {
            if (obj != null && obj.x == x && obj.y == y) {
                return true;
            }
        }
        return false;
    }
    public boolean isWallTile(int x, int y) {
        int tileX = x / gf.TileSize;
        int tileY = y / gf.TileSize;

        if (tileX < 0 || tileY < 0 || tileX >= gf.maxScreenX || tileY >= gf.maxScreenY) {
            return true; // Out of bounds is treated as a wall
        }

        int tileType = gf.tileManager.maptilenum[tileX][tileY];
        return gf.tileManager.tile[tileType].collision;
    }

}
