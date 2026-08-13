package tile;

import Main.GameFrame;
import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileManager {
    GameFrame gf;
    public Tile[] tile;
    public int maptilenum[][];

    public TileManager(GameFrame gf){
        this.gf=gf;
        tile= new Tile[2];
        maptilenum= new int[gf.maxScreenX][gf.maxScreenY];
        GetTileImage();
        Loadmap();
    }
    public void GetTileImage(){
        try{
            tile[0]= new Tile();
            tile[0].image= ImageIO.read(getClass().getResourceAsStream("/tiles/earth2.png"));
            tile[1]= new Tile();
            tile[1].image= ImageIO.read(getClass().getResourceAsStream("/tiles/wall2.png"));
            tile[1].collision=true;
        }catch (IOException e){
            e.printStackTrace();
        }
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
        int column=0;
        int row= 0;
        int x=0;
        int y=0;
        while(column<gf.maxScreenX && row< gf.maxScreenY){
            int tilenum= maptilenum[column][row];
            g2.drawImage(tile[tilenum].image,x,y,gf.TileSize,gf.TileSize,null);
            column++;
            x+=gf.TileSize;

            if (column==gf.maxScreenX){
                column=0;
                x=0;
                row++;
                y+=gf.TileSize;
            }
        }
    }
}