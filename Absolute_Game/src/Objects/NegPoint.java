package Objects;

import Main.GameFrame;
import entity.Player;
import javax.imageio.ImageIO;
import java.awt.Point;
import java.io.IOException;

public class NegPoint extends SuperObject {

    public NegPoint(GameFrame gf) {
        this.gf = gf;
        name = "Negative Point";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/Objects/NegPoint.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        collision = true;
    }

    @Override
    public void interact(Player player, int index) {
        // Object interaction clears Rush and Confusion
        player.onObjectInteraction();

        // Second Chance absorbs this hit — NegPoint still respawns normally
        if (player.hasSecondChance) {
            player.hasSecondChance = false;
            System.out.println("[SecondChance] Harmful collision blocked!");
            gf.object[index] = null;
            spawnReplacement();
            return;
        }

        player.setHp(player.getHp() - 1);
        player.setNegpointgained(player.getNegpointgained() + 1);
        if (player.getNegpointgained() % 5 == 1) {
            player.speed--;
        }
        System.out.println("Hp: " + player.getHp());
        System.out.println("Negative Points: " + player.getNegpointgained());
        gf.object[index] = null;

        spawnReplacement();
    }

    /** Spawns a new NegPoint at a valid random position if space is available. */
    private void spawnReplacement() {
        if (hasAvailableSpace()) {
            Point newPos = getValidPosition();
            Objects.NegPoint newNegPoint = new Objects.NegPoint(this.gf);
            newNegPoint.x = newPos.x;
            newNegPoint.y = newPos.y;

            for (int i = 0; i < gf.object.length; i++) {
                if (gf.object[i] == null) {
                    gf.object[i] = newNegPoint;
                    break;
                }
            }
        }
    }
}
