package Objects;

import Main.GameFrame;
import entity.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.Point;
import java.io.IOException;

public class LuckyBox extends SuperObject{
    public LuckyBox(GameFrame gf){
        this.gf=gf;
        name="Lucky Box";
        try {
            image= ImageIO.read(getClass().getResourceAsStream("/Objects/LuckyBox.png"));
        }catch (IOException e){
            e.printStackTrace();
        }
        collision=true;

    }
    @Override
    public void interact(Player player, int index) {




    }
}
