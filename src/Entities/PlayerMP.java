package Entities;

import Game.GraphicsPanel;

import java.net.InetAddress;

public class PlayerMP extends Player {
    public static InetAddress ip;

    public static int port;

    public PlayerMP(GraphicsPanel graphicsPanel, String username, int castleR, int castleC, InetAddress ip, int port, boolean isMP) {
        super(graphicsPanel, username, castleR, castleC);
        this.ip = ip;
        this.port = port;

        if(isMP){
            super.updateColor(Player.COLORMP);
        }
    }
}
