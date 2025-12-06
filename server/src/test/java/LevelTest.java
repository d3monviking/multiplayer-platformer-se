import java.net.InetSocketAddress;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LevelTest {

    private Level level;
    private Player player;

    @BeforeEach
    public void setUp() {
        level = new Level();
        player = new Player(new InetSocketAddress("localhost", 0), new Vec2(-1000, -1000), 1, 0, 0);
    }

    @Test
    public void testApplyInput_RightMovement() {
        boolean[] inputs = new boolean[5];
        inputs[3] = true; // Right

        assertEquals(0, player.vel.x);

        level.applyInput(player, inputs);

        assertEquals(4.0f, player.acc.x);
        assertEquals(4.0f, player.vel.x);
    }

    @Test
    public void testApplyInput_Friction() {
        player.vel.x = 10f;
        
        boolean[] inputs = new boolean[5];
        
        level.applyInput(player, inputs);

        assertEquals(0f, player.acc.x);
        assertEquals(9.0f, player.vel.x, 0.01f);
    }

    @Test
    public void testApplyInput_Jump() {
        player.onGround = true;
        boolean[] inputs = new boolean[5];
        inputs[4] = true; // Jump

        level.applyInput(player, inputs);

        assertEquals(-23.6f, player.vel.y, 0.01f);
        assertFalse(player.onGround);
    }
    
    @Test
    public void testGravityApplication() {
        player.vel.y = 0;
        player.acc.y = 0;
        
        boolean[] inputs = new boolean[5];
        level.applyInput(player, inputs);
        
        assertEquals(1.4f, player.acc.y, 0.01f);
        assertEquals(1.4f, player.vel.y, 0.01f);
    }
}