package Objects;

import Main.GameFrame;
import entity.Player;

import javax.imageio.ImageIO;
import java.io.IOException;

public class LuckyBox extends SuperObject {

    // Negation Pulse destroys objects within this radius (3 tiles = 144px)
    private static final int PULSE_RADIUS = 3 * 48;

    public LuckyBox(GameFrame gf) {
        this.gf = gf;
        name = "Lucky Box";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/Objects/LuckyBox.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        collision = true;
    }

    @Override
    public void interact(Player player, int index) {
        System.out.println("Player collided with object: Lucky Box");

        // Object interaction — clears Rush and Confusion
        player.onObjectInteraction();

        // Remove this box; it does NOT self-respawn (Point spawns a new one every 10 pts)
        gf.object[index] = null;

        // ── Weighted roll ──────────────────────────────────────────────────
        // 5% Treasure | 5% Cursed | 15% Second Chance | 15% Negation Pulse
        // 15% Slow Motion | 15% Lucky Heal | 15% Rush | 15% Confusion
        double roll = Math.random();

        if (roll < 0.05) {
            // ── Treasure (+100 points) ────────────────────────────────────
            player.setPoints(player.getPoints() + 100);
            System.out.println("[LuckyBox] Treasure! +100 pts → " + player.getPoints());

        } else if (roll < 0.10) {
            // ── Cursed Box (-1 HP, -25 points) ───────────────────────────
            player.setHp(Math.max(0, player.getHp() - 1));
            player.setPoints(Math.max(0, player.getPoints() - 25));
            System.out.println("[LuckyBox] Cursed Box! HP:" + player.getHp() + " Pts:" + player.getPoints());

        } else if (roll < 0.25) {
            // ── Second Chance (absorbs next harmful hit) ──────────────────
            player.hasSecondChance = true;
            System.out.println("[LuckyBox] Second Chance! Next harmful collision blocked.");

        } else if (roll < 0.40) {
            // ── Negation Pulse (destroy nearby objects; NegPoints don't respawn) ─
            applyNegationPulse(player);

        } else if (roll < 0.55) {
            // ── Slow Motion (half speed until next Point interaction) ──────
            if (player.isRush) {           // cancel Rush first if active
                player.speed = player.savedSpeed;
                player.isRush = false;
            }
            player.savedSpeed = player.speed;
            player.speed = Math.max(1, player.speed / 2);
            player.isSlowMotion = true;
            System.out.println("[LuckyBox] Slow Motion! Speed → " + player.speed);

        } else if (roll < 0.70) {
            // ── Lucky Heal (+1 HP; +10 pts at full HP) ────────────────────
            if (player.getHp() < 10) {
                player.setHp(player.getHp() + 1);
                System.out.println("[LuckyBox] Lucky Heal! HP → " + player.getHp());
            } else {
                player.setPoints(player.getPoints() + 10);
                System.out.println("[LuckyBox] Lucky Heal (full HP)! +10 pts → " + player.getPoints());
            }

        } else if (roll < 0.85) {
            // ── Rush (+4 speed until next object interaction) ─────────────
            if (player.isSlowMotion) {     // cancel SlowMotion first if active
                player.speed = player.savedSpeed;
                player.isSlowMotion = false;
            }
            player.savedSpeed = player.speed;
            player.speed = Math.min(20, player.speed + 4);
            player.isRush = true;
            System.out.println("[LuckyBox] Rush! Speed → " + player.speed);

        } else {
            // ── Confusion (reversed controls until next object interaction) ─
            player.isConfused = true;
            System.out.println("[LuckyBox] Confusion! Controls reversed.");
        }
    }

    /**
     * Destroys all objects within PULSE_RADIUS of the player centre.
     * Objects are nulled directly — no interact() is called, so NegPoints
     * do not trigger their respawn logic.
     */
    private void applyNegationPulse(Player player) {
        int px = player.x + gf.TileSize / 2;
        int py = player.y + gf.TileSize / 2;
        int destroyed = 0;

        for (int i = 0; i < gf.object.length; i++) {
            SuperObject obj = gf.object[i];
            if (obj == null) continue;

            int ox = obj.x + gf.TileSize / 2;
            int oy = obj.y + gf.TileSize / 2;
            double dist = Math.sqrt((px - ox) * (double)(px - ox) + (py - oy) * (double)(py - oy));

            if (dist <= PULSE_RADIUS) {
                System.out.println("[NegationPulse] Destroyed: " + obj.name);
                gf.object[i] = null;
                destroyed++;

                if (obj instanceof NegPoint) {
                    // Permanently gone — NegPoints do NOT respawn from Negation Pulse
                } else if (obj instanceof Point && hasAvailableSpace()) {
                    // Relocate the Point to a new position
                    java.awt.Point newPos = getValidPosition();
                    Objects.Point relocated = new Objects.Point(gf);
                    relocated.x = newPos.x;
                    relocated.y = newPos.y;
                    for (int j = 0; j < gf.object.length; j++) {
                        if (gf.object[j] == null) { gf.object[j] = relocated; break; }
                    }
                } else if (obj instanceof LifeJuice && hasAvailableSpace()) {
                    // Relocate the LifeJuice to a new position
                    java.awt.Point newPos = getValidPosition();
                    Objects.LifeJuice relocated = new Objects.LifeJuice(gf);
                    relocated.x = newPos.x;
                    relocated.y = newPos.y;
                    for (int j = 0; j < gf.object.length; j++) {
                        if (gf.object[j] == null) { gf.object[j] = relocated; break; }
                    }
                }
                // LuckyBox caught in the pulse — no respawn (spawned by score milestones)
            }
        }
        System.out.println("[LuckyBox] Negation Pulse! " + destroyed + " object(s) cleared within " + PULSE_RADIUS + "px.");
    }

}
