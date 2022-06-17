package Network.Packets;

import Network.Client;
import Network.Server;

public abstract class Packet {
    public enum PacketTypes {
        INVALID(-1),
        CONNECT(00),
        DISCONNECT(01),
        ADDTILE(02),
        REMOVETILE(03);

        private int packetID;

        PacketTypes(int packetID){
            this.packetID = packetID;
        }

        public int getPacketID(){
            return packetID;
        }
    }

    public byte packetID;

    public Packet(int packetID){
        this.packetID = (byte) packetID;
    }

    public abstract void writeData(Client client);

    public abstract void writeData(Server server);

    public String readData(byte[] data){
        String message = new String(data).trim();
        return message.substring(2);
    }

    public abstract byte[] getData();

    public static PacketTypes lookUpPacket(int id){
        for(PacketTypes p : PacketTypes.values()){
            if (p.getPacketID() == id){
                return p;
            }
        }
        return PacketTypes.INVALID;
    }

    public static PacketTypes lookUpPacket(String packetID) {
        try {
            return lookUpPacket(Integer.parseInt(packetID));
        } catch(NumberFormatException e) {
            return PacketTypes.INVALID;
        }
    }
}
