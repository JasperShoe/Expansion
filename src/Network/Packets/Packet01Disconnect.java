package Network.Packets;

import Network.Client;
import Network.Server;

public class Packet01Disconnect extends Packet {
    private String username;

    public Packet01Disconnect(byte[] data){
        super(01);
        username = readData(data);
    }

    public Packet01Disconnect(String data){
        super(01);
        username = data;
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
        return ("01" + username).getBytes();
    }

    public String getUsername() {
        return username;
    }
}
