import org.junit.jupiter.api.Test;
import java.net.InetSocketAddress;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerMechanicsTest {

    private Player createPlayer(int id) {
        return new Player(new InetSocketAddress("localhost", 8080), new Vec2(0,0), id, 0, 0);
    }
    
    @Test
    public void testPlayerDimensionsById() {
        // Player sizes are hardcoded in the constructor based on ID. 
        // We verify these magic numbers here.
        
        Player p1 = createPlayer(1);
        assertEquals(55, p1.size.x);
        assertEquals(81, p1.size.y);

        Player p2 = createPlayer(2);
        assertEquals(69, p2.size.x);
        assertEquals(80, p2.size.y);

        Player p3 = createPlayer(3);
        assertEquals(56, p3.size.x);
        assertEquals(94, p3.size.y);

        Player p4 = createPlayer(4);
        assertEquals(57, p4.size.x);
        assertEquals(80, p4.size.y);
    }

    @Test
    public void testPowerUpInventoryManagement() {
        Player p = createPlayer(1);
        
        // Create dummy powerups
        PowerUp pu1 = new PowerUp(new Vec2(0,0), new Vec2(0,0));
        PowerUp pu2 = new PowerUp(new Vec2(0,0), new Vec2(0,0));
        
        // Add to inventory
        p.powerUps.add(pu1);
        p.powerUps.add(pu2);
        
        assertEquals(2, p.powerUps.size());
        
        // Apply first powerup
        long time = System.currentTimeMillis();
        p.applyPowerUp(time);
        
        assertEquals(1, p.powerUps.size(), "Should remove one powerup after use");
        assertTrue(p.isBoostActive, "Boost should be active");
        assertEquals(time, p.boostStart);
        
        // Apply second powerup
        p.applyPowerUp(time + 100);
        assertEquals(0, p.powerUps.size(), "Should be empty after second use");
        assertEquals(time + 100, p.boostStart, "Boost start time should update");
    }
}