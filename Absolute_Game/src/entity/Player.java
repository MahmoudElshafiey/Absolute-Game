package entity;

import Main.GameFrame;
import Main.InputHandler;
import Objects.SuperObject;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;


public class Player extends Entity {
    public final GameFrame gf;
    private final InputHandler ih;
    private int hp = 5;
    private int points = 0;
    private int negpointgained=0;

    public Player(GameFrame gf, InputHandler ih) {
        this.gf = gf;
        this.ih = ih;
        collisionarea = new Rectangle(8, 16, 20, 20);
        setDefaults();
        getPlayerImage();
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getNegpointgained() {
        return negpointgained;
    }

    public void setNegpointgained(int negpointgained) {
        this.negpointgained = negpointgained;
    }

    private void setDefaults() {
        x = 100;
        y = 100;
        solidAreaDefaultX = collisionarea.x;
        solidAreaDefaultY = collisionarea.y;
        speed = 4;
        direction = "down";
    }

    public void getPlayerImage() {
        try {
            up1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_up_1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_up_2.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_down_1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_down_2.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_left_1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_left_2.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_right_1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_right_2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        if (ih.upPressed || ih.downPressed || ih.leftPressed || ih.rightPressed) {
            // Set direction based on input
            if (ih.upPressed) direction = "up";
            else if (ih.downPressed) direction = "down";
            else if (ih.leftPressed) direction = "left";
            else if (ih.rightPressed) direction = "right";

            // Reset collision flag
            colliding = false;

            // Check for tile collisions
            gf.collisionChecker.Checktile(this);

            // Check for object collisions
            int objectIndex = gf.collisionChecker.checkObject(this, true);
            if (objectIndex != -1) {
                // Get the object and call its interact method
                SuperObject object = gf.object[objectIndex];
                if (object != null) {
                    object.interact(this, objectIndex); // Polymorphic call
                }
            }

            // Move if not colliding
            if (!colliding) {
                switch (direction) {
                    case "up" -> y -= speed;
                    case "down" -> y += speed;
                    case "left" -> x -= speed;
                    case "right" -> x += speed;
                }
            }

            // Update sprite animation
            spriteCounter++;
            if (spriteCounter > 10) {
                spriteNum = (spriteNum == 1) ? 2 : 1;
                spriteCounter = 0;
            }
        }
    }


    public void draw(Graphics2D g2) {
        BufferedImage image = null;
        switch (direction) {
            case "up" -> image = (spriteNum == 1) ? up1 : up2;
            case "down" -> image = (spriteNum == 1) ? down1 : down2;
            case "left" -> image = (spriteNum == 1) ? left1 : left2;
            case "right" -> image = (spriteNum == 1) ? right1 : right2;
        }
        g2.drawImage(image, x, y, gf.TileSize, gf.TileSize, null);
    }
}
