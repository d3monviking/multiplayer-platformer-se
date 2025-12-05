import org.junit.jupiter.api.Test;
import java.net.InetSocketAddress;
import static org.junit.jupiter.api.Assertions.*;

public class PowerUpTest {

    @Test
    public void testPowerUpType() {
        PowerUp p = new PowerUp(new Vec2(0,0), new Vec2(10,10));
        assertEquals('P', p.getType());
    }

    @Test
    public void testApplyBoost() {
        PowerUp p = new PowerUp(new Vec2(0,0), new Vec2(10,10));
        
        // Mock a player
        Player player = new Player(
            new InetSocketAddress("localhost", 8080),
            new Vec2(0,0), 
            1, 
            0, 
            System.currentTimeMillis()
        );

        player.vel.x = 10.0f;
        float initialMaxSpeed = player.maxSpeed;

        p.applyBoost(player);

        // Check if speed boosted by 4x (as defined in PowerUp.java)
        assertEquals(40.0f, player.vel.x, 0.01f);
        assertEquals(initialMaxSpeed * 4.0f, player.maxSpeed, 0.01f);
        assertTrue(p.boostClock > 0, "Boost clock should be set");
    }
}