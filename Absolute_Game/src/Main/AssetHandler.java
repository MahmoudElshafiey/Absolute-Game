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
        for (int i = 0; i < gf.object.length; i++) {
            gf.object[i] = null;
        }

        int index = 0;
        gf.object[index++] = createObject(new Point(this.gf));

        int negCount = getNegPointCount();
        for (int i = 0; i < negCount; i++) {
            gf.object[index++] = createObject(new NegPoint(this.gf));
        }

        gf.object[index] = createObject(new LifeJuice(this.gf));
    }

    /** Number of NegPoints to spawn at the start of the current game/level. */
    private int getNegPointCount() {
        if (gf.gameMode == GameMode.LEVEL) {
            LevelDefinition level = StageManager.STAGES[gf.currentStageIndex].levels[gf.currentLevelIndex];
            return level.negPointCount;
        }
        return 1;
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
