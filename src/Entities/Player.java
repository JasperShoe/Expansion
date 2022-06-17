package Entities;

import Game.GraphicsPanel;
import Game.Images;
import Game.Main;
import Network.Packets.Packet02AddTile;
import Network.Packets.Packet03RemoveTile;
import World.Building;
import World.ResourceCollector;
import World.Tile;

import java.awt.*;
import java.awt.event.*;

public class Player implements KeyListener, MouseListener, MouseMotionListener {
    public static final Color COLOR = new Color(0, 0, 175), COLORMP = new Color(175, 0, 0);

    private int transX, transY, moveX, moveY, moveBounds, moveSpeed, gold, food, wood, castleR, castleC, currentTileR, currentTileC;

    private String username;

    private Tile[][] tiles, worldTiles;

    private Color color;

    private GraphicsPanel graphicsPanel;

    private Cursor cursor_attack, cursor_select;

    private Tile castle, currentTile;

    public Player(GraphicsPanel graphicsPanel, String username, int castleR, int castleC){
        this.graphicsPanel = graphicsPanel;
        this.username = username;
        color = COLOR;
        transX = 0;
        transY = 0;
        moveX = -1;
        moveY = -1;
        moveBounds = 200;
        moveSpeed = 10;
        gold = 0;
        food = 0;
        wood = 0;
        tiles = new Tile[graphicsPanel.mapR][graphicsPanel.mapC];
        worldTiles = graphicsPanel.getMap().getTiles();
        this.castleR = castleR;
        this.castleC = castleC;
        setCastle(castleR, castleC);
        currentTile = castle;
        currentTileR = castleR;
        currentTileC = castleC;

        cursor_attack = Toolkit.getDefaultToolkit().createCustomCursor(Images.images.get("cursor_attack"), new Point(0, 0), "cursor_attack");
        cursor_select = Toolkit.getDefaultToolkit().createCustomCursor(Images.images.get("cursor_select"), new Point(0, 0), "cursor_select");
        graphicsPanel.setCursor(cursor_select);
    }

    public void update(){
        if (moveX == 0 && transX < 0) {
            transX += moveSpeed;
        } else if (moveX == 1 && transX > (GraphicsPanel.mapW - Main.width)*-1) {
            transX -= moveSpeed;
        }


        if(moveY == 0 && transY < 0){
            transY += moveSpeed;
        } else if(moveY == 1 && transY > (GraphicsPanel.mapH - Main.height)*-1){
            transY -= moveSpeed;
        }
    }

    public void setCastle(int castleR, int castleC){
        castle = worldTiles[castleR][castleC];
        castle.setBuilding(new Building(graphicsPanel, castle, Building.CASTLE));
        addTile(castle, castleR, castleC);
    }

    public void updateColor(Color color){
        this.color = color;
        for(int r = 0; r < graphicsPanel.mapR; r++){
            for(int c = 0; c < graphicsPanel.mapC; c++){
                if(tiles[r][c] != null && tiles[r][c].getPlayer() != null){
                    tiles[r][c].updateColor(color);
                }
            }
        }
    }

    public void select(int mouseX, int mouseY){
        boolean stop = false;
        for(int r = 0; r < worldTiles.length; r++){
            for(int c = 0; c < worldTiles[0].length; c++){
                if (worldTiles[r][c] != null && worldTiles[r][c].getShape().contains(mouseX, mouseY)) {
                    currentTile.setSelected(false);
                    currentTile = worldTiles[r][c];
                    currentTile.setSelected(true);
                    currentTileR = r;
                    currentTileC = c;
                    stop = true;
                }
                if(stop){
                    break;
                }
            }
            if(stop){
                break;
            }
        }
    }

    public void attack(){
        boolean stop = false;
        int r = currentTileR, c = currentTileC;
        for (int r2 = -1; r2 < 2; r2++) {
            for (int c2 = -1; c2 < 2; c2++) {
                if (((r2 == -1 || r2 == 1) && c2 != -Math.pow(-1, r % 2)) || (r2 == 0 && c2 != 0)) {
                    if(r+r2 >= 0 && r+r2 < worldTiles.length && c+c2 >= 0 && c+c2 < worldTiles[0].length && tiles[r+r2][c+c2] != null) {
                        addTile(currentTile, r, c);
                        Packet02AddTile addTilePacket = new Packet02AddTile(getUsername(), r, c);
                        addTilePacket.writeData(graphicsPanel.socketClient);
                        stop = true;
                        break;
                    }
                }
                if (stop) {
                    break;
                }
            }
            if (stop) {
                break;
            }
        }
    }

    public void build(){
        if(currentTile != null && currentTile.getPlayer() == this) {
            currentTile.setBuilding(new ResourceCollector(graphicsPanel, currentTile, Building.FARM));
        }
    }

    public void addTile(Tile tile, int r, int c){
        tile.setOwner(this);
        tiles[r][c] = tile;
    }

    public void conquered(){
        for(int r = 0; r < graphicsPanel.mapR; r++){
            for(int c = 0; c < graphicsPanel.mapC; c++){
                if(tiles[r][c] != null && tiles[r][c].getPlayer() != null){
                    worldTiles[r][c].setOwner(null);
                    Packet03RemoveTile packet = new Packet03RemoveTile(getUsername(), r, c);
                    packet.writeData(graphicsPanel.socketClient);
                }
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if(keyCode == KeyEvent.VK_A){
            attack();
        } else if(keyCode == KeyEvent.VK_B){
            build();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        select(e.getX() - transX, e.getY() - transY);
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        moveX = -1;
        moveY = -1;

        if(e.getX() < moveBounds){
            moveX = 0;
        } else if(e.getX() > Main.width - moveBounds){
            moveX = 1;
        }

        if(e.getY() < moveBounds){
            moveY = 0;
        } else if(e.getY() > Main.height - moveBounds){
            moveY = 1;
        }
    }

    public int getTransX(){
        return transX;
    }

    public int getTransY(){
        return transY;
    }

    public Color getColor(){
        return color;
    }

    public String getUsername() {
        return username;
    }

    public int getCastleR(){
        return castleR;
    }

    public int getCastleC(){
        return castleC;
    }

    public int getFood() {
        return food;
    }

    public int getWood() {
        return wood;
    }

    public int getGold() {
        return gold;
    }

    public GraphicsPanel getGraphicsPanel(){
        return graphicsPanel;
    }

    public void setFood(int food){
        this.food = food;
    }

    public void setWood(int wood){
        this.wood = wood;
    }

    public void setGold(int gold){
        this.gold = gold;
    }
}