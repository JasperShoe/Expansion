package World;

import Entities.Player;
import Game.GraphicsPanel;

import java.awt.*;
import java.util.HashMap;
import java.util.Random;

public class Tile {
    public static final int DESERT = 0, HILL = 1, PLAIN = 2, WOODLAND = 3;

    private int x, y, w, h, type, thickness = 4;

    private Polygon shape, shapeInner;

    private int[] xPoints, yPoints, xPointsInner, yPointsInner;

    private boolean selected;

    private Player player;

    private Color ownerColor, neutral = new Color(135, 135, 135);

    private static HashMap<Integer, Color> types;

    private GraphicsPanel graphicsPanel;

    private Building building;

    static {
        types = new HashMap<>();
        types.put(DESERT, new Color(220, 220, 135));
        types.put(HILL, new Color(120, 80, 0));
        types.put(PLAIN, new Color(106, 175, 26));
        types.put(WOODLAND, new Color(21, 112, 7));
    }

    public Tile(GraphicsPanel graphicsPanel, int x, int y, int w, int h){
        this.graphicsPanel = graphicsPanel;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        player = null;
        ownerColor = neutral;
        building = null;
        selected = false;

        type = new Random().nextInt(100);

        int numRareTiles = 2, rarePercent = 15, percent = 0;
        for(int i = 0; i < types.size(); i++){
            if(i < numRareTiles){
                percent += rarePercent;
            } else {
                percent += (100-numRareTiles*rarePercent)/(types.size()-numRareTiles);
            }
            if (type < percent){
                type = i;
                break;
            }
        }

        xPoints = new int[6];
        yPoints = new int[6];
        xPointsInner = new int[6];
        yPointsInner = new int[6];
        int pointX = 0, pointY = 0;
        for(int i = 0; i < 6; i++){
            xPoints[i] = x + pointX;
            yPoints[i] = y + pointY;
            xPointsInner[i] = xPoints[i];
            yPointsInner[i] = yPoints[i];

            if(i < 2){
                pointX += w/2;
            } else if(i > 2){
                pointX -= w/2;
            }

            if(i == 0 || i == 4){
                pointY -= h/3;
            } else {
                pointY += h/3;
            }
        }

        xPointsInner[0] += thickness/2;
        xPointsInner[2] -= thickness/2;
        xPointsInner[3] -= thickness/2;
        xPointsInner[5] += thickness/2;
        yPointsInner[1] += thickness/2;
        yPointsInner[4] -= thickness/2;

        shape = new Polygon(xPoints, yPoints, xPoints.length);
        shapeInner = new Polygon(xPointsInner, yPointsInner, xPointsInner.length);
    }

    public void draw(Graphics2D g2){
        g2.setColor(types.get(type));
        g2.fillPolygon(shape);

        g2.setStroke(new BasicStroke(thickness));
        if(selected){
            g2.setColor(Color.GREEN);
        } else if(player != null){
            g2.setColor(ownerColor);
        } else {
            g2.setColor(neutral);
        }
        g2.drawPolygon(shapeInner);

        g2.drawString(Integer.toString(type), x + 10, y + 10);

        if(building != null){
            building.draw(g2);
        }
    }

    public void setBuilding(Building building){
        this.building = building;
    }

    public void setOwner(Player player){
        this.player = player;
        if(player == null){
            this.ownerColor = neutral;
        } else {
            this.ownerColor = player.getColor();
        }
    }

    public void setSelected(boolean selected){
        this.selected = selected;
    }

    public void updateColor(Color ownerColor){
        this.ownerColor = ownerColor;
    }

    public Polygon getShape() {
        return shape;
    }

    public Player getPlayer() {
        return player;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getType(){
        return type;
    }
}