package Network.Packets;

import Network.Client;
import Network.Server;

public class Packet02AddTile extends Packet {
    private String[] dataArray;

    private String username;

    private int r, c;

    public Packet02AddTile(byte[] data){
        super(02);
        dataArray = readData(data).split(",");
        username = dataArray[0];
        r = Integer.parseInt(dataArray[1]);
        c = Integer.parseInt(dataArray[2]);
    }

    public Packet02AddTile(String username, int r, int c){
        super(02);
        this.username = username;
        this.r = r;
        this.c = c;
    }

    @Override
    public void writeData(Client client) {
        client.sendData(getData());
    }

    @Override
    public void writeData(Server server) {
        server.sendDataToAllClients(getData());
    }

    @Override
    public byte[] getData() {
        return ("02" + username + "," + r + "," + c).getBytes();
    }

    public String getUsername() {
        return username;
    }

    public int getR(){
        return r;
    }

    public int getC() {
        return c;
    }
}