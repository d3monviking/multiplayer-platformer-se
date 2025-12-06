import java.lang.reflect.Method;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class CalculateTest {

    private Object invokeMethod(Object target, String methodName, Class<?>[] argTypes, Object... args) throws Exception {
        Method method = target.getClass().getDeclaredMethod(methodName, argTypes);
        method.setAccessible(true);
        return method.invoke(target, args);
    }

    @Test
    public void testAABBCollisionDetection_ExactOverlap() throws Exception {
        Calculate calculate = new Calculate(new LinkedBlockingQueue<>());
        Class<?>[] args = new Class<?>[]{float.class, float.class, float.class, float.class, float.class, float.class, float.class, float.class};

        assertTrue((boolean) invokeMethod(calculate, "checkCollision", args, 
            0f, 0f, 50f, 50f,   
            25f, 25f, 50f, 50f));

        assertFalse((boolean) invokeMethod(calculate, "checkCollision", args, 
            0f, 0f, 50f, 50f,   
            50f, 0f, 50f, 50f));
    }

    @Test
    public void testCollisionAdjustment_Horizontal() throws Exception {
        Calculate calculate = new Calculate(new LinkedBlockingQueue<>());
        Player player = new Player(null, new Vec2(0,0), 1, 0, 0);


        float[] newPos = (float[]) invokeMethod(calculate, "adjustToCollision", 
            new Class<?>[]{float.class, float.class, float.class, float.class, float.class, float.class, float.class, float.class, Player.class},
            45f, 0f, 50f, 50f,
            90f, 0f, 50f, 50f,
            player
        );
        
        assertEquals(40f, newPos[0], 0.001f);
        assertEquals(0f, newPos[1], 0.001f);
    }

    @Test
    public void testCollisionAdjustment_VerticalBounce() throws Exception {
        Calculate calculate = new Calculate(new LinkedBlockingQueue<>());
        Player player = new Player(null, new Vec2(0,0), 1, 0, 0);
        player.vel.y = 10f;
        
        float[] newPos = (float[]) invokeMethod(calculate, "adjustToCollision", 
            new Class<?>[]{float.class, float.class, float.class, float.class, float.class, float.class, float.class, float.class, Player.class},
            0f, 45f, 50f, 50f,
            0f, 90f, 50f, 50f,
            player
        );

        assertEquals(40f, newPos[1], 0.001f);
        
        assertEquals(-15f, player.getVelocity().y, 0.001f, "Player should bounce with specific velocity");
    }
}