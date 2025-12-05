import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.net.InetSocketAddress;
import java.time.Instant;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    private Player player;
    private final InetSocketAddress TEST_ADDR = new InetSocketAddress("127.0.0.1", 9000);

    @BeforeEach
    public void setUp() {
        player = new Player(TEST_ADDR, new Vec2(100, 100), 1, 0, System.currentTimeMillis());
    }

    @Test
    public void testConstructorAndDefaults() {
        assertEquals(1, player.getPlayerId());
        assertEquals(TEST_ADDR, player.getAddress());
        assertEquals(100, player.getCoordinates().x);
        assertEquals(100, player.getCoordinates().y);
        assertTrue(player.onGround, "Player should start onGround=true");
        assertEquals(0, player.getVelocity().x);
        assertEquals(0, player.getVelocity().y);
        assertEquals(4f, player.runAcc);
        assertEquals(12f, player.maxSpeed);
    }

    @Test
    public void testPlayerSizesByID() {
        // ID 1
        Player p1 = new Player(TEST_ADDR, new Vec2(0,0), 1, 0, 0);
        assertEquals(55, p1.size.x);
        assertEquals(81, p1.size.y);

        // ID 2
        Player p2 = new Player(TEST_ADDR, new Vec2(0,0), 2, 0, 0);
        assertEquals(69, p2.size.x);
        assertEquals(80, p2.size.y);

        // ID 3
        Player p3 = new Player(TEST_ADDR, new Vec2(0,0), 3, 0, 0);
        assertEquals(56, p3.size.x);
        assertEquals(94, p3.size.y);

        // ID 4
        Player p4 = new Player(TEST_ADDR, new Vec2(0,0), 4, 0, 0);
        assertEquals(57, p4.size.x);
        assertEquals(80, p4.size.y);
    }

    @Test
    public void testSettersAndGetters() {
        // ID
        player.setPlayerId(99);
        assertEquals(99, player.getPlayerId());

        // Sequence Number
        player.setLastProcessedSeqNum(500);
        assertEquals(500, player.getLastProcessedSeqNum());

        // Coordinates
        Vec2 newPos = new Vec2(50, 50);
        player.setCoordinates(newPos);
        assertSame(newPos, player.getCoordinates());

        // Velocity
        Vec2 newVel = new Vec2(10, -10);
        player.setVel(newVel);
        assertSame(newVel, player.getVelocity());

        // Address
        InetSocketAddress newAddr = new InetSocketAddress("192.168.1.1", 80);
        player.setAddress(newAddr);
        assertEquals(newAddr, player.getAddress());
    }

    @Test
    public void testTimestampLogic() {
        long now = System.currentTimeMillis();
        player.setTimestamp(now);
        assertEquals(now, player.getTimestampMilli());

        // Wait a bit to ensure updateTimestamp works
        try { Thread.sleep(10); } catch (InterruptedException e) {}
        player.updateTimestamp();
        assertTrue(player.getTimestampMilli() > now);
    }

    @Test
    public void testPowerUpApplication() {
        PowerUp pu = new PowerUp(new Vec2(0,0), new Vec2(0,0));
        player.powerUps.add(pu);

        assertFalse(player.isBoostActive);
        
        long time = 10000L;
        player.applyPowerUp(time);

        assertTrue(player.isBoostActive);
        assertEquals(time, player.boostStart);
        assertTrue(player.powerUps.isEmpty(), "PowerUp should be consumed");
        
        // Verify speed boost effect (Base 12.0 * 4.0 = 48.0)
        // Note: applyPowerUp modifies vel.x logic in PowerUp.java
        // but current velocity is 0, so 0 * 4 is 0. 
        // We should set velocity first to test the multiplier.
        player.vel.x = 10f;
        player.maxSpeed = 10f;
        player.powerUps.add(new PowerUp(new Vec2(0,0), new Vec2(0,0)));
        player.applyPowerUp(time);
        
        assertEquals(40f, player.vel.x);
        assertEquals(40f, player.maxSpeed);
    }

    @Test
    public void testToString() {
        String str = player.toString();
        assertTrue(str.contains("playerId=1"));
        assertTrue(str.contains("coordinates=100.0, 100.0"));
    }
}