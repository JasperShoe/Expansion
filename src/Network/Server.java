package Network;

import Entities.PlayerMP;
import Game.GraphicsPanel;
import Network.Packets.*;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

public class Server extends Thread {
    private DatagramSocket socket;

    private GraphicsPanel graphicsPanel;

    private ArrayList<PlayerMP> connectedPlayers = new ArrayList<>();

    public Server(GraphicsPanel graphicsPanel, int port){
        this.graphicsPanel = graphicsPanel;
        try {
            this.socket = new DatagramSocket(port);
        } catch (SocketException e) {
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
                System.out.println("[" + ip.getHostAddress() + ":" + port + "] " + ((Packet00Connect)packet).getUsername() + " has connected to the server.");

                PlayerMP player;
                if(((Packet00Connect) packet).getUsername().equalsIgnoreCase(graphicsPanel.getPlayer().getUsername())) {
                    player = new PlayerMP(graphicsPanel, ((Packet00Connect) packet).getUsername(), ((Packet00Connect) packet).getCastleR(), ((Packet00Connect) packet).getCastleC(), ip, port, false);
                } else {
                    player = new PlayerMP(graphicsPanel, ((Packet00Connect) packet).getUsername(), ((Packet00Connect) packet).getCastleR(), ((Packet00Connect) packet).getCastleC(), ip, port, true);
                }
                addConnection(player, (Packet00Connect) packet);
                break;
            case DISCONNECT:
                packet = new Packet01Disconnect(data);
                System.out.println("[" + ip.getHostAddress() + ":" + port + "] " + ((Packet01Disconnect)packet).getUsername() + " has left.");
                removeConnection((Packet01Disconnect) packet);
                graphicsPanel.removePlayerMP(((Packet01Disconnect)packet).getUsername());
                break;
            case ADDTILE:
                packet = new Packet02AddTile(data);
                packet.writeData(this);
                graphicsPanel.addPlayerMPTile(((Packet02AddTile)packet).getUsername(), ((Packet02AddTile)packet).getR(), ((Packet02AddTile)packet).getC());
                break;
            case REMOVETILE:
                packet = new Packet03RemoveTile(data);
                packet.writeData(this);
                graphicsPanel.getMap().getTiles()[((Packet03RemoveTile)packet).getR()][((Packet03RemoveTile)packet).getC()].setOwner(null);
                break;
        }
    }

    public void addConnection(PlayerMP player, Packet00Connect packet) {
        boolean alreadyConnected = false;

        for(PlayerMP p : connectedPlayers){
            if(p.getUsername().equalsIgnoreCase(player.getUsername())){
                 if(p.ip == null){
                     p.ip = player.ip;
                 }

                 if(p.port == -1){
                     p.port = player.port;
                 }

                 alreadyConnected = true;
            } else {
                sendData(packet.getData(), p.ip, p.port);
                packet = new Packet00Connect(p.getUsername(), p.getCastleR(), p.getCastleC());
                sendData(packet.getData(), player.ip, player.port);
            }
        }

        if(!alreadyConnected){
            this.connectedPlayers.add(player);
            graphicsPanel.addPlayerMP(player);
        }
    }

    public void removeConnection(Packet01Disconnect packet){
        connectedPlayers.remove(getPlayerMPIndex(packet.getUsername()));
        packet.writeData(this );
    }

    public int getPlayerMPIndex(String username){
        int index = 0;
        for (PlayerMP player : connectedPlayers){
            if(player.getUsername().equalsIgnoreCase(username)){
                break;
            }
            index++;
        }
        return index;
    }

    public void sendData(byte[] data, InetAddress ip, int port){
        DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendDataToAllClients(byte[] data) {
        for (PlayerMP p : connectedPlayers){
            sendData(data, p.ip, p.port);
        }
    }
}