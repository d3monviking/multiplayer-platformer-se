import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class MovingPlatformTest {

    @Test
    public void testPlatformMovementLogic() {
        Vec2 startPos = new Vec2(500, 500);
        Vec2 size = new Vec2(50, 10);
        MovingPlatform platform = new MovingPlatform(startPos, size);

        // 1. Test Default Move Right
        platform.movePlatform(); 
        assertTrue(platform.coordinates.x > 500, "Should move right initially");

        // 2. Test Boundary Reversal
        platform.coordinates.x = 600; // Hit Right Limit (500 + 100)
        platform.movePlatform(); // This sets movingLeft = true
        
        platform.movePlatform(); // Move Left
        assertTrue(platform.coordinates.x < 600, "Should move left after hitting boundary");
    }

    @Test
    public void testUpdatePlatformsList() {
        List<MovingPlatform> platforms = new ArrayList<>();
        MovingPlatform p1 = new MovingPlatform(new Vec2(100, 100), new Vec2(50, 10));
        MovingPlatform p2 = new MovingPlatform(new Vec2(200, 200), new Vec2(50, 10));
        platforms.add(p1);
        platforms.add(p2);

        // Verify initial state
        assertEquals(100, p1.coordinates.x);
        assertEquals(200, p2.coordinates.x);

        // Static update call
        MovingPlatform.updatePlatforms(platforms);

        // Both should have moved (default velocity is 1, default dir is right)
        assertEquals(101, p1.coordinates.x, 0.01f);
        assertEquals(201, p2.coordinates.x, 0.01f);
    }
    
    @Test
    public void testStaticFlagConsistency() {
        // Warning: 'moveLeft' is a static public boolean in MovingPlatform.java
        // This test documents that behavior. Changing one platform affects the flag for others read it?
        // Actually, the flag is static, but logic uses instance 'movingLeft' (private) for logic
        // and updates static 'moveLeft' for... rendering/client?
        
        MovingPlatform p = new MovingPlatform(new Vec2(0,0), new Vec2(10,10));
        
        // Reset
        MovingPlatform.moveLeft = true; 
        p.movePlatform(); // If internal is false (right), it sets static moveLeft = false
        
        assertFalse(MovingPlatform.moveLeft, "Instance movement should update the static flag");
    }
}