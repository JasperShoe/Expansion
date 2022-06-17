package Game;

import Entities.*;
import Network.Client;
import Network.Packets.Packet00Connect;
import Network.Server;
import World.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

public class GraphicsPanel extends JPanel {
    public static Client socketClient;

    public static Server socketServer;

    public static int mapPadX = 1, mapPadY = 1, mapW = Main.width*2, mapH = Main.height*2, mapR = (int)(mapH/(Map.tileH*2/3)-2*mapPadY), mapC = mapW/Map.tileW-2*mapPadX;

    private Player player;

    private Map map;

    private ArrayList<Player> players = new ArrayList<>();

    public GraphicsPanel(){
        setSize(mapW, mapH);
        setLayout(null);
        setFocusable(true);

        map = new Map(this, mapR, mapC);

        int castleR = new Random().nextInt(mapR), castleC;
        if(castleR%2==0){
            castleC = new Random().nextInt(mapC-1);
        } else {
            castleC = new Random().nextInt(mapC);
        }
        player = new PlayerMP(this, JOptionPane.showInputDialog(this, "Enter your username:"), castleR, castleC, null, -1, false);


        if(JOptionPane.showConfirmDialog(this, "Do you wish to run the server?") == 0){
            socketServer = new Server(this, 1331);
            socketServer.start();
        }

        socketClient = new Client(this, "localhost", 1331);
        socketClient.start();

        Packet00Connect connectPacket = new Packet00Connect(player.getUsername(), player.getCastleR(), player.getCastleC());
        if (socketServer != null){
            socketServer.addConnection((PlayerMP) player, connectPacket);
        } else {
            players.add(player);
        }
        connectPacket.writeData(socketClient);

        Timer update = new Timer(1000/60, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                repaint();
            }
        });
        update.start();

        addKeyListener(player);
        addMouseListener(player);
        addMouseMotionListener(player);
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.translate(player.getTransX(), player.getTransY());

        for(Player player : players){
            player.update();
        }

        g2.setColor(new Color(40, 50, 180));
        g2.fillRect(0, 0, mapW, mapH);
        map.draw(g2);
    }

    public void addPlayerMP(PlayerMP player){
        players.add(player);
    }

    public void removePlayerMP(String username){
        int index = 0;
        for(Player player : players){
            if(player.getUsername().equalsIgnoreCase(username)){
                player.conquered();
                break;
            } else {
                index++;
            }
        }
        players.remove(index);
    }

    public void addPlayerMPTile(String username, int r, int c){
        for(Player player : players){
            if(player.getUsername().equalsIgnoreCase(username)){
                map.getTiles()[r][c].setOwner(player);
            }
        }
    }

    public Map getMap(){
        return map;
    }

    public Player getPlayer() {
        return player;
    }
}
