package Network;

import Entities.PlayerMP;
import Game.GraphicsPanel;
import Network.Packets.*;

import java.io.IOException;
import java.net.*;

public class Client extends Thread {
    private InetAddress ip;

    private int port;

    private DatagramSocket socket;

    private GraphicsPanel graphicsPanel;

    public Client(GraphicsPanel graphicsPanel, String ip, int port){
        this.graphicsPanel = graphicsPanel;
        this.port = port;
        try {
            this.ip = InetAddress.getByName(ip);
            this.socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
         catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void run(){
        while (true){
             byte[] data = new byte[1024];
             DatagramPacket packet = new DatagramPacket(data, data.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }

            parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
        }
    }

    private void parsePacket(byte[] data, InetAddress ip, int port) {
        String message = new String(data).trim();
        Packet.PacketTypes type = Packet.lookUpPacket(message.substring(0, 2));
        Packet packet = null;

        switch (type){
            default:
            case INVALID:
                break;
            case CONNECT:
                packet = new Packet00Connect(data);
                System.out.println("[" + ip.getHostAddress() + ":" + port + "] " + ((Packet00Connect)packet).getUsername() + " has joined the game.");
                PlayerMP player;
                if(((Packet00Connect) packet).getUsername().equalsIgnoreCase(graphicsPanel.getPlayer().getUsername())) {
                    player = new PlayerMP(graphicsPanel, ((Packet00Connect) packet).getUsername(), ((Packet00Connect) packet).getCastleR(), ((Packet00Connect) packet).getCastleC(), ip, port, false);
                } else {
                    player = new PlayerMP(graphicsPanel, ((Packet00Connect) packet).getUsername(), ((Packet00Connect) packet).getCastleR(), ((Packet00Connect) packet).getCastleC(), ip, port, true);
                }
                graphicsPanel.addPlayerMP(player);
                graphicsPanel.getMap().setTiles(player.getGraphicsPanel().getMap().getTiles());
                System.out.println(player.getUsername());
                break;
            case DISCONNECT:
                packet = new Packet01Disconnect(data);
                System.out.println("[" + ip.getHostAddress() + ":" + port + "] " + ((Packet01Disconnect)packet).getUsername() + " has left.");
                graphicsPanel.removePlayerMP(((Packet01Disconnect) packet).getUsername());
                break;
            case ADDTILE:
                packet = new Packet02AddTile(data);
                graphicsPanel.addPlayerMPTile(((Packet02AddTile)packet).getUsername(), ((Packet02AddTile)packet).getR(), ((Packet02AddTile)packet).getC());
                break;
            case REMOVETILE:
                packet = new Packet03RemoveTile(data);
                graphicsPanel.getMap().getTiles()[((Packet03RemoveTile)packet).getR()][((Packet03RemoveTile)packet).getC()].setOwner(null);
                break;
        }
    }

    public void sendData(byte[] data){
        DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
        try {
            socket.send(packet);
         } catch (IOException e) {
            e.printStackTrace();
        }
    }
}