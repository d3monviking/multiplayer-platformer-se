import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.net.InetSocketAddress;
import static org.junit.jupiter.api.Assertions.*;

public class LevelTest {

    private Level level;
    private Player player;

    @BeforeEach
    public void setUp() {
        level = new Level();
        // FIX: Spawn player at (-1000, -1000) to ensure they are in "void" space.
        // This prevents immediate collision with tiles loaded from the CSV map files.
        player = new Player(new InetSocketAddress("localhost", 0), new Vec2(-1000, -1000), 1, 0, 0);
    }

    @Test
    public void testApplyInput_RightMovement() {
        // inputs: [?, Left, ?, Right, Jump]
        boolean[] inputs = new boolean[5];
        inputs[3] = true; // Right

        // Initial check
        assertEquals(0, player.vel.x);

        // Apply input
        level.applyInput(player, inputs);

        // Logic: acc.x = runAcc (4.0). vel.x += acc.x.
        // Since we are at (-1000, -1000), no collision should occur, so vel remains 4.0.
        assertEquals(4.0f, player.acc.x);
        assertEquals(4.0f, player.vel.x);
    }

    @Test
    public void testApplyInput_Friction() {
        // Set up player moving right
        player.vel.x = 10f;
        
        // No inputs
        boolean[] inputs = new boolean[5];
        
        level.applyInput(player, inputs);

        // Logic: acc.x = 0. vel.x *= FRICTION (0.9).
        assertEquals(0f, player.acc.x);
        assertEquals(9.0f, player.vel.x, 0.01f);
    }

    @Test
    public void testApplyInput_Jump() {
        player.onGround = true;
        boolean[] inputs = new boolean[5];
        inputs[4] = true; // Jump

        level.applyInput(player, inputs);

        // Logic Update: 
        // 1. Input processing sets vel.y = -25.0f (JUMP_FORCE)
        // 2. applyInput calls yCollisions(self)
        // 3. yCollisions applies gravity: vel.y += 1.4f
        // 4. Final Result = -25.0 + 1.4 = -23.6
        assertEquals(-23.6f, player.vel.y, 0.01f);
        assertFalse(player.onGround);
    }
    
    @Test
    public void testGravityApplication() {
        // applyInput calls yCollisions which applies gravity
        player.vel.y = 0;
        player.acc.y = 0;
        
        boolean[] inputs = new boolean[5];
        level.applyInput(player, inputs);
        
        // Gravity is 1.4f applied in yCollisions
        assertEquals(1.4f, player.acc.y, 0.01f);
        assertEquals(1.4f, player.vel.y, 0.01f);
    }
}