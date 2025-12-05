import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.net.InetAddress;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class ServerTest {

    @BeforeEach
    public void clearPlayers() {
        Server.getPlayerList().clear();
    }

    @Test
    public void testAddNewPlayer() throws Exception {
        InetAddress mockAddr = InetAddress.getByName("127.0.0.1");
        int port = 12345;

        // Note: addNewPlayer calls udpSocket.send(). 
        // If Server.udpSocket is null (which it is in a test environment without main() running),
        // this will throw a NullPointerException.
        // To fix this for testing, you would usually initialize a Dummy DatagramSocket.
        
        try {
            Server.udpSocket = new java.net.DatagramSocket(null); // Unbound socket
            Server.addNewPlayer(mockAddr, port, 1);
        } catch (Exception e) {
            // We expect some network failure or NPE depending on implementation details
            // but we want to check if the logic added the player to the list BEFORE sending.
        }

        // However, looking at your code, the player is added BEFORE the socket send.
        // So even if the socket fails, the list might have the player.
        // Ideally, refactor Server.java to separate List logic from Network logic.
        
        List<Player> players = Server.getPlayerList();
        assertEquals(1, players.size());
        assertEquals(port, players.get(0).getAddress().getPort());
    }

    @Test
    public void testDuplicatePlayerPrevention() throws Exception {
        Server.udpSocket = new java.net.DatagramSocket(null);
        InetAddress mockAddr = InetAddress.getByName("127.0.0.1");
        
        Server.addNewPlayer(mockAddr, 5555, 1);
        Server.addNewPlayer(mockAddr, 5555, 1); // Add same again

        assertEquals(1, Server.getPlayerList().size(), "Should not add duplicate IP/Port combination");
    }
}