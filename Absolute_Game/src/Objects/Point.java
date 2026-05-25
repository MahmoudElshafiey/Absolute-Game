package Objects;

import Main.GameFrame;
import entity.Player;
import javax.imageio.ImageIO;
import java.io.IOException;

public class Point extends SuperObject{
    public Point(GameFrame gf){
        this.gf=gf;
        name="Point";
        try {
            image= ImageIO.read(getClass().getResourceAsStream("/Objects/Point.png"));
        }catch (IOException e){
            e.printStackTrace();
        }
        collision=true;

    }

    public Point(int x, int y) {
        this.x=x;
        this.y=y;

    }
    @Override
    public void interact(Player player, int index) {
        player.setPoints(player.getPoints()+1);
        System.out.println("Points: " + player.getPoints());
        if (player.getPoints() >= 2 && player.speed <= 20) {
            player.speed += player.getPoints() % 3;
        }
        if (player.getPoints() % 10 == 1) {
            player.setHp(player.getHp()+1);
        }

        player.gf.object[index] = null;

        if (hasAvailableSpace()) {
            // Create one Point object
            java.awt.Point newPos = getValidPosition();
            Objects.Point newPoint = new Objects.Point(gf);
            newPoint.x = newPos.x;
            newPoint.y = newPos.y;

            for (int i = 0; i < player.gf.object.length; i++) {
                if (player.gf.object[i] == null) {
                    player.gf.object[i] = newPoint;
                    break;
                }
            }

            // Create one NegPoint object
            if (player.getPoints()%2==1) {
                java.awt.Point negPos = getValidPosition();
                Objects.NegPoint newNegPoint = new Objects.NegPoint(this.gf);
                newNegPoint.x = negPos.x;
                newNegPoint.y = negPos.y;

                for (int i = 0; i < gf.object.length; i++) {
                    if (player.gf.object[i] == null) {
                        player.gf.object[i] = newNegPoint;
                        break;
                    }
                }
            }
        }
    }
}
