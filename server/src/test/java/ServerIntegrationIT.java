import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.flatbuffers.FlatBufferBuilder;

import Game.ClientMessage;
import Game.PlayerData;

public class ServerIntegrationIT {

    private Thread calculateThread;
    private BlockingQueue<ClientMessage> messageQueue;

    @BeforeEach
    public void setUp() throws Exception {
        Server.udpSocket = new DatagramSocket();

        if (Server.getPlayerList() != null) {
            Server.getPlayerList().clear();
        }
        
        messageQueue = new LinkedBlockingQueue<>();
        
        Calculate calculate = new Calculate(messageQueue);
        calculateThread = new Thread(calculate);
        calculateThread.start();
    }

    @AfterEach
    public void tearDown() {
        if (calculateThread != null) {
            calculateThread.interrupt();
        }
        Server.getPlayerList().clear();
        if (Server.udpSocket != null && !Server.udpSocket.isClosed()) {
            Server.udpSocket.close();
        }
    }

    private ClientMessage createTickMessage(int playerId) {
        FlatBufferBuilder builder = new FlatBufferBuilder(1024);
        
        PlayerData.startPlayerData(builder);
        PlayerData.addPlayerId(builder, playerId);
        
        int posOffset = Game.Vec2.createVec2(builder, 0.0f, 0.0f);
        PlayerData.addPos(builder, posOffset);
        
        int velOffset = Game.Vec2.createVec2(builder, 0.0f, -1.0f);
        PlayerData.addVel(builder, velOffset);

        
        int selfDataOffset = PlayerData.endPlayerData(builder);

        boolean[] inputs = new boolean[5]; 
        int inputOffset = ClientMessage.createPlayerInputVector(builder, inputs);
        
        ClientMessage.startClientMessage(builder);
        ClientMessage.addSelfData(builder, selfDataOffset);
        ClientMessage.addPlayerInput(builder, inputOffset);
        int messageOffset = ClientMessage.endClientMessage(builder);
        
        builder.finish(messageOffset);
        return ClientMessage.getRootAsClientMessage(builder.dataBuffer());
    }

    @Test
    public void testPlayerJoinAndGravityIntegration() throws Exception {
        InetAddress mockIp = InetAddress.getByName("127.0.0.1");
        Server.addNewPlayer(mockIp, 5000, 0);

        List<Player> players = Server.getPlayerList();
        assertEquals(1, players.size());
        Player p = players.get(0);

        p.setCoordinates(new Vec2(50.0f, -500.0f));
        
        float initialY = p.getCoordinates().getY();

        for (int i = 0; i < 100; i++) {
            messageQueue.put(createTickMessage(p.getPlayerId()));
            Thread.sleep(10);
        }

        assertNotEquals(initialY, p.getCoordinates().getY());
        assertTrue(p.getCoordinates().getY() > initialY);
    }

    @Test
    public void testMultiplePlayersCollisionIntegration() throws Exception {
        Server.addNewPlayer(InetAddress.getByName("127.0.0.1"), 5001, 0);
        Player p1 = Server.getPlayerList().get(0);
        p1.setCoordinates(new Vec2(100.0f, -500.0f)); 

        Server.addNewPlayer(InetAddress.getByName("127.0.0.1"), 5002, 0);
        Player p2 = Server.getPlayerList().get(1);
        p2.setCoordinates(new Vec2(100.0f, -500.0f));

        for (int i = 0; i < 50; i++) {
            messageQueue.put(createTickMessage(p1.getPlayerId()));
            messageQueue.put(createTickMessage(p2.getPlayerId()));
            Thread.sleep(10);
        }

        float dx = Math.abs(p1.getCoordinates().getX() - p2.getCoordinates().getX());
        float dy = Math.abs(p1.getCoordinates().getY() - p2.getCoordinates().getY());
        float minDist = 1e-3f;

        assertTrue(dx > minDist || dy > minDist);

    }
}