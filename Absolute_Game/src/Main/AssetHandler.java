package Main;

import Objects.SuperObject;
import Objects.Point;
import Objects.NegPoint;
import Objects.LifeJuice;
import java.util.Random;

public class AssetHandler {
    private final GameFrame gf;
    private final Random random = new Random();

    public AssetHandler(GameFrame gf) {
        this.gf = gf;
    }

    public void setObject() {
        gf.object[0] = createObject(new Point(this.gf));
        gf.object[1] = createObject(new NegPoint(this.gf));
        gf.object[2] = createObject(new LifeJuice(this.gf));
    }

    private SuperObject createObject(SuperObject object) {
        Point position = getValidPosition();
        object.x = position.x;
        object.y = position.y;
        return object;
    }

    private Point getValidPosition() {
        Point position;
        do {
            int tileX = random.nextInt(gf.maxScreenX);
            int tileY = random.nextInt(gf.maxScreenY);
            int x = tileX * gf.TileSize;
            int y = tileY * gf.TileSize;
            position = new Point(x, y);
        } while (isWallTile(position.x, position.y) || isTileOccupied(position.x, position.y));
        return position;
    }

    private boolean isWallTile(int x, int y) {
        int tileX = x / gf.TileSize;
        int tileY = y / gf.TileSize;

        if (tileX < 0 || tileY < 0 || tileX >= gf.maxScreenX || tileY >= gf.maxScreenY) {
            return true; // Out of bounds is treated as a wall
        }

        int tileType = gf.tileManager.maptilenum[tileX][tileY];
        return gf.tileManager.tile[tileType].collision;
    }

    private boolean isTileOccupied(int x, int y) {
        for (SuperObject obj : gf.object) {
            if (obj != null && obj.x == x && obj.y == y) {
                return true;
            }
        }
        return false;
    }
}
