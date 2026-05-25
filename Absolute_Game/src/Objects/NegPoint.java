package Objects;

import Main.GameFrame;
import entity.Player;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.Point;
import java.io.IOException;

public class NegPoint extends SuperObject{

    public NegPoint(GameFrame gf){
        this.gf=gf;
        name="Negative Point";
        try {
            image= ImageIO.read(getClass().getResourceAsStream("/Objects/NegPoint.png"));
        }catch (IOException e){
            e.printStackTrace();
        }
        collision=true;

    }
    @Override
    public void interact(Player player, int index) {
        player.setHp(player.getHp()-1);
        player.setNegpointgained(player.getNegpointgained()+1);
        if (player.getNegpointgained()%5==1){
            player.speed--;
        }
        System.out.println("Hp: " + player.getHp());
        System.out.println("Negative Points: "+player.getNegpointgained());
        gf.object[index] = null;

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

            if (player.getHp() <= 0) {
                JOptionPane.showMessageDialog(null, "Game Over");
                JOptionPane.showMessageDialog(null, "Total Points: "+player.getPoints());
            }
        }
    }
}
