package Main;

import entity.Entity;
import Objects.SuperObject;

public class CollisionChecker {
    GameFrame gf;

    public CollisionChecker(GameFrame gf) {
        this.gf = gf;
    }

    public void Checktile(Entity entity) {
        int entityleftworldX = entity.x + entity.collisionarea.x;
        int entityrightworldX = entity.x + entity.collisionarea.x + entity.collisionarea.width;
        int entitytopworldY = entity.y + entity.collisionarea.y;
        int entitybottomworldY = entity.y + entity.collisionarea.y + entity.collisionarea.height;

        int entityLeftcolumn = entityleftworldX / gf.TileSize;
        int entityRightcolumn = entityrightworldX / gf.TileSize;
        int entityTopRow = entitytopworldY / gf.TileSize;
        int entityBottomrow = entitybottomworldY / gf.TileSize;

        int tilenum1, tilenum2;
        switch (entity.direction) {
            case "up" -> {
                entityTopRow = (entitytopworldY - entity.speed) / gf.TileSize;
                tilenum1 = gf.tileManager.maptilenum[entityLeftcolumn][entityTopRow];
                tilenum2 = gf.tileManager.maptilenum[entityRightcolumn][entityTopRow];
                if (gf.tileManager.tile[tilenum1].collision || gf.tileManager.tile[tilenum2].collision) {
                    entity.colliding = true;
                }
            }
            case "down" -> {
                entityBottomrow = (entitybottomworldY + entity.speed) / gf.TileSize;
                tilenum1 = gf.tileManager.maptilenum[entityLeftcolumn][entityBottomrow];
                tilenum2 = gf.tileManager.maptilenum[entityRightcolumn][entityBottomrow];
                if (gf.tileManager.tile[tilenum1].collision || gf.tileManager.tile[tilenum2].collision) {
                    entity.colliding = true;
                }
            }
            case "left" -> {
                entityLeftcolumn = (entityleftworldX - entity.speed) / gf.TileSize;
                tilenum1 = gf.tileManager.maptilenum[entityLeftcolumn][entityTopRow];
                tilenum2 = gf.tileManager.maptilenum[entityLeftcolumn][entityBottomrow];
                if (gf.tileManager.tile[tilenum1].collision || gf.tileManager.tile[tilenum2].collision) {
                    entity.colliding = true;
                }
            }
            case "right" -> {
                entityRightcolumn = (entityrightworldX + entity.speed) / gf.TileSize;
                tilenum1 = gf.tileManager.maptilenum[entityRightcolumn][entityTopRow];
                tilenum2 = gf.tileManager.maptilenum[entityRightcolumn][entityBottomrow];
                if (gf.tileManager.tile[tilenum1].collision || gf.tileManager.tile[tilenum2].collision) {
                    entity.colliding = true;
                }
            }
        }
    }

    public int checkObject(Entity entity, boolean player) {
        int index = -1; // Default to -1 if no collision occurs

        // Entity's collision area, adjusted by movement direction.
        // Computed once here instead of allocating Rectangles per object.
        int entityAreaX = entity.collisionarea.x + entity.x;
        int entityAreaY = entity.collisionarea.y + entity.y;

        switch (entity.direction) {
            case "up" -> entityAreaY -= entity.speed;
            case "down" -> entityAreaY += entity.speed;
            case "left" -> entityAreaX -= entity.speed;
            case "right" -> entityAreaX += entity.speed;
        }

        int entityW = entity.collisionarea.width;
        int entityH = entity.collisionarea.height;

        for (int i = 0; i < gf.object.length; i++) {
            SuperObject obj = gf.object[i];
            if (obj == null) {
                continue;
            }

            int objectAreaX = obj.collisionarea.x + obj.x;
            int objectAreaY = obj.collisionarea.y + obj.y;

            // Direct AABB overlap test — avoids allocating Rectangles every frame
            if (entityAreaX < objectAreaX + obj.collisionarea.width
                    && entityAreaX + entityW > objectAreaX
                    && entityAreaY < objectAreaY + obj.collisionarea.height
                    && entityAreaY + entityH > objectAreaY) {
                entity.colliding = true;
                index = i; // Update the index of the collided object

                if (player) {
                    // Handle collision for player (e.g., pick up item)
                    System.out.println("Player collided with object: " + obj.name);
                }
            }
        }
        return index;
    }
}
