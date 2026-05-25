package Main;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class UI {
    GameFrame gf;
    Font font_30;
    BufferedImage point;
    BufferedImage heart;
    public UI(GameFrame gf){
        this.gf=gf;
        font_30 = new Font("Font", Font.BOLD, 30);
        try {
            point= ImageIO.read(getClass().getResourceAsStream("/Objects/Point.png"));
        }catch (IOException e){
            e.printStackTrace();
        }
        try {
            heart= ImageIO.read(getClass().getResourceAsStream("/Objects/Heart3.png"));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2){
        g2.setFont(font_30);
        g2.setColor(Color.white);
        g2.drawString(""+gf.player.getPoints(),gf.TileSize*15,35);
        g2.drawImage(point,gf.TileSize*14,0,gf.TileSize, gf.TileSize,null);
        for (int i=0; i<gf.player.getHp();i++) {
            g2.drawImage(heart, i*gf.TileSize, 0, gf.TileSize*6/7, gf.TileSize*6/7, null);
        }

    }
}
