import java.net.InetAddress;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ServerTest {

    @BeforeEach
    public void clearPlayers() {
        Server.getPlayerList().clear();
    }

    @Test
    public void testAddNewPlayer() throws Exception {
        InetAddress mockAddr = InetAddress.getByName("127.0.0.1");
        int port = 12345;

        
        try {
            Server.udpSocket = new java.net.DatagramSocket(null); 
            Server.addNewPlayer(mockAddr, port, 1);
        } catch (Exception e) {

        }

        
        List<Player> players = Server.getPlayerList();
        assertEquals(1, players.size());
        assertEquals(port, players.get(0).getAddress().getPort());
    }

    @Test
    public void testDuplicatePlayerPrevention() throws Exception {
        Server.udpSocket = new java.net.DatagramSocket(null);
        InetAddress mockAddr = InetAddress.getByName("127.0.0.1");
        
        Server.addNewPlayer(mockAddr, 5555, 1);
        Server.addNewPlayer(mockAddr, 5555, 1); 

        assertEquals(1, Server.getPlayerList().size(), "Should not add duplicate IP/Port combination");
    }
}