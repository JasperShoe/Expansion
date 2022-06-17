package Game;

import Network.Packets.Packet01Disconnect;
import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class Main extends JFrame implements WindowListener {
    public static int width = 900, height = 900, offset = 22;

    private GraphicsPanel graphicsPanel;

    public Main(){
        super("Expansion");
        setSize(width, height + offset);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        graphicsPanel = new GraphicsPanel();
        add(graphicsPanel);
        addWindowListener(this);
        setVisible(true);
    }

    public static void main(String[] args) {
        new Main();

    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        Packet01Disconnect disconnectPacket = new Packet01Disconnect(graphicsPanel.getPlayer().getUsername());
        disconnectPacket.writeData(graphicsPanel.socketClient);
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
