package World;

import Game.GraphicsPanel;
import Game.Images;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class Building {
    public static final int CASTLE = 0, FARM = 1, WOODMILL = 2, TOWER = 3, VILLAGE = 4;

    private Tile tile;

    private int type;

    private BufferedImage img;

    private GraphicsPanel graphicsPanel;

    private static HashMap<Integer, BufferedImage> types;

    static {
        types = new HashMap<>();
        types.put(CASTLE, Images.images.get("building_castle"));
        types.put(FARM, Images.images.get("building_farm"));
        types.put(WOODMILL, Images.images.get("building_woodmill"));
        types.put(TOWER, Images.images.get("building_tower"));
        types.put(VILLAGE, Images.images.get("building_village"));
    }

    public Building(GraphicsPanel graphicsPanel, Tile tile, int type){
        this.graphicsPanel = graphicsPanel;
        this.tile = tile;
        this.type = type;
        this.img = types.get(type);
    }

    public void draw(Graphics2D g2){
        g2.drawImage(img, tile.getX() + Map.tileW/2 - img.getWidth()/2, (int)(tile.getY() + Map.tileH/6) - img.getHeight()/2, null);
    }

    public int getType(){
        return type;
    }

    public GraphicsPanel getGraphicsPanel(){
        return graphicsPanel;
    }
}