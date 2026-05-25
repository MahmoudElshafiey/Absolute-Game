package Main;

import entity.Entity;
import java.awt.*;

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

        for (int i = 0; i < gf.object.length; i++) {
            if (gf.object[i] != null) {
                // Store original collision area positions
                int entityAreaX = entity.collisionarea.x + entity.x;
                int entityAreaY = entity.collisionarea.y + entity.y;
                int objectAreaX = gf.object[i].collisionarea.x + gf.object[i].x;
                int objectAreaY = gf.object[i].collisionarea.y + gf.object[i].y;

                // Adjust the entity's collision area based on direction
                switch (entity.direction) {
                    case "up" -> entityAreaY -= entity.speed;
                    case "down" -> entityAreaY += entity.speed;
                    case "left" -> entityAreaX -= entity.speed;
                    case "right" -> entityAreaX += entity.speed;
                }

                // Check for intersection
                if (new Rectangle(entityAreaX, entityAreaY, entity.collisionarea.width, entity.collisionarea.height)
                        .intersects(new Rectangle(objectAreaX, objectAreaY, gf.object[i].collisionarea.width, gf.object[i].collisionarea.height))) {
                    entity.colliding = true;
                    index = i; // Update the index of the collided object

                    if (player) {
                        // Handle collision for player (e.g., pick up item)
                        System.out.println("Player collided with object: " + gf.object[i].name);
                    }
                }
            }
        }
        return index;
    }
}
