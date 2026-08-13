package tile;

import Main.GameFrame;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileManager {
    GameFrame gf;
    public Tile[] tile;
    public int maptilenum[][];
    private final BufferedImage mapImage;

    public TileManager(GameFrame gf){
        this.gf=gf;
        tile= new Tile[2];
        maptilenum= new int[gf.maxScreenX][gf.maxScreenY];
        GetTileImage();
        Loadmap();
        mapImage = renderMap();
    }
    public void GetTileImage(){
        try{
            tile[0]= new Tile();
            tile[0].image= scale(ImageIO.read(getClass().getResourceAsStream("/tiles/earth2.png")), gf.TileSize);
            tile[1]= new Tile();
            tile[1].image= scale(ImageIO.read(getClass().getResourceAsStream("/tiles/wall2.png")), gf.TileSize);
            tile[1].collision=true;
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private static BufferedImage scale(BufferedImage src, int size) {
        BufferedImage scaled = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = scaled.createGraphics();
        g.drawImage(src, 0, 0, size, size, null);
        g.dispose();
        return scaled;
    }

    private BufferedImage renderMap() {
        BufferedImage img = new BufferedImage(gf.screenWidth, gf.screenHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        for (int row = 0; row < gf.maxScreenY; row++) {
            for (int col = 0; col < gf.maxScreenX; col++) {
                g.drawImage(tile[maptilenum[col][row]].image,
                        col * gf.TileSize, row * gf.TileSize, null);
            }
        }
        g.dispose();
        return img;
    }
    public void Loadmap(){
        try{
            InputStream is= getClass().getResourceAsStream("/Map/map.txt");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            int column= 0;
            int row=0;
            while(column<gf.maxScreenX && row< gf.maxScreenY){
                String line=bufferedReader.readLine();
                while (column< gf.maxScreenX){
                    String numbers[]=line.split(" ");
                    int number= Integer.parseInt(numbers[column]);
                    maptilenum[column][row]=number;
                    column++;
                }
                if (column== gf.maxScreenX){
                    column=0;
                    row++;
                }
            }
            bufferedReader.close();
        }catch (Exception e){

        }
    }
    public void draw(Graphics2D g2){
        g2.drawImage(mapImage, 0, 0, null);
    }
}