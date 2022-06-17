package World;

import Game.GraphicsPanel;

import java.awt.*;

public class Map {
    public static int tileW = 150;

    public static double tileH = tileW/1.5;

    private int rows, cols;

    private Tile[][] tiles;

    private GraphicsPanel graphicsPanel;

    public Map(GraphicsPanel graphicsPanel, int rows, int cols){
        this.graphicsPanel = graphicsPanel;
        this.rows = rows;
        this.cols = cols;
        tiles = new Tile[rows][cols];

        int tileX, ifEvenRow;
        double tileY = (GraphicsPanel.mapPadY) * tileH;
        for(int r = 0; r < rows; r++){
            if(r%2 == 0) {
                tileX = (GraphicsPanel.mapPadX)*tileW+tileW/2;
                ifEvenRow = cols-1;
            } else {
                tileX = (GraphicsPanel.mapPadX)*tileW;
                ifEvenRow = cols;
            }

            for(int c = 0; c < ifEvenRow; c++){
                tiles[r][c] = new Tile(graphicsPanel, tileX, (int) tileY, tileW, (int) tileH);
                tileX += tileW;
            }

            tileY += tileH*2/3;
        }
    }

    public void draw(Graphics2D g2){
        for(int r = 0; r < rows; r++){
            int colNum;
            if(r%2==0){
                colNum = cols-1;
            } else {
                colNum = cols;
            }

            for(int c = 0; c < colNum; c++){
                tiles[r][c].draw(g2);
            }
        }
    }

    public void setTiles(Tile[][] tiles){
        for(int r = 0; r < tiles.length; r++){
            for(int c = 0; c < tiles[0].length; c++){
                this.tiles[r][c] = tiles[r][c];
            }
        }

        System.out.println(tiles[0][0].getType());
        System.out.println(this.tiles[0][0].getType());
    }

    public Tile[][] getTiles(){
        return tiles;
    }
}
