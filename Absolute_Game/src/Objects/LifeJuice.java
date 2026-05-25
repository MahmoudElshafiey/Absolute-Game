package Objects;

import Main.GameFrame;
import entity.Player;
import javax.imageio.ImageIO;
import java.awt.Point;
import java.io.IOException;

public class LifeJuice extends SuperObject{
    public LifeJuice(GameFrame gf){
        this.gf=gf;
        name="Life Juice";
        try {
            image= ImageIO.read(getClass().getResourceAsStream("/Objects/LifeJuice.png"));
        }catch (IOException e){
            e.printStackTrace();
        }
        collision=true;
    }
    @Override
    public void interact(Player player, int index) {
        if(player.getHp()<10){
            player.setHp(player.getHp()+1);
        }
        System.out.println("Hp: " + player.getHp());
        gf.object[index] = null;

        if (hasAvailableSpace()) {
            Point newPos = getValidPosition();
            Objects.LifeJuice newLifeJuice = new Objects.LifeJuice(this.gf);
            newLifeJuice.x = newPos.x;
            newLifeJuice.y = newPos.y;

            for (int i = 0; i < gf.object.length; i++) {
                if (gf.object[i] == null) {
                    gf.object[i] = newLifeJuice;
                    break;
                }
            }
        }
    }
}
