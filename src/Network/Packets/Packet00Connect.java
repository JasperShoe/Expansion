package Network.Packets;

import Network.Client;
import Network.Server;

public class Packet00Connect extends Packet {
    private String[] dataArray;

    private String username;

    private int castleR, castleC;

    public Packet00Connect(byte[] data){
        super(00);
        dataArray = readData(data).split(",");
        username = dataArray[0];
        castleR = Integer.parseInt(dataArray[1]);
        castleC = Integer.parseInt(dataArray[2]);
    }

    public Packet00Connect(String username, int castleR, int castleC){
        super(00);
        this.username = username;
        this.castleR = castleR;
        this.castleC = castleC;
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
        return ("00" + username + "," + castleR + "," + castleC).getBytes();
    }

    public String getUsername(){
        return username;
    }

    public int getCastleR() {
        return castleR;
    }

    public int getCastleC() {
        return castleC;
    }
}