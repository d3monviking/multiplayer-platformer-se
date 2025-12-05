import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import Game.ClientMessage; 
import static org.junit.jupiter.api.Assertions.*;

public class CalculateTest {

    // Helper to access private methods
    private Object invokeMethod(Object target, String methodName, Class<?>[] argTypes, Object... args) throws Exception {
        Method method = target.getClass().getDeclaredMethod(methodName, argTypes);
        method.setAccessible(true);
        return method.invoke(target, args);
    }

    @Test
    public void testAABBCollisionDetection_ExactOverlap() throws Exception {
        Calculate calculate = new Calculate(new LinkedBlockingQueue<>());
        Class<?>[] args = new Class<?>[]{float.class, float.class, float.class, float.class, float.class, float.class, float.class, float.class};

        // 1. Partial Overlap
        assertTrue((boolean) invokeMethod(calculate, "checkCollision", args, 
            0f, 0f, 50f, 50f,   
            25f, 25f, 50f, 50f));

        // 2. Exact Edge Touching (Should be FALSE in typically exclusive AABB, logic: x1 < x2 + w2)
        // Rect 1: 0-50. Rect 2: 50-100.
        // 0 < 100 && 50 > 50 -> False.
        assertFalse((boolean) invokeMethod(calculate, "checkCollision", args, 
            0f, 0f, 50f, 50f,   
            50f, 0f, 50f, 50f));
    }

    @Test
    public void testCollisionAdjustment_Horizontal() throws Exception {
        Calculate calculate = new Calculate(new LinkedBlockingQueue<>());
        Player player = new Player(null, new Vec2(0,0), 1, 0, 0);

        // Player (Width 50) at X=45. Object (Width 50) at X=90.
        // Player Right=95, Object Left=90. Overlap = 5.
        // Center P=70, Center O=115. Delta X = -45.
        // Should push Left.
        float[] newPos = (float[]) invokeMethod(calculate, "adjustToCollision", 
            new Class<?>[]{float.class, float.class, float.class, float.class, float.class, float.class, float.class, float.class, Player.class},
            45f, 0f, 50f, 50f,
            90f, 0f, 50f, 50f,
            player
        );
        
        // Original X=45. Should become 45 - 5 = 40.
        assertEquals(40f, newPos[0], 0.001f);
        assertEquals(0f, newPos[1], 0.001f);
    }

    @Test
    public void testCollisionAdjustment_VerticalBounce() throws Exception {
        Calculate calculate = new Calculate(new LinkedBlockingQueue<>());
        Player player = new Player(null, new Vec2(0,0), 1, 0, 0);
        player.vel.y = 10f; // Moving down

        // Player (Height 50) at Y=45. Object (Height 50) at Y=90.
        // Overlap Y = 5. Player is ABOVE object (Delta Y < 0).
        // Logic: if deltaY < 0 (Player center < Obj center) -> Move UP.
        
        float[] newPos = (float[]) invokeMethod(calculate, "adjustToCollision", 
            new Class<?>[]{float.class, float.class, float.class, float.class, float.class, float.class, float.class, float.class, Player.class},
            0f, 45f, 50f, 50f,
            0f, 90f, 50f, 50f,
            player
        );

        // Should move up by overlap (45 - 5 = 40)
        assertEquals(40f, newPos[1], 0.001f);
        
        // Check bounce effect logic in code:
        // "Move player up (bouncing) ... player.getVelocity().y = -15;"
        assertEquals(-15f, player.getVelocity().y, 0.001f, "Player should bounce with specific velocity");
    }
}